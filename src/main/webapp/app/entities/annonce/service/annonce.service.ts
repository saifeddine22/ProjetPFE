import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnnonce, getAnnonceIdentifier } from '../annonce.model';

import 'ol/ol.css';
import { Fill, Stroke, Style, Circle as CircleStyle, Icon } from 'ol/style';
import GeoJSON from 'ol/format/GeoJSON';
import Map from 'ol/Map';
import View from 'ol/View';
import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import { toStringHDMS } from 'ol/coordinate';
import VectorSource from 'ol/source/Vector';
import VectorLayer from 'ol/layer/Vector';
import Draw from 'ol/interaction/Draw';
import Overlay from 'ol/Overlay';
import { toLonLat } from 'ol/proj';

export type EntityResponseType = HttpResponse<IAnnonce>;
export type EntityArrayResponseType = HttpResponse<IAnnonce[]>;

@Injectable({ providedIn: 'root' })
export class AnnonceService {
  map!: Map;
  latitude = 0;
  longitude = 0;
  source = new VectorSource({wrapX: false});
  vector = new VectorLayer({ source: this.source,opacity:0.3});
  draw = new Draw({source: this.source,type: 'Point',});
  drawCircle = new Draw({source: this.source,type: 'Circle',});
  view = new View({center: [-6.531926743408018, 33.6278503045078],zoom: 8,projection: 'EPSG:4326',});

  ajustementGPSPointsVector = new VectorSource();

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/annonces');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}
  initilizeMap(): void {
    this.map = new Map({
      target: 'map',
      layers: [],
      view: this.view,
    }); /* center: [-10.25838565457947,28.438727490809264], zoom: 5.22*/
    const layer = new TileLayer({
      visible: true,
      preload: Infinity,
      source: new XYZ({
        url: 'http://{1-4}.base.maps.cit.api.here.com/maptile/2.1/maptile/newest/normal.day/{z}/{x}/{y}/256/png8?app_id=xWVIueSv6JL0aJ5xqTxb&app_code=djPZyynKsbTjIUDOBcHZ2g',
        crossOrigin: 'anonymous',
      }),
    });
    this.map.addLayer(layer);
    /* const ajustementDrawPolygonStyle = new Style({
      fill: new Fill({color: 'rgba(255, 255, 255, 0.2)'}),
      stroke: new Stroke({color: '#ffcc33',width: 2}),
      image: new CircleStyle({radius: 5,fill: new Fill({color: '#ffcc33'})})
    }); */
    function ajustementDrawPolygonStyle(feature: { get: (arg0: string) => string }): Style {
      if (feature.get('Catégorie') === 'Bricolage-Travaux') {
        return new Style({
          fill: new Fill({ color: 'rgba(255, 255, 255, 0.2)' }),
          stroke: new Stroke({ color: '#ffcc33', width: 2 }),
          image: new Icon({
            anchor: [0.5, 46],
            anchorXUnits: 'fraction',
            anchorYUnits: 'pixels',
            src: 'content/images/mapIcons/canvas.png',
          }),
        });
      } else {
        return new Style({
          fill: new Fill({ color: 'rgba(255, 255, 255, 0.2)' }),
          stroke: new Stroke({ color: '#ffcc33', width: 2 }),
          image: new CircleStyle({ radius: 5, fill: new Fill({ color: '#000' }) }),
        });
      }
    }

    const ajustementDrawPolygonVector = new VectorLayer({
      visible: true,
      source: this.ajustementGPSPointsVector,
      style: ajustementDrawPolygonStyle,
    });
    this.map.addLayer(ajustementDrawPolygonVector);

    const closer = document.getElementById('popup-closer');

    /**
     * Create an overlay to anchor the popup to the map.
     */
    const overlay = new Overlay({
      element: document.getElementById('popup')!,
      autoPan: {
        animation: {
          duration: 250,
        },
      },
    });

    closer!.onclick = function () {
      overlay.setPosition(undefined);
      closer!.blur();
      return false;
    };

    this.map.addOverlay(overlay);

    this.map.addLayer(this.vector);
    this.map.addInteraction(this.draw);
    /* this.map.addInteraction(this.drawCircle);
    this.drawCircle.on('drawend', (event) =>{
      const ext = event.feature.getGeometry()?.getExtent();
      this.map.getView().fit(ext!); 
    }); */
    /* this.map.addInteraction(this.draw); */
    this.vector.getSource()?.clear();

    this.map.on('click',  (evt) => {
      const coordinate = evt.coordinate;
      const hdms = toStringHDMS(toLonLat(coordinate));
      this.latitude = coordinate[0];
      this.longitude = coordinate[1];
      this.map.forEachFeatureAtPixel(evt.pixel,(feature) => {
        const description = String(feature.get('nom'));
        const activ = String(feature.get('Activité'));
        const categ = String(feature.get('Catégorie'));
        if(feature.get('Catégorie') !== undefined){
          document.getElementById('popup-content')!.innerHTML = '<p class="h2">Annonce :</p>'+
          '<p class="h4">'+categ+'</p>'+
          '<p class="h5">'+activ+'</p>'+
          '<p class="h6">'+description+'</p>'
          +'<code>' + hdms + '</code>';
          overlay.setPosition(coordinate);
          
          this.map.removeInteraction(this.drawCircle);
        }
      });
      if(Number(this.vector.getSource()?.getFeatures().length) > 1){
        const fk = this.vector.getSource()?.getFeatures()[0];
        this.vector.getSource()?.removeFeature(fk!);
      }
      
      /* this.draw.removeLastPoint();
      this.map.removeInteraction(this.draw); */
    
    });
  }

