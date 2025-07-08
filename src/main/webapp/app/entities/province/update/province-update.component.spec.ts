import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ProvinceService } from '../service/province.service';
import { IProvince, Province } from '../province.model';
import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';

import { ProvinceUpdateComponent } from './province-update.component';

describe('Province Management Update Component', () => {
  let comp: ProvinceUpdateComponent;
  let fixture: ComponentFixture<ProvinceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let provinceService: ProvinceService;
  let regionService: RegionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ProvinceUpdateComponent],
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
      .overrideTemplate(ProvinceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProvinceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    provinceService = TestBed.inject(ProvinceService);
    regionService = TestBed.inject(RegionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Region query and add missing value', () => {
      const province: IProvince = { id: 456 };
      const region: IRegion = { id: 81212 };
      province.region = region;

      const regionCollection: IRegion[] = [{ id: 26943 }];
      jest.spyOn(regionService, 'query').mockReturnValue(of(new HttpResponse({ body: regionCollection })));
      const additionalRegions = [region];
      const expectedCollection: IRegion[] = [...additionalRegions, ...regionCollection];
      jest.spyOn(regionService, 'addRegionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ province });
      comp.ngOnInit();

      expect(regionService.query).toHaveBeenCalled();
      expect(regionService.addRegionToCollectionIfMissing).toHaveBeenCalledWith(regionCollection, ...additionalRegions);
      expect(comp.regionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const province: IProvince = { id: 456 };
      const region: IRegion = { id: 92166 };
      province.region = region;

      activatedRoute.data = of({ province });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(province));
      expect(comp.regionsSharedCollection).toContain(region);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Province>>();
      const province = { id: 123 };
      jest.spyOn(provinceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ province });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: province }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(provinceService.update).toHaveBeenCalledWith(province);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Province>>();
      const province = new Province();
      jest.spyOn(provinceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ province });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: province }));
      saveSubject.complete();

      // THEN
      expect(provinceService.create).toHaveBeenCalledWith(province);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Province>>();
      const province = { id: 123 };
      jest.spyOn(provinceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ province });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(provinceService.update).toHaveBeenCalledWith(province);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRegionById', () => {
      it('Should return tracked Region primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRegionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
