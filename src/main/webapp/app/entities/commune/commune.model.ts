import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { IProvince } from 'app/entities/province/province.model';

export interface ICommune {
  id?: number;
  codeReg?: number | null;
  codeProv?: number | null;
  provinceFr?: string | null;
  provinceAr?: string | null;
  regionFr?: string | null;
  regionAr?: string | null;
  cercleFr?: string | null;
  codeCercle?: number | null;
  comFr?: string | null;
  codeCom?: number | null;
  centreFr?: string | null;
  codAc?: number | null;
  comAr?: string | null;
  cc?: number | null;
  centreAr?: string | null;
  nomAr?: string | null;
  nomFr?: string | null;
  geometry?: string | null;
  annonces?: IAnnonce[] | null;
  province?: IProvince;
}

export class Commune implements ICommune {
  constructor(
    public id?: number,
    public codeReg?: number | null,
    public codeProv?: number | null,
    public provinceFr?: string | null,
    public provinceAr?: string | null,
    public regionFr?: string | null,
    public regionAr?: string | null,
    public cercleFr?: string | null,
    public codeCercle?: number | null,
    public comFr?: string | null,
    public codeCom?: number | null,
    public centreFr?: string | null,
    public codAc?: number | null,
    public comAr?: string | null,
    public cc?: number | null,
    public centreAr?: string | null,
    public nomAr?: string | null,
    public nomFr?: string | null,
    public geometry?: string | null,
    public annonces?: IAnnonce[] | null,
    public province?: IProvince
  ) {}
}

export function getCommuneIdentifier(commune: ICommune): number | undefined {
  return commune.id;
}
