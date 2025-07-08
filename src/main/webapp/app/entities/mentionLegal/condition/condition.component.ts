import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-condition',
  templateUrl: './condition.component.html',
  styleUrls: ['./condition.component.scss'],
})
export class ConditionComponent implements OnInit {
  constructor() {
    console.log('condition-d-utilisation');
  }

  ngOnInit(): void {
    console.log('condition-d-utilisation');
  }
}
