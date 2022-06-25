import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnnonce } from '../annonce.model';

@Component({
  selector: 'jhi-annonce-detail',
  templateUrl: './annonce-detail.component.html',
})
export class AnnonceDetailComponent implements OnInit {
  annonce: IAnnonce | null = null;
  userConnect = Number(sessionStorage.getItem("userConnectedId"));

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annonce }) => {
      this.annonce = annonce;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
