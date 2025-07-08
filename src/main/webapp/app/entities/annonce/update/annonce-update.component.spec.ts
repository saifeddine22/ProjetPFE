import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AnnonceService } from '../service/annonce.service';
import { IAnnonce, Annonce } from '../annonce.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ICommune } from 'app/entities/commune/commune.model';
import { CommuneService } from 'app/entities/commune/service/commune.service';
import { IActivite } from 'app/entities/activite/activite.model';
import { ActiviteService } from 'app/entities/activite/service/activite.service';

import { AnnonceUpdateComponent } from './annonce-update.component';

describe('Annonce Management Update Component', () => {
  let comp: AnnonceUpdateComponent;
  let fixture: ComponentFixture<AnnonceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let annonceService: AnnonceService;
  let userService: UserService;
  let communeService: CommuneService;
  let activiteService: ActiviteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AnnonceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AnnonceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AnnonceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    annonceService = TestBed.inject(AnnonceService);
    userService = TestBed.inject(UserService);
    communeService = TestBed.inject(CommuneService);
    activiteService = TestBed.inject(ActiviteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const annonce: IAnnonce = { id: 456 };
      const user: IUser = { id: 68143 };
      annonce.user = user;

      const userCollection: IUser[] = [{ id: 23402 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ annonce });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Commune query and add missing value', () => {
      const annonce: IAnnonce = { id: 456 };
      const commune: ICommune = { id: 60405 };
      annonce.commune = commune;

      const communeCollection: ICommune[] = [{ id: 3607 }];
      jest.spyOn(communeService, 'query').mockReturnValue(of(new HttpResponse({ body: communeCollection })));
      const additionalCommunes = [commune];
      const expectedCollection: ICommune[] = [...additionalCommunes, ...communeCollection];
      jest.spyOn(communeService, 'addCommuneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ annonce });
      comp.ngOnInit();

      expect(communeService.query).toHaveBeenCalled();
      expect(communeService.addCommuneToCollectionIfMissing).toHaveBeenCalledWith(communeCollection, ...additionalCommunes);
      expect(comp.communesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Activite query and add missing value', () => {
      const annonce: IAnnonce = { id: 456 };
      const activite: IActivite = { id: 73736 };
      annonce.activite = activite;

      const activiteCollection: IActivite[] = [{ id: 18056 }];
      jest.spyOn(activiteService, 'query').mockReturnValue(of(new HttpResponse({ body: activiteCollection })));
      const additionalActivites = [activite];
      const expectedCollection: IActivite[] = [...additionalActivites, ...activiteCollection];
      jest.spyOn(activiteService, 'addActiviteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ annonce });
      comp.ngOnInit();

      expect(activiteService.query).toHaveBeenCalled();
      expect(activiteService.addActiviteToCollectionIfMissing).toHaveBeenCalledWith(activiteCollection, ...additionalActivites);
      expect(comp.activitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const annonce: IAnnonce = { id: 456 };
      const user: IUser = { id: 26014 };
      annonce.user = user;
      const commune: ICommune = { id: 81890 };
      annonce.commune = commune;
      const activite: IActivite = { id: 63824 };
      annonce.activite = activite;

      activatedRoute.data = of({ annonce });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(annonce));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.communesSharedCollection).toContain(commune);
      expect(comp.activitesSharedCollection).toContain(activite);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Annonce>>();
      const annonce = { id: 123 };
      jest.spyOn(annonceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ annonce });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: annonce }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(annonceService.update).toHaveBeenCalledWith(annonce);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Annonce>>();
      const annonce = new Annonce();
      jest.spyOn(annonceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ annonce });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: annonce }));
      saveSubject.complete();

      // THEN
      expect(annonceService.create).toHaveBeenCalledWith(annonce);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Annonce>>();
      const annonce = { id: 123 };
      jest.spyOn(annonceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ annonce });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(annonceService.update).toHaveBeenCalledWith(annonce);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackCommuneById', () => {
      it('Should return tracked Commune primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCommuneById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackActiviteById', () => {
      it('Should return tracked Activite primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackActiviteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
