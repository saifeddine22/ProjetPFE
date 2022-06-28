import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommentaireService } from 'app/entities/commentaire/service/commentaire.service';
import { PersonneService } from 'app/entities/personne/service/personne.service';

import { IAnnonce } from '../annonce.model';

@Component({
  selector: 'jhi-annonce-detail',
  templateUrl: './annonce-detail.component.html',
})
export class AnnonceDetailComponent implements OnInit {
  annonce: IAnnonce | null = null;
  userConnect = Number(sessionStorage.getItem('userConnectedId'));

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected personneService: PersonneService,
    protected commentaireService: CommentaireService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annonce }) => {
      this.annonce = annonce;
    });
  }

  previousState(): void {
    window.history.back();
  }

  goToPersonneDetails(userId: number | undefined): void {
    if (userId) {
      this.personneService.findByUserId(userId).subscribe(res => {
        const personneId = res.body?.id;
        if (personneId) {
          this.router.navigate(['personne', personneId, 'view']);
        }
      });
    }
  }
}
