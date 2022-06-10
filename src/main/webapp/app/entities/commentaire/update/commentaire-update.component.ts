import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICommentaire, Commentaire } from '../commentaire.model';
import { CommentaireService } from '../service/commentaire.service';
import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { AnnonceService } from 'app/entities/annonce/service/annonce.service';

@Component({
  selector: 'jhi-commentaire-update',
  templateUrl: './commentaire-update.component.html',
})
export class CommentaireUpdateComponent implements OnInit {
  isSaving = false;

  annoncesSharedCollection: IAnnonce[] = [];

  editForm = this.fb.group({
    id: [],
    details: [],
    dateCommentaire: [],
    annonce: [],
  });

  constructor(
    protected commentaireService: CommentaireService,
    protected annonceService: AnnonceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commentaire }) => {
      if (commentaire.id === undefined) {
        const today = dayjs().startOf('day');
        commentaire.dateCommentaire = today;
      }

      this.updateForm(commentaire);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commentaire = this.createFromForm();
    if (commentaire.id !== undefined) {
      this.subscribeToSaveResponse(this.commentaireService.update(commentaire));
    } else {
      this.subscribeToSaveResponse(this.commentaireService.create(commentaire));
    }
  }

  trackAnnonceById(_index: number, item: IAnnonce): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommentaire>>): void {
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

  protected updateForm(commentaire: ICommentaire): void {
    this.editForm.patchValue({
      id: commentaire.id,
      details: commentaire.details,
      dateCommentaire: commentaire.dateCommentaire ? commentaire.dateCommentaire.format(DATE_TIME_FORMAT) : null,
      annonce: commentaire.annonce,
    });

    this.annoncesSharedCollection = this.annonceService.addAnnonceToCollectionIfMissing(this.annoncesSharedCollection, commentaire.annonce);
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

  protected createFromForm(): ICommentaire {
    return {
      ...new Commentaire(),
      id: this.editForm.get(['id'])!.value,
      details: this.editForm.get(['details'])!.value,
      dateCommentaire: this.editForm.get(['dateCommentaire'])!.value
        ? dayjs(this.editForm.get(['dateCommentaire'])!.value, DATE_TIME_FORMAT)
        : undefined,
      annonce: this.editForm.get(['annonce'])!.value,
    };
  }
}
