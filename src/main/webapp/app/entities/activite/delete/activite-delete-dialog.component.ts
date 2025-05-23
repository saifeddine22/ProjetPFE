import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IActivite } from '../activite.model';
import { ActiviteService } from '../service/activite.service';

@Component({
  templateUrl: './activite-delete-dialog.component.html',
})
export class ActiviteDeleteDialogComponent {
  activite?: IActivite;

  constructor(protected activiteService: ActiviteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.activiteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
