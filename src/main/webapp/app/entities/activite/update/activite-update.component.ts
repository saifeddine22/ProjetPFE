import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IActivite, Activite } from '../activite.model';
import { ActiviteService } from '../service/activite.service';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';

@Component({
  selector: 'jhi-activite-update',
  templateUrl: './activite-update.component.html',
})
export class ActiviteUpdateComponent implements OnInit {
  isSaving = false;

  categoriesSharedCollection: ICategorie[] = [];

  editForm = this.fb.group({
    id: [],
    nomFr: [],
    nomAr: [],
    categorieFr: [],
    categorieAr: [],
    categorie: [null, Validators.required],
  });

  constructor(
    protected activiteService: ActiviteService,
    protected categorieService: CategorieService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ activite }) => {
      this.updateForm(activite);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const activite = this.createFromForm();
    if (activite.id !== undefined) {
      this.subscribeToSaveResponse(this.activiteService.update(activite));
    } else {
      this.subscribeToSaveResponse(this.activiteService.create(activite));
    }
  }

  trackCategorieById(_index: number, item: ICategorie): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IActivite>>): void {
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

  protected updateForm(activite: IActivite): void {
    this.editForm.patchValue({
      id: activite.id,
      nomFr: activite.nomFr,
      nomAr: activite.nomAr,
      categorieFr: activite.categorieFr,
      categorieAr: activite.categorieAr,
      categorie: activite.categorie,
    });

    this.categoriesSharedCollection = this.categorieService.addCategorieToCollectionIfMissing(
      this.categoriesSharedCollection,
      activite.categorie
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categorieService
      .query()
      .pipe(map((res: HttpResponse<ICategorie[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategorie[]) =>
          this.categorieService.addCategorieToCollectionIfMissing(categories, this.editForm.get('categorie')!.value)
        )
      )
      .subscribe((categories: ICategorie[]) => (this.categoriesSharedCollection = categories));
  }

  protected createFromForm(): IActivite {
    return {
      ...new Activite(),
      id: this.editForm.get(['id'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      nomAr: this.editForm.get(['nomAr'])!.value,
      categorieFr: this.editForm.get(['categorieFr'])!.value,
      categorieAr: this.editForm.get(['categorieAr'])!.value,
      categorie: this.editForm.get(['categorie'])!.value,
    };
  }
}
