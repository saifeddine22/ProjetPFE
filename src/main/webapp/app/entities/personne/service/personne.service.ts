import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonne, getPersonneIdentifier } from '../personne.model';

export type EntityResponseType = HttpResponse<IPersonne>;
export type EntityArrayResponseType = HttpResponse<IPersonne[]>;

@Injectable({ providedIn: 'root' })
export class PersonneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/personnes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(personne: IPersonne): Observable<EntityResponseType> {
    return this.http.post<IPersonne>(this.resourceUrl, personne, { observe: 'response' });
  }

  update(personne: IPersonne): Observable<EntityResponseType> {
    return this.http.put<IPersonne>(`${this.resourceUrl}/${getPersonneIdentifier(personne) as number}`, personne, { observe: 'response' });
  }

  partialUpdate(personne: IPersonne): Observable<EntityResponseType> {
    return this.http.patch<IPersonne>(`${this.resourceUrl}/${getPersonneIdentifier(personne) as number}`, personne, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonne>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByUserId(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonne>(`${this.resourceUrl}/user/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonne[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPersonneToCollectionIfMissing(personneCollection: IPersonne[], ...personnesToCheck: (IPersonne | null | undefined)[]): IPersonne[] {
    const personnes: IPersonne[] = personnesToCheck.filter(isPresent);
    if (personnes.length > 0) {
      const personneCollectionIdentifiers = personneCollection.map(personneItem => getPersonneIdentifier(personneItem)!);
      const personnesToAdd = personnes.filter(personneItem => {
        const personneIdentifier = getPersonneIdentifier(personneItem);
        if (personneIdentifier == null || personneCollectionIdentifiers.includes(personneIdentifier)) {
          return false;
        }
        personneCollectionIdentifiers.push(personneIdentifier);
        return true;
      });
      return [...personnesToAdd, ...personneCollection];
    }
    return personneCollection;
  }
}
