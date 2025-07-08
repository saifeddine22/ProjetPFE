import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICommune, Commune } from '../commune.model';
import { CommuneService } from '../service/commune.service';
import { IProvince } from 'app/entities/province/province.model';
import { ProvinceService } from 'app/entities/province/service/province.service';

@Component({
  selector: 'jhi-commune-update',
  templateUrl: './commune-update.component.html',
})
export class CommuneUpdateComponent implements OnInit {
  isSaving = false;

  provincesSharedCollection: IProvince[] = [];

  editForm = this.fb.group({
    id: [],
    codeReg: [],
    codeProv: [],
    provinceFr: [],
    provinceAr: [],
    regionFr: [],
    regionAr: [],
    cercleFr: [],
    codeCercle: [],
    comFr: [],
    codeCom: [],
    centreFr: [],
    codAc: [],
    comAr: [],
    cc: [],
    centreAr: [],
    nomAr: [],
    nomFr: [],
    geometry: [],
    province: [null, Validators.required],
  });

  constructor(
    protected communeService: CommuneService,
    protected provinceService: ProvinceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commune }) => {
      this.updateForm(commune);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commune = this.createFromForm();
    if (commune.id !== undefined) {
      this.subscribeToSaveResponse(this.communeService.update(commune));
    } else {
      this.subscribeToSaveResponse(this.communeService.create(commune));
    }
  }

  trackProvinceById(_index: number, item: IProvince): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommune>>): void {
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

  protected updateForm(commune: ICommune): void {
    this.editForm.patchValue({
      id: commune.id,
      codeReg: commune.codeReg,
      codeProv: commune.codeProv,
      provinceFr: commune.provinceFr,
      provinceAr: commune.provinceAr,
      regionFr: commune.regionFr,
      regionAr: commune.regionAr,
      cercleFr: commune.cercleFr,
      codeCercle: commune.codeCercle,
      comFr: commune.comFr,
      codeCom: commune.codeCom,
      centreFr: commune.centreFr,
      codAc: commune.codAc,
      comAr: commune.comAr,
      cc: commune.cc,
      centreAr: commune.centreAr,
      nomAr: commune.nomAr,
      nomFr: commune.nomFr,
      geometry: commune.geometry,
      province: commune.province,
    });

    this.provincesSharedCollection = this.provinceService.addProvinceToCollectionIfMissing(
      this.provincesSharedCollection,
      commune.province
    );
  }

  protected loadRelationshipsOptions(): void {
    this.provinceService
      .query()
      .pipe(map((res: HttpResponse<IProvince[]>) => res.body ?? []))
      .pipe(
        map((provinces: IProvince[]) =>
          this.provinceService.addProvinceToCollectionIfMissing(provinces, this.editForm.get('province')!.value)
        )
      )
      .subscribe((provinces: IProvince[]) => (this.provincesSharedCollection = provinces));
  }

  protected createFromForm(): ICommune {
    return {
      ...new Commune(),
      id: this.editForm.get(['id'])!.value,
      codeReg: this.editForm.get(['codeReg'])!.value,
      codeProv: this.editForm.get(['codeProv'])!.value,
      provinceFr: this.editForm.get(['provinceFr'])!.value,
      provinceAr: this.editForm.get(['provinceAr'])!.value,
      regionFr: this.editForm.get(['regionFr'])!.value,
      regionAr: this.editForm.get(['regionAr'])!.value,
      cercleFr: this.editForm.get(['cercleFr'])!.value,
      codeCercle: this.editForm.get(['codeCercle'])!.value,
      comFr: this.editForm.get(['comFr'])!.value,
      codeCom: this.editForm.get(['codeCom'])!.value,
      centreFr: this.editForm.get(['centreFr'])!.value,
      codAc: this.editForm.get(['codAc'])!.value,
      comAr: this.editForm.get(['comAr'])!.value,
      cc: this.editForm.get(['cc'])!.value,
      centreAr: this.editForm.get(['centreAr'])!.value,
      nomAr: this.editForm.get(['nomAr'])!.value,
      nomFr: this.editForm.get(['nomFr'])!.value,
      geometry: this.editForm.get(['geometry'])!.value,
      province: this.editForm.get(['province'])!.value,
    };
  }
}
