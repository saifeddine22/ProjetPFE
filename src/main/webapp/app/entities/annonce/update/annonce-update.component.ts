import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAnnonce, Annonce } from '../annonce.model';
import { AnnonceService } from '../service/annonce.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICommune } from 'app/entities/commune/commune.model';
import { CommuneService } from 'app/entities/commune/service/commune.service';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';
import { IActivite } from 'app/entities/activite/activite.model';
import { ActiviteService } from 'app/entities/activite/service/activite.service';
import { AIService, ServiceDetailsDTO } from '../ai.service';

@Component({
  selector: 'jhi-annonce-update',
  templateUrl: './annonce-update.component.html',
})
export class AnnonceUpdateComponent implements OnInit {
  isSaving = false;
  isLoadingAISuggestions = false;

  // Variables pour les suggestions IA
  titleSuggestions: { title: string } | null = null;
  aiSuggestions: { enhancedDescription: string } | null = null;
  keywordSuggestions: string[] = [];
  priceSuggestions: { minPrice: number; maxPrice: number; currency: string } | null = null;

  usersSharedCollection: IUser[] = [];
  communesSharedCollection: ICommune[] = [];
  categoriesSharedCollection: ICategorie[] = [];
  activitesSharedCollection: IActivite[] = [];

  editForm = this.fb.group({
    id: [],
    titre: [null, [Validators.required]],
    description: [null, [Validators.required]],
    adresse: [null, [Validators.required]],
    latitude: [],
    longitude: [],
    status: [],
    dateAnnonce: [],
    user: [null, Validators.required],
    commune: [],
    categorie: [],
    activite: [null, Validators.required],
  });

