import { IProvince } from 'app/entities/province/province.model';

export interface IRegion {
  id?: number;
  codeReg?: number | null;
  nomFr?: string | null;
  nomAr?: string | null;
  geometry?: string | null;
  provinces?: IProvince[] | null;
}

export class Region implements IRegion {
  constructor(
    public id?: number,
    public codeReg?: number | null,
    public nomFr?: string | null,
    public nomAr?: string | null,
    public geometry?: string | null,
    public provinces?: IProvince[] | null
  ) {}
}

export function getRegionIdentifier(region: IRegion): number | undefined {
  return region.id;
}
