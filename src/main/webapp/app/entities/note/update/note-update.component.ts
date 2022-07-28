import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INote, Note } from '../note.model';
import { NoteService } from '../service/note.service';
import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { AnnonceService } from 'app/entities/annonce/service/annonce.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-note-update',
  templateUrl: './note-update.component.html',
})
export class NoteUpdateComponent implements OnInit {
  isSaving = false;

  note: INote | null = null;

  annoncesSharedCollection: IAnnonce[] = [];

  starRating = 0;

  editForm = this.fb.group({
    id: [],
    valeur: [],
    annonce: [],
  });

  constructor(
    protected noteService: NoteService,
    protected annonceService: AnnonceService,
    protected activatedRoute: ActivatedRoute,
    protected activeModal: NgbActiveModal,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    /* this.activatedRoute.data.subscribe(({ note }) => {
      this.updateForm(note);
      this.loadRelationshipsOptions();
    }); */
    console.log('');
  }

  previousState(): void {
    window.location.reload();
    this.activeModal.dismiss();
  }

  dismiss(): void {
    this.activeModal.dismiss();
  }

  save(): void {
    this.isSaving = true;
    const note = this.createFromForm();
    console.log(note);
    // if (note.id !== undefined) {
    // this.subscribeToSaveResponse(this.noteService.update(note));
    // } else {
    this.subscribeToSaveResponse(this.noteService.create(note));
    // }
  }

  trackAnnonceById(_index: number, item: IAnnonce): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INote>>): void {
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

  protected updateForm(note: INote): void {
    this.editForm.patchValue({
      id: note.id,
      valeur: note.valeur,
      annonce: note.annonce,
    });

    this.annoncesSharedCollection = this.annonceService.addAnnonceToCollectionIfMissing(this.annoncesSharedCollection, note.annonce);
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

  protected createFromForm(): INote {
    return {
      ...new Note(),
      id: this.editForm.get(['id'])!.value,
      valeur: this.editForm.get(['valeur'])!.value,
      annonce: { id: Number(sessionStorage.getItem('currentAnnonce')) },
    };
  }
}
