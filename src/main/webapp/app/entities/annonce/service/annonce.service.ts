import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnnonce, getAnnonceIdentifier } from '../annonce.model';

export type EntityResponseType = HttpResponse<IAnnonce>;
export type EntityArrayResponseType = HttpResponse<IAnnonce[]>;

@Injectable({ providedIn: 'root' })
export class AnnonceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/annonces');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(annonce: IAnnonce): Observable<EntityResponseType> {
    return this.http.post<IAnnonce>(this.resourceUrl, annonce, { observe: 'response' });
  }

  update(annonce: IAnnonce): Observable<EntityResponseType> {
    return this.http.put<IAnnonce>(`${this.resourceUrl}/${getAnnonceIdentifier(annonce) as number}`, annonce, { observe: 'response' });
  }

  partialUpdate(annonce: IAnnonce): Observable<EntityResponseType> {
    return this.http.patch<IAnnonce>(`${this.resourceUrl}/${getAnnonceIdentifier(annonce) as number}`, annonce, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAnnonce>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAnnonce[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAnnonceToCollectionIfMissing(annonceCollection: IAnnonce[], ...annoncesToCheck: (IAnnonce | null | undefined)[]): IAnnonce[] {
    const annonces: IAnnonce[] = annoncesToCheck.filter(isPresent);
    if (annonces.length > 0) {
      const annonceCollectionIdentifiers = annonceCollection.map(annonceItem => getAnnonceIdentifier(annonceItem)!);
      const annoncesToAdd = annonces.filter(annonceItem => {
        const annonceIdentifier = getAnnonceIdentifier(annonceItem);
        if (annonceIdentifier == null || annonceCollectionIdentifiers.includes(annonceIdentifier)) {
          return false;
        }
        annonceCollectionIdentifiers.push(annonceIdentifier);
        return true;
      });
      return [...annoncesToAdd, ...annonceCollection];
    }
    return annonceCollection;
  }
}
