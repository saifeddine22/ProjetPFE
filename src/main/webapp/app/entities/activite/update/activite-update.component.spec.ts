import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ActiviteService } from '../service/activite.service';
import { IActivite, Activite } from '../activite.model';
import { ICategorie } from 'app/entities/categorie/categorie.model';
import { CategorieService } from 'app/entities/categorie/service/categorie.service';

import { ActiviteUpdateComponent } from './activite-update.component';

describe('Activite Management Update Component', () => {
  let comp: ActiviteUpdateComponent;
  let fixture: ComponentFixture<ActiviteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let activiteService: ActiviteService;
  let categorieService: CategorieService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ActiviteUpdateComponent],
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
      .overrideTemplate(ActiviteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ActiviteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    activiteService = TestBed.inject(ActiviteService);
    categorieService = TestBed.inject(CategorieService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Categorie query and add missing value', () => {
      const activite: IActivite = { id: 456 };
      const categorie: ICategorie = { id: 65624 };
      activite.categorie = categorie;

      const categorieCollection: ICategorie[] = [{ id: 46909 }];
      jest.spyOn(categorieService, 'query').mockReturnValue(of(new HttpResponse({ body: categorieCollection })));
      const additionalCategories = [categorie];
      const expectedCollection: ICategorie[] = [...additionalCategories, ...categorieCollection];
      jest.spyOn(categorieService, 'addCategorieToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ activite });
      comp.ngOnInit();

      expect(categorieService.query).toHaveBeenCalled();
      expect(categorieService.addCategorieToCollectionIfMissing).toHaveBeenCalledWith(categorieCollection, ...additionalCategories);
      expect(comp.categoriesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const activite: IActivite = { id: 456 };
      const categorie: ICategorie = { id: 43304 };
      activite.categorie = categorie;

      activatedRoute.data = of({ activite });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(activite));
      expect(comp.categoriesSharedCollection).toContain(categorie);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Activite>>();
      const activite = { id: 123 };
      jest.spyOn(activiteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: activite }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(activiteService.update).toHaveBeenCalledWith(activite);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Activite>>();
      const activite = new Activite();
      jest.spyOn(activiteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: activite }));
      saveSubject.complete();

      // THEN
      expect(activiteService.create).toHaveBeenCalledWith(activite);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Activite>>();
      const activite = { id: 123 };
      jest.spyOn(activiteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ activite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(activiteService.update).toHaveBeenCalledWith(activite);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCategorieById', () => {
      it('Should return tracked Categorie primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCategorieById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
