import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IActivite, Activite } from '../activite.model';

import { ActiviteService } from './activite.service';

describe('Activite Service', () => {
  let service: ActiviteService;
  let httpMock: HttpTestingController;
  let elemDefault: IActivite;
  let expectedResult: IActivite | IActivite[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ActiviteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomFr: 'AAAAAAA',
      nomAr: 'AAAAAAA',
      categorieFr: 'AAAAAAA',
      categorieAr: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Activite', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Activite()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Activite', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomFr: 'BBBBBB',
          nomAr: 'BBBBBB',
          categorieFr: 'BBBBBB',
          categorieAr: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Activite', () => {
      const patchObject = Object.assign(
        {
          nomFr: 'BBBBBB',
          nomAr: 'BBBBBB',
          categorieFr: 'BBBBBB',
        },
        new Activite()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Activite', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomFr: 'BBBBBB',
          nomAr: 'BBBBBB',
          categorieFr: 'BBBBBB',
          categorieAr: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Activite', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addActiviteToCollectionIfMissing', () => {
      it('should add a Activite to an empty array', () => {
        const activite: IActivite = { id: 123 };
        expectedResult = service.addActiviteToCollectionIfMissing([], activite);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(activite);
      });

      it('should not add a Activite to an array that contains it', () => {
        const activite: IActivite = { id: 123 };
        const activiteCollection: IActivite[] = [
          {
            ...activite,
          },
          { id: 456 },
        ];
        expectedResult = service.addActiviteToCollectionIfMissing(activiteCollection, activite);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Activite to an array that doesn't contain it", () => {
        const activite: IActivite = { id: 123 };
        const activiteCollection: IActivite[] = [{ id: 456 }];
        expectedResult = service.addActiviteToCollectionIfMissing(activiteCollection, activite);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(activite);
      });

      it('should add only unique Activite to an array', () => {
        const activiteArray: IActivite[] = [{ id: 123 }, { id: 456 }, { id: 25072 }];
        const activiteCollection: IActivite[] = [{ id: 123 }];
        expectedResult = service.addActiviteToCollectionIfMissing(activiteCollection, ...activiteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const activite: IActivite = { id: 123 };
        const activite2: IActivite = { id: 456 };
        expectedResult = service.addActiviteToCollectionIfMissing([], activite, activite2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(activite);
        expect(expectedResult).toContain(activite2);
      });

      it('should accept null and undefined values', () => {
        const activite: IActivite = { id: 123 };
        expectedResult = service.addActiviteToCollectionIfMissing([], null, activite, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(activite);
      });

      it('should return initial array if no Activite is added', () => {
        const activiteCollection: IActivite[] = [{ id: 123 }];
        expectedResult = service.addActiviteToCollectionIfMissing(activiteCollection, undefined, null);
        expect(expectedResult).toEqual(activiteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
