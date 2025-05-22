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

@Component({
  selector: 'jhi-annonce-detail',
  templateUrl: './annonce-detail.component.html',
})
export class AnnonceDetailComponent implements OnInit {
  annonce: IAnnonce | null = null;
  userConnect = Number(sessionStorage.getItem('userConnectedId'));
  note!: INote;
  commentaire!: ICommentaire;

  constructor(
    public annonceService: AnnonceService,
    protected activatedRoute: ActivatedRoute,
    protected personneService: PersonneService,
    protected commentaireService: CommentaireService,
    protected photoService: PhotoService,
    protected noteService: NoteService,
    protected modalService: NgbModal,
    private router: Router
  ) {}

  afficherCarte(): void {
    // Vider la carte existante
    const mapElement = document.getElementById('map');
    if (mapElement) {
      mapElement.innerHTML = '';
      mapElement.style.height = '399px';

      // Stocker les données d'annonce - vérifie maintenant si l'annonce existe
      if (this.annonce) {
        sessionStorage.setItem('dataAnnonce', JSON.stringify(this.annonce));

        try {
          // Initialiser la carte avec un délai court pour s'assurer que le DOM est prêt
          setTimeout(() => {
            this.annonceService.initilizeMap();

            // Ajouter un gestionnaire d'erreurs pour le chargement des vectorMap
            try {
              this.annonceService.vectorMap();
            } catch (err) {
              console.warn('Erreur lors du chargement des vecteurs de carte:', err);
            }

            // Mettre à jour la taille de la carte
            this.annonceService.map.updateSize();
            this.annonceService.map.render();

            // Retirer l'interaction de dessin - corrigé la condition
            // Modification ici - Suppression de la vérification inutile
            this.annonceService.map.removeInteraction(this.annonceService.draw);
          }, 300);
        } catch (err) {
          console.error("Erreur lors de l'initialisation de la carte:", err);
        }
      }
    }
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annonce }) => {
      this.annonce = annonce;
    });
    if (this.annonce?.id) {
      sessionStorage.setItem('currentAnnonce', String(this.annonce.id));
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
}
