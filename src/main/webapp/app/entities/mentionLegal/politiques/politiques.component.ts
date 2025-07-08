import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-politiques',
  templateUrl: './politiques.component.html',
  styleUrls: ['./politiques.component.scss'],
})
export class PolitiquesComponent implements OnInit {
  constructor() {
    console.log('politique-de-confidentialite1');
  }

  ngOnInit(): void {
    console.log('politique-de-confidentialite');
  }
}
