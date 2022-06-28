import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICommentaire, getCommentaireIdentifier } from '../commentaire.model';

export type EntityResponseType = HttpResponse<ICommentaire>;
export type EntityArrayResponseType = HttpResponse<ICommentaire[]>;

@Injectable({ providedIn: 'root' })
export class CommentaireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/commentaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(commentaire: ICommentaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(commentaire);
    return this.http
      .post<ICommentaire>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(commentaire: ICommentaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(commentaire);
    return this.http
      .put<ICommentaire>(`${this.resourceUrl}/${getCommentaireIdentifier(commentaire) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(commentaire: ICommentaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(commentaire);
    return this.http
      .patch<ICommentaire>(`${this.resourceUrl}/${getCommentaireIdentifier(commentaire) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICommentaire>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findByAnnonceId(id: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICommentaire[]>(`${this.resourceUrl}/annonce/${id}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICommentaire[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCommentaireToCollectionIfMissing(
    commentaireCollection: ICommentaire[],
    ...commentairesToCheck: (ICommentaire | null | undefined)[]
  ): ICommentaire[] {
    const commentaires: ICommentaire[] = commentairesToCheck.filter(isPresent);
    if (commentaires.length > 0) {
      const commentaireCollectionIdentifiers = commentaireCollection.map(commentaireItem => getCommentaireIdentifier(commentaireItem)!);
      const commentairesToAdd = commentaires.filter(commentaireItem => {
        const commentaireIdentifier = getCommentaireIdentifier(commentaireItem);
        if (commentaireIdentifier == null || commentaireCollectionIdentifiers.includes(commentaireIdentifier)) {
          return false;
        }
        commentaireCollectionIdentifiers.push(commentaireIdentifier);
        return true;
      });
      return [...commentairesToAdd, ...commentaireCollection];
    }
    return commentaireCollection;
  }

  protected convertDateFromClient(commentaire: ICommentaire): ICommentaire {
    return Object.assign({}, commentaire, {
      dateCommentaire: commentaire.dateCommentaire?.isValid() ? commentaire.dateCommentaire.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCommentaire = res.body.dateCommentaire ? dayjs(res.body.dateCommentaire) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((commentaire: ICommentaire) => {
        commentaire.dateCommentaire = commentaire.dateCommentaire ? dayjs(commentaire.dateCommentaire) : undefined;
      });
    }
    return res;
  }
}
