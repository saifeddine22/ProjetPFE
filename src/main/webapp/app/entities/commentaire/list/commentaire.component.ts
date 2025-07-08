import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICommentaire } from '../commentaire.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { CommentaireService } from '../service/commentaire.service';
import { CommentaireDeleteDialogComponent } from '../delete/commentaire-delete-dialog.component';
import { IAnnonce } from 'app/entities/annonce/annonce.model';

@Component({
  selector: 'jhi-commentaire',
  templateUrl: './commentaire.component.html',
})
export class CommentaireComponent implements OnInit {
  commentaires?: ICommentaire[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  annonce: IAnnonce | undefined;

  constructor(
    protected commentaireService: CommentaireService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annonce }) => {
      this.annonce = annonce;
      console.log(annonce);
      this.handleNavigation();
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

    const query = this.annonce?.id
      ? this.commentaireService.findByAnnonceId(this.annonce.id, pageable)
      : this.commentaireService.query(pageable);

    query.subscribe({
      next: (res: HttpResponse<ICommentaire[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
      },
      error: () => {
        this.isLoading = false;
        this.onError();
      },
    });
  }

  trackId(_index: number, item: ICommentaire): number {
    return item.id!;
  }

  delete(commentaire: ICommentaire): void {
    const modalRef = this.modalService.open(CommentaireDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.commentaire = commentaire;
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

  protected onSuccess(data: ICommentaire[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    const route = this.annonce?.id ? ['/annonce', this.annonce.id, 'commentaires'] : ['/commentaire'];
    if (navigate) {
      this.router.navigate(route, {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.commentaires = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
