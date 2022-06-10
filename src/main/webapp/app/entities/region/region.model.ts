import { IProvince } from 'app/entities/province/province.model';

export interface IRegion {
  id?: number;
  nom?: string | null;
  nomAr?: string | null;
  geometry?: string | null;
  attachement?: string | null;
  provinces?: IProvince[] | null;
}

export class Region implements IRegion {
  constructor(
    public id?: number,
    public nom?: string | null,
    public nomAr?: string | null,
    public geometry?: string | null,
    public attachement?: string | null,
    public provinces?: IProvince[] | null
  ) {}
}

export function getRegionIdentifier(region: IRegion): number | undefined {
  return region.id;
}
