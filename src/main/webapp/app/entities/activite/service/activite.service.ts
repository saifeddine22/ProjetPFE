import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IActivite, getActiviteIdentifier } from '../activite.model';

export type EntityResponseType = HttpResponse<IActivite>;
export type EntityArrayResponseType = HttpResponse<IActivite[]>;

@Injectable({ providedIn: 'root' })
export class ActiviteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/activites');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(activite: IActivite): Observable<EntityResponseType> {
    return this.http.post<IActivite>(this.resourceUrl, activite, { observe: 'response' });
  }

  update(activite: IActivite): Observable<EntityResponseType> {
    return this.http.put<IActivite>(`${this.resourceUrl}/${getActiviteIdentifier(activite) as number}`, activite, { observe: 'response' });
  }

  partialUpdate(activite: IActivite): Observable<EntityResponseType> {
    return this.http.patch<IActivite>(`${this.resourceUrl}/${getActiviteIdentifier(activite) as number}`, activite, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IActivite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IActivite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addActiviteToCollectionIfMissing(activiteCollection: IActivite[], ...activitesToCheck: (IActivite | null | undefined)[]): IActivite[] {
    const activites: IActivite[] = activitesToCheck.filter(isPresent);
    if (activites.length > 0) {
      const activiteCollectionIdentifiers = activiteCollection.map(activiteItem => getActiviteIdentifier(activiteItem)!);
      const activitesToAdd = activites.filter(activiteItem => {
        const activiteIdentifier = getActiviteIdentifier(activiteItem);
        if (activiteIdentifier == null || activiteCollectionIdentifiers.includes(activiteIdentifier)) {
          return false;
        }
        activiteCollectionIdentifiers.push(activiteIdentifier);
        return true;
      });
      return [...activitesToAdd, ...activiteCollection];
    }
    return activiteCollection;
  }
}
