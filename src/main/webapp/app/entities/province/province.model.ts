import { ICommune } from 'app/entities/commune/commune.model';
import { IRegion } from 'app/entities/region/region.model';

export interface IProvince {
  id?: number;
  codeReg?: number | null;
  codeProv?: number | null;
  nomFr?: string | null;
  nomAr?: string | null;
  regionFr?: string | null;
  regionAr?: string | null;
  geometry?: string | null;
  communes?: ICommune[] | null;
  region?: IRegion;
}

export class Province implements IProvince {
  constructor(
    public id?: number,
    public codeReg?: number | null,
    public codeProv?: number | null,
    public nomFr?: string | null,
    public nomAr?: string | null,
    public regionFr?: string | null,
    public regionAr?: string | null,
    public geometry?: string | null,
    public communes?: ICommune[] | null,
    public region?: IRegion
  ) {}
}

export function getProvinceIdentifier(province: IProvince): number | undefined {
  return province.id;
}
