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
    user: [], // Retiré Validators.required temporairement
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

  // MÉTHODES PUBLIQUES (doivent être avant les méthodes privées/protected)

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annonce }) => {
      if (annonce.id === undefined) {
        const today = dayjs().startOf('day');
        annonce.dateAnnonce = today;
      }

      this.updateForm(annonce);
      this.loadRelationshipsOptions();

      // Réactiver l'auto-sélection de l'utilisateur
      if (!annonce.user) {
        this.autoSelectUser();
      }
    });
  }

  // Nouvelle méthode pour sélectionner automatiquement un utilisateur
  autoSelectUser(): void {
    // Récupérer l'ID de l'utilisateur connecté depuis sessionStorage
    const connectedUserIdString = sessionStorage.getItem('userConnectedId');
    const connectedUserId = Number(connectedUserIdString);

    console.log('=== DEBUG UTILISATEUR ===');
    console.log('sessionStorage userConnectedId (string):', connectedUserIdString);
    console.log('sessionStorage userConnectedId (number):', connectedUserId);
    console.log('Tous les items sessionStorage:');
    for (let i = 0; i < sessionStorage.length; i++) {
      const key = sessionStorage.key(i);
      if (key) {
        const value = sessionStorage.getItem(key);
        console.log('  ' + key + ': ' + String(value));
      }
    }
    console.log('=========================');

    // Fonction pour trouver et sélectionner l'utilisateur connecté
    const selectConnectedUser = (): void => {
      if (this.usersSharedCollection.length > 0) {
        console.log('Utilisateurs disponibles:', this.usersSharedCollection);

        // Chercher l'utilisateur connecté dans la collection
        const connectedUser = this.usersSharedCollection.find(user => user.id === connectedUserId);

        if (connectedUser && connectedUserId && connectedUserId !== 0) {
          this.editForm.patchValue({
            user: connectedUser,
          });
          console.log('✅ Utilisateur connecté trouvé et sélectionné:');
          console.log(connectedUser);
        } else {
          console.warn('❌ Utilisateur connecté non trouvé. Recherche par login...');

          // Essayer de trouver par d'autres moyens (login stocké séparément)
          const currentUserLogin = sessionStorage.getItem('currentUserLogin') ?? sessionStorage.getItem('userLogin');
          console.log('Recherche par login:', currentUserLogin);

          if (currentUserLogin) {
            const userByLogin = this.usersSharedCollection.find(user => user.login === currentUserLogin);
            if (userByLogin) {
              this.editForm.patchValue({ user: userByLogin });
              console.log('✅ Utilisateur trouvé par login:', userByLogin);
              return;
            }
          }

          // Fallback : utiliser le premier utilisateur
          console.warn('⚠️ Utilisation du premier utilisateur par défaut');
          const firstUser = this.usersSharedCollection[0];
          this.editForm.patchValue({
            user: firstUser,
          });
          console.log('Premier utilisateur sélectionné par défaut:');
          console.log(firstUser);
        }
      }
    };

    // Vérifier d'abord si les utilisateurs sont déjà chargés
    if (this.usersSharedCollection.length > 0 && !this.editForm.get('user')?.value) {
      selectConnectedUser();
    } else {
      // Sinon, attendre que les utilisateurs soient chargés
      const checkUsers = setInterval(() => {
        if (this.usersSharedCollection.length > 0) {
          clearInterval(checkUsers);
          if (!this.editForm.get('user')?.value) {
            selectConnectedUser();
          }
        }
      }, 100);

      // Sécurité : arrêter après 5 secondes
      setTimeout(() => {
        clearInterval(checkUsers);
      }, 5000);
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const annonce = this.createFromForm();

    // Log pour débugger les données envoyées
    console.log("Données de l'annonce à sauvegarder:", annonce);
    console.log('Annonce JSON:');
    console.log(JSON.stringify(annonce, null, 2));

    if (annonce.id !== undefined) {
      this.subscribeToSaveResponse(this.annonceService.update(annonce));
    } else {
      this.subscribeToSaveResponse(this.annonceService.create(annonce));
    }
  }

  // Méthode pour débugger le formulaire
  debugForm(): void {
    console.log('Form valid:', this.editForm.valid);
    console.log('Form errors:', this.editForm.errors);

    Object.keys(this.editForm.controls).forEach(key => {
      const control = this.editForm.get(key);
      if (control?.invalid) {
        console.log('Field invalid:', key);
        console.log('Errors:', control.errors);
      }
    });
  }

  // Méthode pour suggérer un titre en fonction de la description
  suggestTitle(): void {
    const description = this.editForm.get('description')?.value;

    if (!description) {
      alert("Veuillez d'abord saisir une description");
      return;
    }

    this.isLoadingAISuggestions = true;

    // Ajout de gestion d'erreur plus robuste
    this.aiService.generateTitle(description).subscribe({
      next: (response: { title: string }) => {
        this.titleSuggestions = response;
        this.isLoadingAISuggestions = false;
      },
      error: (error: any) => {
        console.error('Erreur lors de la génération du titre:', error);
        alert('Erreur lors de la génération du titre. Vérifiez que le service IA est disponible.');
        this.isLoadingAISuggestions = false;
      },
    });
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
    const formValue = this.editForm.value;

    // Utilisation de l'objet Annonce avec les bonnes valeurs
    const annonce = new Annonce();

    // Champs de base
    annonce.id = formValue.id;
    annonce.titre = formValue.titre || '';
    annonce.description = formValue.description || '';
    annonce.adresse = formValue.adresse || '';
    annonce.latitude = formValue.latitude;
    annonce.longitude = formValue.longitude;
    annonce.status = formValue.status ?? false;
    annonce.dateAnnonce = formValue.dateAnnonce ? dayjs(formValue.dateAnnonce, DATE_TIME_FORMAT) : dayjs();

    // Relations - seulement les objets avec ID
    if (formValue.user?.id) {
      annonce.user = { id: formValue.user.id } as IUser;
    } else {
      // Fallback : utiliser l'utilisateur connecté si aucun n'est sélectionné
      const connectedUserId = Number(sessionStorage.getItem('userConnectedId'));
      if (connectedUserId && this.usersSharedCollection.length > 0) {
        const connectedUser = this.usersSharedCollection.find(user => user.id === connectedUserId);
        if (connectedUser) {
          annonce.user = { id: connectedUser.id } as IUser;
          console.log('Utilisateur connecté utilisé comme fallback:', connectedUser);
        } else if (this.usersSharedCollection.length > 0) {
          annonce.user = { id: this.usersSharedCollection[0].id } as IUser;
          console.log('Premier utilisateur utilisé comme fallback:', this.usersSharedCollection[0]);
        }
      }
    }

    if (formValue.commune?.id) {
      annonce.commune = { id: formValue.commune.id } as ICommune;
    }

    if (formValue.categorie?.id) {
      annonce.categorie = { id: formValue.categorie.id } as ICategorie;
    }

    if (formValue.activite?.id) {
      annonce.activite = { id: formValue.activite.id } as IActivite;
    }

    console.log('Annonce créée:');
    console.log(annonce);
    return annonce;
  }

  // MÉTHODES PROTECTED (doivent être après les méthodes publiques mais avant les privées)

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnnonce>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: error => this.onSaveError(error),
    });
  }

  protected onSaveSuccess(): void {
    console.log('Annonce sauvegardée avec succès !');
    this.previousState();
  }

  protected onSaveError(error?: any): void {
    console.error('Erreur lors de la sauvegarde:');
    console.error(error);
    if (error?.error?.message) {
      const errorMessage = 'Erreur: ' + String(error.error.message);
      alert(errorMessage);
    } else {
      alert("Erreur lors de la sauvegarde de l'annonce");
    }
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

  // MÉTHODES PRIVÉES (doivent être en dernier)

  private getServiceTypeFromForm(): string | undefined {
    const categorie = this.editForm.get('categorie')?.value as ICategorie | null;
    const activite = this.editForm.get('activite')?.value as IActivite | null;

    return categorie?.nomFr ?? activite?.nomFr ?? undefined;
  }
}
