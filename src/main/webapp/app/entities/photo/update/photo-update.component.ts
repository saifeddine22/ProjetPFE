import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPhoto, Photo } from '../photo.model';
import { PhotoService } from '../service/photo.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { AnnonceService } from 'app/entities/annonce/service/annonce.service';

@Component({
  selector: 'jhi-photo-update',
  templateUrl: './photo-update.component.html',
})
export class PhotoUpdateComponent implements OnInit {
  isSaving = false;

  annoncesSharedCollection: IAnnonce[] = [];

  editForm = this.fb.group({
    id: [],
    url: [],
    libelle: [],
    image: [],
    imageContentType: [],
    annonce: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected photoService: PhotoService,
    protected annonceService: AnnonceService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected activeModal: NgbActiveModal,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    /*  this.activatedRoute.data.subscribe(({ photo }) => {
      this.updateForm(photo);

      this.loadRelationshipsOptions();
    }); */
    console.log('');
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('bricoviteApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    /* window.history.back(); */
    this.activeModal.dismiss();
    window.location.reload();
  }
  dismiss(): void {
    this.activeModal.dismiss();
  }

  save(): void {
    this.isSaving = true;
    const photo = this.createFromForm();
    /* if (photo.id !== undefined) {
      this.subscribeToSaveResponse(this.photoService.update(photo));
    } else { */
    this.subscribeToSaveResponse(this.photoService.create(photo));
    /* } */
  }

  trackAnnonceById(_index: number, item: IAnnonce): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPhoto>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(photo: IPhoto): void {
    this.editForm.patchValue({
      id: photo.id,
      url: photo.url,
      libelle: photo.libelle,
      image: photo.image,
      imageContentType: photo.imageContentType,
      annonce: photo.annonce,
    });

    this.annoncesSharedCollection = this.annonceService.addAnnonceToCollectionIfMissing(this.annoncesSharedCollection, photo.annonce);
  }

  protected loadRelationshipsOptions(): void {
    this.annonceService
      .query()
      .pipe(map((res: HttpResponse<IAnnonce[]>) => res.body ?? []))
      .pipe(
        map((annonces: IAnnonce[]) => this.annonceService.addAnnonceToCollectionIfMissing(annonces, this.editForm.get('annonce')!.value))
      )
      .subscribe((annonces: IAnnonce[]) => (this.annoncesSharedCollection = annonces));
  }

  protected createFromForm(): IPhoto {
    return {
      ...new Photo(),
      id: this.editForm.get(['id'])!.value,
      url: this.editForm.get(['url'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      imageContentType: this.editForm.get(['imageContentType'])!.value,
      image: this.editForm.get(['image'])!.value,
      annonce: { id: Number(sessionStorage.getItem('currentAnnonce')) },
    };
  }
}
