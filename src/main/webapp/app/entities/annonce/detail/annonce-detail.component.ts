import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ICommentaire } from 'app/entities/commentaire/commentaire.model';
import { CommentaireService } from 'app/entities/commentaire/service/commentaire.service';
import { CommentaireUpdateComponent } from 'app/entities/commentaire/update/commentaire-update.component';
import { INote } from 'app/entities/note/note.model';
import { NoteService } from 'app/entities/note/service/note.service';
import { NoteUpdateComponent } from 'app/entities/note/update/note-update.component';
import { PersonneDetailComponent } from 'app/entities/personne/detail/personne-detail.component';
import { PersonneService } from 'app/entities/personne/service/personne.service';
import { PhotoService } from 'app/entities/photo/service/photo.service';

import { IAnnonce } from '../annonce.model';
import { AnnonceService } from '../service/annonce.service';
import { AIService, ServiceDetailsDTO } from '../ai.service';
import { DATE_TIME_FORMAT } from '../../../config/input.constants';
import { FormBuilder, Validators } from '@angular/forms';
import { ICategorie } from '../../categorie/categorie.model';
import { IActivite } from '../../activite/activite.model';

@Component({
  selector: 'jhi-annonce-detail',
  templateUrl: './annonce-detail.component.html',
})
export class AnnonceDetailComponent implements OnInit {
  annonce: IAnnonce | null = null;
  userConnect = Number(sessionStorage.getItem('userConnectedId'));
  note!: INote;
  commentaire!: ICommentaire;
  isSaving = false;
  isLoadingAISuggestions = false;

  // Variables pour les suggestions IA
  titleSuggestions: { title: string } | null = null;
  aiSuggestions: { enhancedDescription: string } | null = null;
  keywordSuggestions: string[] = [];
  priceSuggestions: { minPrice: number; maxPrice: number; currency: string } | null = null;

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
    public annonceService: AnnonceService,
    protected activatedRoute: ActivatedRoute,
    protected personneService: PersonneService,
    protected commentaireService: CommentaireService,
    protected photoService: PhotoService,
    protected noteService: NoteService,
    protected modalService: NgbModal,
    private router: Router,
    protected aiService: AIService,
    protected fb: FormBuilder
  ) {}

  // MÉTHODES PUBLIQUES (en premier)

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annonce }) => {
      this.annonce = annonce;
    });
    if (this.annonce?.id) {
      sessionStorage.setItem('currentAnnonce', String(this.annonce.id));
    }
  }

  afficherCarte(): void {
    const mapElement = document.getElementById('map');
    if (mapElement) {
      mapElement.innerHTML = '';
      mapElement.style.height = '399px';

      if (this.annonce) {
        sessionStorage.setItem('dataAnnonce', JSON.stringify(this.annonce));

        try {
          this.annonceService.initilizeMap();
          this.annonceService.vectorMap();
          this.annonceService.map.updateSize();
          this.annonceService.map.render();
          this.annonceService.map.removeInteraction(this.annonceService.draw);
        } catch (error) {
          console.error("Erreur lors de l'initialisation de la carte:", error);
        }
      }
    }
  }

  previousState(): void {
    window.history.back();
  }

  goToPersonneDetails(userId: number | undefined): void {
    const modalRef = this.modalService.open(PersonneDetailComponent, { size: 'lg', backdrop: 'static' });
    if (userId) {
      this.personneService.findByUserId(userId).subscribe(res => {
        modalRef.componentInstance.personne = res.body;
      });
    }
  }

  addRating(note: INote | undefined): void {
    this.modalService.open(NoteUpdateComponent, { size: 'lg', backdrop: 'static' });
  }

  addComment(commentaire: ICommentaire | undefined): void {
    this.modalService.open(CommentaireUpdateComponent, { size: 'lg', backdrop: 'static' });
  }

  // Méthode pour suggérer un titre en fonction de la description
  suggestTitle(): void {
    const description = this.editForm.get('description')?.value;

    if (!description) {
      alert("Veuillez d'abord saisir une description");
      return;
    }

    this.isLoadingAISuggestions = true;

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

    this.aiService.enhanceDescription(description).subscribe({
      next: (response: { enhancedDescription: string }) => {
        this.aiSuggestions = response;
        this.isLoadingAISuggestions = false;
      },
      error: (error: Error) => {
        console.error("Erreur lors de l'amélioration de la description:", error);
        this.isLoadingAISuggestions = false;
      },
    });
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

    this.aiService.suggestKeywords({ description, serviceType }).subscribe({
      next: (result: string[]) => {
        this.keywordSuggestions = result;
        this.isLoadingAISuggestions = false;
      },
      error: (error: Error) => {
        console.error('Erreur lors de la génération des mots-clés:', error);
        this.isLoadingAISuggestions = false;
      },
    });
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

    this.aiService.estimatePrice(serviceDetails).subscribe({
      next: (response: { minPrice: number; maxPrice: number; currency: string }) => {
        this.priceSuggestions = response;
        this.isLoadingAISuggestions = false;
      },
      error: (error: Error) => {
        console.error("Erreur lors de l'estimation du prix:", error);
        this.isLoadingAISuggestions = false;
      },
    });
  }

  // MÉTHODES PROTECTED (après les publiques)

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
  }

  // MÉTHODES PRIVÉES (en dernier)

  private getServiceTypeFromForm(): string | undefined {
    const categorie = this.editForm.get('categorie')?.value as ICategorie | null;
    const activite = this.editForm.get('activite')?.value as IActivite | null;

    return categorie?.nomFr ?? activite?.nomFr ?? undefined;
  }
}
