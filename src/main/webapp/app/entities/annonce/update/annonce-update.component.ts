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
import { IActivite } from 'app/entities/activite/activite.model';
import { ActiviteService } from 'app/entities/activite/service/activite.service';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';

@Component({
  selector: 'jhi-annonce-update',
  templateUrl: './annonce-update.component.html',
})
export class AnnonceUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  categoriesSharedCollection: ICategorie[] = [];
  communesSharedCollection: ICommune[] = [];
  activitesSharedCollection: IActivite[] = [];

  editForm = this.fb.group({
    id: [],
    titre: [null, [Validators.required]],
    description: [null, [Validators.required]],
    adresse: [null, [Validators.required]],
    status: [],

    dateAnnonce: [],
    latitude: [],
    longitude: [],
    user: [],
    categorie: [],
    commune: [],
    activite: [null, Validators.required],
  });

  constructor(
    public annonceService: AnnonceService,
    protected userService: UserService,
    protected categorieService: CategorieService,
    protected communeService: CommuneService,
    protected activiteService: ActiviteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  onChekCategorie(): number {
    const catId = this.editForm.get(['categorie'])!.value;
    return Number(catId);
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annonce }) => {
      if (annonce.id === undefined) {
        const today = dayjs().startOf('minutes');
        annonce.dateAnnonce = today;
      }
      this.updateForm(annonce);
      this.loadRelationshipsOptions();
      
      sessionStorage.setItem('dataAnnonce', JSON.stringify(annonce));
      this.annonceService.initilizeMap();
      this.annonceService.vectorMap();
      this.annonceService.map.removeInteraction(this.annonceService.draw);
      this.annonceService.map.addInteraction(this.annonceService.draw);
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

  trackUserById(_index: number, item: IUser): number {
    return item.id!;
  }

  trackCategorieById(_index: number, item: ICategorie): number {
    return item.id!;
  }

  trackCommuneById(_index: number, item: ICommune): number {
    return item.id!;
  }

  trackActiviteById(_index: number, item: IActivite): number {
    return item.id!;
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
      status: annonce.status,
      dateAnnonce: annonce.dateAnnonce ? annonce.dateAnnonce.format(DATE_TIME_FORMAT) : null,
      latitude: annonce.latitude,
      longitude: annonce.longitude,
      user: annonce.user,
      categorie: annonce.categorie,
      commune: annonce.commune,
      activite: annonce.activite,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, annonce.user);
    this.categoriesSharedCollection = this.categorieService.addCategorieToCollectionIfMissing(
      this.categoriesSharedCollection,
      annonce.categorie
    );
    this.communesSharedCollection = this.communeService.addCommuneToCollectionIfMissing(this.communesSharedCollection, annonce.commune);
    this.activitesSharedCollection = this.activiteService.addActiviteToCollectionIfMissing(
      this.activitesSharedCollection,
      annonce.activite
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.categorieService
      .query({ size: 25 })
      .pipe(map((res: HttpResponse<ICategorie[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategorie[]) =>
          this.categorieService.addCategorieToCollectionIfMissing(categories, this.editForm.get('categorie')!.value)
        )
      )
      .subscribe((categories: ICategorie[]) => (this.categoriesSharedCollection = categories));

    this.communeService
      .query()
      .pipe(map((res: HttpResponse<ICommune[]>) => res.body ?? []))
      .pipe(
        map((communes: ICommune[]) => this.communeService.addCommuneToCollectionIfMissing(communes, this.editForm.get('commune')!.value))
      )
      .subscribe((communes: ICommune[]) => (this.communesSharedCollection = communes));

    this.activiteService
      .query({ size: 200 })
      .pipe(map((res: HttpResponse<IActivite[]>) => res.body ?? []))
      .pipe(
        map((activites: IActivite[]) =>
          this.activiteService.addActiviteToCollectionIfMissing(activites, this.editForm.get('activite')!.value)
        )
      )
      .subscribe((activites: IActivite[]) => (this.activitesSharedCollection = activites));
  }

  protected createFromForm(): IAnnonce {
    return {
      ...new Annonce(),
      id: this.editForm.get(['id'])!.value,
      titre: this.editForm.get(['titre'])!.value,
      description: this.editForm.get(['description'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      status: this.editForm.get(['status'])!.value,
      dateAnnonce: this.editForm.get(['dateAnnonce'])!.value
        ? dayjs(this.editForm.get(['dateAnnonce'])!.value, DATE_TIME_FORMAT)
        : undefined,
      latitude: this.annonceService.latitude,
      longitude: this.annonceService.longitude,
      user: { id: Number(sessionStorage.getItem('userConnectedId')) },
      categorie: this.editForm.get(['categorie'])!.value,
      commune: this.editForm.get(['commune'])!.value,
      activite: this.editForm.get(['activite'])!.value,
    };
  }
}