  vectorMap(): void {
    
    let arr = [];
    arr = JSON.parse(String(sessionStorage.getItem('dataAnnonce')));
    console.log(arr);
    const format = new GeoJSON({});
    let dldlGeojson = '{"type": "FeatureCollection","features": [';
    let testGeojson = '';
    if (arr.length > 0) {
      for (let index = 0; index < arr.length; index++) {
        const element = arr[index];
        const id = String(element.id);
        const description = String(element.description);
        const lat = String(element.latitude);
        const lon = String(element.longitude);
        const activ = String(element.activite.nomFr);
        const categ = String(element.activite.categorieFr);
        testGeojson = '{"id" : "'+id+'","type": "Feature","geometry":{"type":"Point", "coordinates":['+lat+','+lon+']},"properties": {"id": "'+id+'","nom": "'+description+'","Activité": "'+activ+'","Catégorie": "'+categ+'"}}';
        testGeojson = testGeojson.replace(/(\r\n|\n|\r)/gm, "");
        dldlGeojson +=testGeojson+','; 
      }
    } else {
      if(arr.id !== undefined){
        const id = String(arr.id);
        const description = String(arr.description);

        const lat = String(arr.latitude);
        const lon = String(arr.longitude);
        const activ = String(arr.activite.nomFr);
        const categ = String(arr.activite.categorieFr);
        testGeojson ='{"id" : "' +id +'","type": "Feature","geometry":{"type":"Point", "coordinates":[' +lat +',' +lon +']},"properties": {"id": "' +id +'","nom": "' +description +'","Activité": "' +activ +'","Catégorie": "' +categ +'"}}';
        testGeojson = testGeojson.replace(/(\r\n|\n|\r)/gm, '');
        dldlGeojson += testGeojson + ',';
      }
    }

    dldlGeojson += ']}';
    dldlGeojson = dldlGeojson.replace(',]}', ']}');

    console.log(dldlGeojson);
    const ft = format.readFeatures(dldlGeojson, { dataProjection: 'EPSG:4326', featureProjection: 'EPSG:4326' });
    this.ajustementGPSPointsVector.clear();
    console.log(this.ajustementGPSPointsVector.getFeatures().length);
    this.ajustementGPSPointsVector.addFeatures(ft);
    const ext = this.ajustementGPSPointsVector.getExtent();
    this.map.getView().fit(ext); 
    this.map.getView().setZoom(9);
    console.log(this.ajustementGPSPointsVector.getFeatures().length);
    this.map.updateSize();
  }
  /* getCoord(event: any): Coordinate {
    /* const coordinate = transform(this.map.getEventCoordinate(event),'EPSG:3857','EPSG:4326'); 
    const coordinate = this.map.getEventCoordinate(event);
    this.latitude = coordinate[0];
    this.longitude = coordinate[1];
    /* alert(`latitude :  ${this.latitude} , longitude :  ${this.longitude}`); 
    this.map.forEachFeatureAtPixel(event.pixel,function(feature, layer) {
      // console.log(layer.get('name'));
          if(layer.get('name')==='soufiane'){
              alert(feature.get('nom'));
          }
    });
    return coordinate;
  } */
  

  create(annonce: IAnnonce): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(annonce);
    return this.http
      .post<IAnnonce>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(annonce: IAnnonce): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(annonce);
    return this.http
      .put<IAnnonce>(`${this.resourceUrl}/${getAnnonceIdentifier(annonce) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(annonce: IAnnonce): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(annonce);
    return this.http
      .patch<IAnnonce>(`${this.resourceUrl}/${getAnnonceIdentifier(annonce) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAnnonce>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  findByConnectedUser(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnnonce[]>(`${this.resourceUrl}/connectedUser`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  findByActiviteId(activiteId: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnnonce[]>(`${this.resourceUrl}/activite?activiteId=${activiteId}`, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnnonce[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
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

  protected convertDateFromClient(annonce: IAnnonce): IAnnonce {
    return Object.assign({}, annonce, {
      dateAnnonce: annonce.dateAnnonce?.isValid() ? annonce.dateAnnonce.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateAnnonce = res.body.dateAnnonce ? dayjs(res.body.dateAnnonce) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((annonce: IAnnonce) => {
        annonce.dateAnnonce = annonce.dateAnnonce ? dayjs(annonce.dateAnnonce) : undefined;
      });
    }
    return res;
  }
}
