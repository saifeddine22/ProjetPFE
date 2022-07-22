import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CommentaireService } from 'app/entities/commentaire/service/commentaire.service';
import { INote } from 'app/entities/note/note.model';
import { NoteService } from 'app/entities/note/service/note.service';
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
  note: INote | null = null;

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
  
  afficherCarte(): void{
    sessionStorage.setItem('dataAnnonce', JSON.stringify(this.annonce));
    this.annonceService.initilizeMap();
    this.annonceService.vectorMap();
  }

  
  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annonce }) => {
      this.annonce = annonce;
    });
    sessionStorage.setItem('currentAnnonce', String(this.annonce?.id));
  }

  previousState(): void {
    window.history.back();
  }

  /*  goToPersonneDetails(userId: number | undefined): void {
    if (userId) {
      this.personneService.findByUserId(userId).subscribe(res => {
        const personneId = res.body?.id;
        if (personneId) {
          this.router.navigate(['personne', personneId, 'view']);
        }
      });
    }
  } */

  goToPersonneDetails(userId: number | undefined): void {
    const modalRef = this.modalService.open(PersonneDetailComponent, { size: 'lg', backdrop: 'static' });
    if (userId) {
      this.personneService.findByUserId(userId).subscribe(res => {
        modalRef.componentInstance.personne = res.body;
      });
    }
  }
}
