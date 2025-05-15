import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnnonce, getAnnonceIdentifier } from '../annonce.model';
import { INote } from 'app/entities/note/note.model';

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
import { Feature } from 'ol';
import { Point } from 'ol/geom';
import Tile from 'ol/Tile';
export type EntityResponseType = HttpResponse<IAnnonce>;
export type EntityArrayResponseType = HttpResponse<IAnnonce[]>;

@Injectable({ providedIn: 'root' })
export class AnnonceService {
  note!: INote;
  moyNote = 0;
  rating = this.moyNote;
  map!: Map;
  latitude = 0;
  longitude = 0;
  source = new VectorSource({ wrapX: false });
  /* vector = new VectorLayer({ source: this.source,opacity:0.3}); */
  vector = new VectorLayer({
    source: this.source,
    style: new Style({
      fill: new Fill({ color: 'rgba(255, 255, 255, 0.2)' }),
      stroke: new Stroke({ color: '#e0ff33', width: 2 }),
      image: new Icon({
        anchor: [0.5, 46],
        anchorXUnits: 'fraction',
        anchorYUnits: 'pixels',
        src: 'content/images/mapIcons/location.png',
      }),
    }),
  });
  draw = new Draw({
    source: this.source,
    type: 'Point',
    style: new Style({
      fill: new Fill({ color: 'rgba(255, 255, 255, 0.2)' }),
      stroke: new Stroke({ color: '#ffcc33', width: 2 }),
      image: new Icon({
        anchor: [0.5, 46],
        anchorXUnits: 'fraction',
        anchorYUnits: 'pixels',
        src: 'content/images/mapIcons/location.png',
      }),
    }),
  });
  drawCircle = new Draw({ source: this.source, type: 'Circle' });
  view = new View({ center: [-6.531926743408018, 33.6278503045078], zoom: 8, projection: 'EPSG:4326' });