  constructor(
    protected annonceService: AnnonceService,
    protected userService: UserService,
    protected communeService: CommuneService,
    protected categorieService: CategorieService,
    protected activiteService: ActiviteService,
    protected aiService: AIService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annonce }) => {
      if (annonce.id === undefined) {
        const today = dayjs().startOf('day');
        annonce.dateAnnonce = today;
      }

      this.updateForm(annonce);
      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const annonce = this.createFromForm();
    if (annonce.id !== undefined) {
      this.subscribeToSaveResponse(this.annonceService.update(annonce));
    } else {
      this.subscribeToSaveResponse(this.annonceService.create(annonce));
    }
  }

  // Méthode pour suggérer un titre en fonction de la description
  suggestTitle(): void {
    const description = this.editForm.get('description')?.value;

    if (!description) {
      return;
    }

    this.isLoadingAISuggestions = true;

    this.aiService.generateTitle(description).subscribe(
      (response: { title: string }) => {
        this.titleSuggestions = response;
        this.isLoadingAISuggestions = false;
      },
      (error: Error) => {
        console.error('Erreur lors de la génération du titre:', error);
        this.isLoadingAISuggestions = false;
      }
    );
  }

  // Méthode pour appliquer la suggestion de titre
  applyTitleSuggestion(): void {
    if (this.titleSuggestions?.title) {
      this.editForm.patchValue({
        titre: this.titleSuggestions.title,
      });
      this.titleSuggestions = null;
    }
  }

  // Méthode pour améliorer la description avec l'IA
  enhanceWithAI(): void {
    const description = this.editForm.get('description')?.value;

    if (!description) {
      return;
    }

    this.isLoadingAISuggestions = true;

    this.aiService.enhanceDescription(description).subscribe(
      (response: { enhancedDescription: string }) => {
        this.aiSuggestions = response;
        this.isLoadingAISuggestions = false;
      },
      (error: Error) => {
        console.error("Erreur lors de l'amélioration de la description:", error);
        this.isLoadingAISuggestions = false;
      }
    );
  }

  // Méthode pour appliquer la suggestion de description
  applyDescriptionSuggestion(): void {
    if (this.aiSuggestions?.enhancedDescription) {
      this.editForm.patchValue({
        description: this.aiSuggestions.enhancedDescription,
      });
      this.aiSuggestions = null;
    }
  }

  // Méthode pour suggérer des mots-clés
  suggestKeywords(): void {
    const description = this.editForm.get('description')?.value;
    const serviceType = this.getServiceTypeFromForm();

    if (!description) {
      return;
    }

    this.isLoadingAISuggestions = true;

    this.aiService.suggestKeywords({ description, serviceType }).subscribe(
      (result: string[]) => {
        this.keywordSuggestions = result;
        this.isLoadingAISuggestions = false;
      },
      (error: Error) => {
        console.error('Erreur lors de la génération des mots-clés:', error);
        this.isLoadingAISuggestions = false;
      }
    );
  }

  // Méthode pour estimer le prix
  estimatePrice(): void {
    const description = this.editForm.get('description')?.value;
    const categorieId = this.editForm.get('categorie')?.value?.id;
    const activiteId = this.editForm.get('activite')?.value?.id;

    if (!description) {
      return;
    }

    this.isLoadingAISuggestions = true;

    const serviceDetails: ServiceDetailsDTO = {
      description,
      categorieId,
      activiteId,
    };

    this.aiService.estimatePrice(serviceDetails).subscribe(
      (response: { minPrice: number; maxPrice: number; currency: string }) => {
        this.priceSuggestions = response;
        this.isLoadingAISuggestions = false;
      },
      (error: Error) => {
        console.error("Erreur lors de l'estimation du prix:", error);
        this.isLoadingAISuggestions = false;
      }
    );
  }

  onChekCategorie(): ICategorie | null {
    return this.editForm.get('categorie')?.value as ICategorie | null;
  }

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }

  trackCommuneById(_index: number, item: ICommune): number {
    return item.id!;
  }

  trackCategorieById(_index: number, item: ICategorie): number {
    return item.id!;
  }

  trackActiviteById(_index: number, item: IActivite): number {
    return item.id!;
  }

  createFromForm(): IAnnonce {
    return {
      ...new Annonce(),
      id: this.editForm.get('id')?.value,
      titre: this.editForm.get('titre')?.value,
      description: this.editForm.get('description')?.value,
      adresse: this.editForm.get('adresse')?.value,
      latitude: this.editForm.get('latitude')?.value,
      longitude: this.editForm.get('longitude')?.value,
      status: this.editForm.get('status')?.value,
      dateAnnonce: this.editForm.get('dateAnnonce')?.value ? dayjs(this.editForm.get('dateAnnonce')?.value, DATE_TIME_FORMAT) : undefined,
      user: this.editForm.get('user')?.value,
      commune: this.editForm.get('commune')?.value,
      categorie: this.editForm.get('categorie')?.value,
      activite: this.editForm.get('activite')?.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnnonce>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(annonce: IAnnonce): void {
    this.editForm.patchValue({
      id: annonce.id,
      titre: annonce.titre,
      description: annonce.description,
      adresse: annonce.adresse,
      latitude: annonce.latitude,
      longitude: annonce.longitude,
      status: annonce.status,
      dateAnnonce: annonce.dateAnnonce ? annonce.dateAnnonce.format(DATE_TIME_FORMAT) : null,
      user: annonce.user,
      commune: annonce.commune,
      categorie: annonce.categorie,
      activite: annonce.activite,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, annonce.user);
    this.communesSharedCollection = this.communeService.addCommuneToCollectionIfMissing(this.communesSharedCollection, annonce.commune);
    this.categoriesSharedCollection = this.categorieService.addCategorieToCollectionIfMissing(
      this.categoriesSharedCollection,
      annonce.categorie
    );
    this.activitesSharedCollection = this.activiteService.addActiviteToCollectionIfMissing(
      this.activitesSharedCollection,
      annonce.activite
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')?.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.communeService
      .query()
      .pipe(map((res: HttpResponse<ICommune[]>) => res.body ?? []))
      .pipe(
        map((communes: ICommune[]) => this.communeService.addCommuneToCollectionIfMissing(communes, this.editForm.get('commune')?.value))
      )
      .subscribe((communes: ICommune[]) => (this.communesSharedCollection = communes));

    this.categorieService
      .query()
      .pipe(map((res: HttpResponse<ICategorie[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategorie[]) =>
          this.categorieService.addCategorieToCollectionIfMissing(categories, this.editForm.get('categorie')?.value)
        )
      )
      .subscribe((categories: ICategorie[]) => (this.categoriesSharedCollection = categories));

    this.activiteService
      .query()
      .pipe(map((res: HttpResponse<IActivite[]>) => res.body ?? []))
      .pipe(
        map((activites: IActivite[]) =>
          this.activiteService.addActiviteToCollectionIfMissing(activites, this.editForm.get('activite')?.value)
        )
      )
      .subscribe((activites: IActivite[]) => (this.activitesSharedCollection = activites));
  }

  // Méthode utilitaire privée - doit être en dernier selon les règles de linting
  private getServiceTypeFromForm(): string | undefined {
    const categorie = this.editForm.get('categorie')?.value as ICategorie | null;
    const activite = this.editForm.get('activite')?.value as IActivite | null;

    if (categorie?.nomFr) {
      return categorie.nomFr;
    } else if (activite?.nomFr) {
      return activite.nomFr;
    }

    return undefined;
  }
}
