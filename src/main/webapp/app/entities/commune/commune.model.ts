import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { IProvince } from 'app/entities/province/province.model';

export interface ICommune {
  id?: number;
  nom?: string | null;
  nomAr?: string | null;
  geometry?: string | null;
  attachement?: string | null;
  annonces?: IAnnonce[] | null;
  province?: IProvince;
}

export class Commune implements ICommune {
  constructor(
    public id?: number,
    public nom?: string | null,
    public nomAr?: string | null,
    public geometry?: string | null,
    public attachement?: string | null,
    public annonces?: IAnnonce[] | null,
    public province?: IProvince
  ) {}
}

export function getCommuneIdentifier(commune: ICommune): number | undefined {
  return commune.id;
}