  ajustementGPSPointsVector = new VectorSource();

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/annonces');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}
  initilizeMap(): void {
    try {
      // Créer la carte
      this.map = new Map({
        target: 'map',
        layers: [],
        view: this.view,
      });

      // Ajouter une couche de tuiles OSM directement
      const osmLayer = new TileLayer({
        visible: true,
        preload: Infinity,
        source: new XYZ({
          url: 'https://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png',
          attributions: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
        }),
      });

      this.map.addLayer(osmLayer);

      // Continuer avec le reste de votre code...
    } catch (err) {
      console.error('Erreur dans initilizeMap:', err);
    }
    // Handle tile load errors globally
    /* this.map.on('tileloaderror', (event: any) => {  // Explicitly typing 'event' as 'any'
      const tileUrl = event.tile.src;
      console.error(`Error loading tile: ${tileUrl}`, event);
      // Optionally, you could display a fallback tile or a message to the user here
    });

    this.map.addLayer(layer);*/
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
            src: 'content/images/mapIcons/location2.png',
          }),
        });
      } else {
        return new Style({
          fill: new Fill({ color: 'rgba(255, 255, 255, 0.2)' }),
          stroke: new Stroke({ color: '#ffcc33', width: 2 }),
          image: new Icon({
            anchor: [0.5, 46],
            anchorXUnits: 'fraction',
            anchorYUnits: 'pixels',
            src: 'content/images/mapIcons/location1.png',
          }),
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

    this.map.on('click', evt => {
      const coordinate = evt.coordinate;
      const hdms = toStringHDMS(toLonLat(coordinate));
      this.latitude = coordinate[0];
      this.longitude = coordinate[1];
      /* alert(`latitude :  ${this.latitude} , longitude :  ${this.longitude}`); */
      this.map.forEachFeatureAtPixel(evt.pixel, feature => {
        const id = String(feature.get('id'));
        const description = String(feature.get('nom'));
        const activ = String(feature.get('Activité'));
        const categ = String(feature.get('Catégorie'));
        if (feature.get('Catégorie') !== undefined) {
          document.getElementById('popup-content')!.innerHTML =
            '<p class="h2">Annonce :</p>' +
            `<a href="/annonce/${id}/view">` +
            '<p class="h4">' +
            categ +
            '</p>' +
            '<p class="h5">' +
            activ +
            '</p>' +
            '<p class="h6">' +
            description +
            '</p></a>' +
            '<code>' +
            hdms +
            '</code>';
          overlay.setPosition(coordinate);

          this.map.removeInteraction(this.drawCircle);
        }
      });
      if (Number(this.vector.getSource()?.getFeatures().length) > 1) {
        const fk = this.vector.getSource()?.getFeatures()[0];
        this.vector.getSource()?.removeFeature(fk!);
      }

      /* this.draw.removeLastPoint();
      this.map.removeInteraction(this.draw); */
    });
  }

  vectorMap(): void {
    /*let arr = [];
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
        testGeojson =
          '{"id" : "' +
          id +
          '","type": "Feature","geometry":{"type":"Point", "coordinates":[' +
          lat +
          ',' +
          lon +
          ']},"properties": {"id": "' +
          id +
          '","nom": "' +
          description +
          '","Activité": "' +
          activ +
          '","Catégorie": "' +
          categ +
          '"}}';
        testGeojson = testGeojson.replace(/(\r\n|\n|\r)/gm, '');
        dldlGeojson += testGeojson + ',';
      }
    } else {
      if (arr.id !== undefined) {
        const id = String(arr.id);
        const description = String(arr.description);

        const lat = String(arr.latitude);
        const lon = String(arr.longitude);
        const activ = String(arr.activite.nomFr);
        const categ = String(arr.activite.categorieFr);
        testGeojson =
          '{"id" : "' +
          id +
          '","type": "Feature","geometry":{"type":"Point", "coordinates":[' +
          lat +
          ',' +
          lon +
          ']},"properties": {"id": "' +
          id +
          '","nom": "' +
          description +
          '","Activité": "' +
          activ +
          '","Catégorie": "' +
          categ +
          '"}}';
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
    this.map.updateSize();*/
    /*   const arr: IAnnonce[] = JSON.parse(String(sessionStorage.getItem('dataAnnonce'))) || [];
       const format = new GeoJSON();

       const features: Feature<Point>[] = arr
         .map((element: IAnnonce) => {
           const id = String(element.id);
           const description = String(element.description);
           const lat = parseFloat(String(element.latitude)); // Ensure these are valid numbers
           const lon = parseFloat(String(element.longitude));
           const activ = String(element.activite?.nomFr);
           const categ = String(element.activite?.categorieFr);

           // Check if lat/lon are valid numbers
           if (isNaN(lat) || isNaN(lon)) {
             console.error(`Invalid coordinates for annonce ${id}: ${lat}, ${lon}`);
             return null; // Skip invalid entries
           }

           // Create a GeoJSON feature
           return new Feature({
             geometry: new Point([lon, lat]),
             id,
             nom: description,
             Activité: activ,
             Catégorie: categ
           });
         })
         .filter((feature): feature is Feature<Point> => feature !== null); // Filter out nulls

       // Clear previous features and add new ones
       this.ajustementGPSPointsVector.clear();
       this.ajustementGPSPointsVector.addFeatures(features); // Now `features` is of type `Feature<Point>[]`

       const ext = this.ajustementGPSPointsVector.getExtent();
       this.map.getView().fit(ext);
       this.map.updateSize();

     }*/
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
    try {
      // Récupérer les données d'annonce
      let arr = [];
      const annonceData = sessionStorage.getItem('dataAnnonce');

      if (annonceData) {
        arr = JSON.parse(annonceData);
      } else {
        console.warn("Aucune donnée d'annonce trouvée dans sessionStorage");
        return; // Sortir si pas de données
      }

      // Le reste de votre code pour vectorMap...
      // Ajoutez des vérifications pour s'assurer que les données existent avant de les utiliser
    } catch (err) {
      console.error('Erreur dans vectorMap:', err);
    }
  }
  // JavaScript code
  search_fast(): void {
    let input = (<HTMLInputElement>document.getElementById('rechRapide')).value;
    input = input.toLowerCase();
    const x = document.getElementsByClassName('annonceContent') as HTMLCollectionOf<HTMLElement>;

    for (let i = 0; i < x.length; i++) {
      if (!x[i].innerHTML.toLowerCase().includes(input)) {
        x[i].style.display = 'none';
      } else {
        x[i].style.display = 'list-item';
      }
    }
  }

  moy(notes: INote[]): number {
    if (notes.length !== 0) {
      const som = notes.map(n => n.valeur ?? 0).reduce((a, b) => a + b, 0);
      this.moyNote = som / notes.length;
    } else {
      this.moyNote = 0;
    }
    return this.moyNote;
  }

  updateRating(): number {
    this.rating = this.moyNote;
    return this.rating;
  }

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

  search(activiteId: number, provinceId: number, categorieId: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnnonce[]>(`${this.resourceUrl}/search?provinceId=${provinceId}&activiteId=${activiteId}&categorieId=${categorieId}`, {
        params: options,
        observe: 'response',
      })
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
