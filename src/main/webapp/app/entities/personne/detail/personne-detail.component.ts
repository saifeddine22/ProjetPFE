import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonne } from '../personne.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-personne-detail',
  templateUrl: './personne-detail.component.html',
})
export class PersonneDetailComponent implements OnInit {
  personne: IPersonne | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected activeModal: NgbActiveModal) {}

  ngOnInit(): void {
    /* this.activatedRoute.data.subscribe(({ personne }) => {
      this.personne = personne;
    });*/
    console.log('');
  }

  previousState(): void {
    this.activeModal.dismiss();
  }
  /*   previousState(): void {
    window.history.back();
  } */
}
