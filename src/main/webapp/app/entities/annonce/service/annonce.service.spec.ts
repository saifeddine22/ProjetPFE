import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAnnonce, Annonce } from '../annonce.model';

import { AnnonceService } from './annonce.service';

describe('Annonce Service', () => {
  let service: AnnonceService;
  let httpMock: HttpTestingController;
  let elemDefault: IAnnonce;
  let expectedResult: IAnnonce | IAnnonce[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AnnonceService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      titre: 'AAAAAAA',
      description: 'AAAAAAA',
      adresse: 'AAAAAAA',
      status: false,
      dateAnnonce: currentDate,
      latitude: 0,
      longitude: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateAnnonce: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Annonce', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateAnnonce: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateAnnonce: currentDate,
        },
        returnedFromService
      );

      service.create(new Annonce()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Annonce', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          titre: 'BBBBBB',
          description: 'BBBBBB',
          adresse: 'BBBBBB',
          status: true,
          dateAnnonce: currentDate.format(DATE_TIME_FORMAT),
          latitude: 1,
          longitude: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateAnnonce: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Annonce', () => {
      const patchObject = Object.assign(
        {
          description: 'BBBBBB',
          status: true,
          dateAnnonce: currentDate.format(DATE_TIME_FORMAT),
        },
        new Annonce()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateAnnonce: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Annonce', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          titre: 'BBBBBB',
          description: 'BBBBBB',
          adresse: 'BBBBBB',
          status: true,
          dateAnnonce: currentDate.format(DATE_TIME_FORMAT),
          latitude: 1,
          longitude: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateAnnonce: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Annonce', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAnnonceToCollectionIfMissing', () => {
      it('should add a Annonce to an empty array', () => {
        const annonce: IAnnonce = { id: 123 };
        expectedResult = service.addAnnonceToCollectionIfMissing([], annonce);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(annonce);
      });

      it('should not add a Annonce to an array that contains it', () => {
        const annonce: IAnnonce = { id: 123 };
        const annonceCollection: IAnnonce[] = [
          {
            ...annonce,
          },
          { id: 456 },
        ];
        expectedResult = service.addAnnonceToCollectionIfMissing(annonceCollection, annonce);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Annonce to an array that doesn't contain it", () => {
        const annonce: IAnnonce = { id: 123 };
        const annonceCollection: IAnnonce[] = [{ id: 456 }];
        expectedResult = service.addAnnonceToCollectionIfMissing(annonceCollection, annonce);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(annonce);
      });

      it('should add only unique Annonce to an array', () => {
        const annonceArray: IAnnonce[] = [{ id: 123 }, { id: 456 }, { id: 69740 }];
        const annonceCollection: IAnnonce[] = [{ id: 123 }];
        expectedResult = service.addAnnonceToCollectionIfMissing(annonceCollection, ...annonceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const annonce: IAnnonce = { id: 123 };
        const annonce2: IAnnonce = { id: 456 };
        expectedResult = service.addAnnonceToCollectionIfMissing([], annonce, annonce2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(annonce);
        expect(expectedResult).toContain(annonce2);
      });

      it('should accept null and undefined values', () => {
        const annonce: IAnnonce = { id: 123 };
        expectedResult = service.addAnnonceToCollectionIfMissing([], null, annonce, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(annonce);
      });

      it('should return initial array if no Annonce is added', () => {
        const annonceCollection: IAnnonce[] = [{ id: 123 }];
        expectedResult = service.addAnnonceToCollectionIfMissing(annonceCollection, undefined, null);
        expect(expectedResult).toEqual(annonceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
