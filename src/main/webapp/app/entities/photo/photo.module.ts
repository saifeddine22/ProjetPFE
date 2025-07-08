import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PhotoComponent } from './list/photo.component';
import { PhotoDetailComponent } from './detail/photo-detail.component';
import { PhotoUpdateComponent } from './update/photo-update.component';
import { PhotoDeleteDialogComponent } from './delete/photo-delete-dialog.component';
import { PhotoRoutingModule } from './route/photo-routing.module';
import { PhotoModalComponent } from './listModal/photo-modal.component';

@NgModule({
  imports: [SharedModule, PhotoRoutingModule],
  declarations: [PhotoComponent, PhotoDetailComponent, PhotoUpdateComponent, PhotoDeleteDialogComponent, PhotoModalComponent],
  entryComponents: [PhotoDeleteDialogComponent],
})
export class PhotoModule {}
