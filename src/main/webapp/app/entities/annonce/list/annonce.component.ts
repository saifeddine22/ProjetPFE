import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, map } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAnnonce } from '../annonce.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { AnnonceService } from '../service/annonce.service';
import { AnnonceDeleteDialogComponent } from '../delete/annonce-delete-dialog.component';
import { FormBuilder } from '@angular/forms';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { IActivite } from 'app/entities/activite/activite.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';
import { ActiviteService } from 'app/entities/activite/service/activite.service';
import { AnnonceDetailComponent } from '../detail/annonce-detail.component';
import { ProvinceService } from 'app/entities/province/service/province.service';
import { IProvince } from 'app/entities/province/province.model';

@Component({
  selector: 'jhi-annonce',
  templateUrl: './annonce.component.html',
})
export class AnnonceComponent implements OnInit {
  annonces?: IAnnonce[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  userConnect = Number(sessionStorage.getItem('userConnectedId'));
  annonce: IAnnonce | undefined;
  categoriesSharedCollection: ICategorie[] = [];
  activitesSharedCollection: IActivite[] = [];
  provincesSharedCollection: IProvince[] = [];
  annonceDetailComponenet?: AnnonceDetailComponent;

  isMesAnnonces = false;
  starRating = 0;
  txtRateValue = 0;

  editForm = this.fb.group({
    id: [],
    categorie: [],
    activite: [],
    province: [],
  });

  constructor(
    public annonceService: AnnonceService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected categorieService: CategorieService,
    protected activiteService: ActiviteService,
    protected provinceService: ProvinceService,
    protected fb: FormBuilder
  ) {}

  onChekCategorie(): number {
    const catId = this.editForm.get(['categorie'])!.value;
    return Number(catId);
  }
  supprimerClass(): void {
    document.getElementById('collapseExample')?.classList.remove('show');
  }

  filterActivites(): IActivite[] {
    const catId = this.editForm.get('categorie')?.value;
    if (catId) {
      return this.activitesSharedCollection.filter(a => a.categorie?.id === catId);
    } else {
      return this.activitesSharedCollection;
    }
  }

  search(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;
    const activiteId = this.editForm.get('activite')?.value ?? -1;
    const provinceId = this.editForm.get('province')?.value ?? -1;
    const categorieId = this.editForm.get('categorie')?.value ?? -1;

    this.annonceService
      .search(activiteId, provinceId, categorieId, {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IAnnonce[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          this.supprimerClass();
        },
        error: () => {
          this.isLoading = false;
          this.onError();
        },
      });
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;
    const pageable = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };

    const query = this.isMesAnnonces ? this.annonceService.findByConnectedUser(pageable) : this.annonceService.query(pageable);

    /* this.annonceService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      }) */

    query.subscribe({
      next: (res: HttpResponse<IAnnonce[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
      },
      error: () => {
        this.isLoading = false;
        this.onError();
      },
    });
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(data => {
      this.isMesAnnonces = data.mesAnnonces;
      this.handleNavigation();
      this.updateForm(data);
      this.loadRelationshipsOptions();
      this.annonceService.initilizeMap();
      this.annonceService.map.removeInteraction(this.annonceService.draw);
      this.annonceService.map.addInteraction(this.annonceService.drawCircle);
      this.annonceService.drawCircle.on('drawend', event => {
        const ext = event.feature.getGeometry()?.getExtent();
        this.annonceService.map.getView().fit(ext!);
      });
      this.annonceService.map.on('pointermove', () => {
        this.annonceService.map.addInteraction(this.annonceService.drawCircle);
      });
    });
    sessionStorage.removeItem('currentAnnonce');
  }

  trackId(_index: number, item: IAnnonce): number {
    return item.id!;
  }
  trackCategorieById(_index: number, item: ICategorie): number {
    return item.id!;
  }
  trackActiviteById(_index: number, item: IActivite): number {
    return item.id!;
  }
  trackProvinceById(_index: number, item: IProvince): number {
    return item.id!;
  }

  delete(annonce: IAnnonce): void {
    const modalRef = this.modalService.open(AnnonceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.annonce = annonce;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = +(page ?? 1);
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IAnnonce[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    const route = this.isMesAnnonces ? ['/annonce', 'mesAnnonces'] : ['/annonce'];
    if (navigate) {
      this.router.navigate(route, {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.annonces = data ?? [];
    this.ngbPaginationPage = this.page;
    sessionStorage.setItem('dataAnnonce', JSON.stringify(this.annonces));
    this.annonceService.vectorMap();
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  protected updateForm(annonce: IAnnonce): void {
    this.editForm.patchValue({
      id: annonce.id,
      categorie: annonce.categorie,
      activite: annonce.activite,
      province: annonce.province,
    });

    this.categoriesSharedCollection = this.categorieService.addCategorieToCollectionIfMissing(
      this.categoriesSharedCollection,
      annonce.categorie
    );
    this.activitesSharedCollection = this.activiteService.addActiviteToCollectionIfMissing(
      this.activitesSharedCollection,
      annonce.activite
    );
    this.provincesSharedCollection = this.provinceService.addProvinceToCollectionIfMissing(
      this.provincesSharedCollection,
      annonce.province
    );
  }

  protected loadRelationshipsOptions(): void {
    this.categorieService
      .query({ size: 25 })
      .pipe(map((res: HttpResponse<ICategorie[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategorie[]) =>
          this.categorieService.addCategorieToCollectionIfMissing(categories, this.editForm.get('categorie')!.value)
        )
      )
      .subscribe((categories: ICategorie[]) => (this.categoriesSharedCollection = categories));

    this.activiteService
      .query({ size: 200 })
      .pipe(map((res: HttpResponse<IActivite[]>) => res.body ?? []))
      .pipe(
        map((activites: IActivite[]) =>
          this.activiteService.addActiviteToCollectionIfMissing(activites, this.editForm.get('activite')!.value)
        )
      )
      .subscribe((activites: IActivite[]) => (this.activitesSharedCollection = activites));

    this.provinceService
      .query({ size: 200 })
      .pipe(map((res: HttpResponse<IProvince[]>) => res.body ?? []))
      .pipe(
        map((provinces: IProvince[]) =>
          this.provinceService.addProvinceToCollectionIfMissing(provinces, this.editForm.get('province')!.value)
        )
      )
      .subscribe((provinces: IProvince[]) => (this.provincesSharedCollection = provinces));
  }
}
