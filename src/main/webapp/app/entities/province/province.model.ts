import { ICommune } from 'app/entities/commune/commune.model';
import { IRegion } from 'app/entities/region/region.model';

export interface IProvince {
  id?: number;
  nom?: string | null;
  nomAr?: string | null;
  geometry?: string | null;
  attachement?: string | null;
  communes?: ICommune[] | null;
  region?: IRegion;
}

export class Province implements IProvince {
  constructor(
    public id?: number,
    public nom?: string | null,
    public nomAr?: string | null,
    public geometry?: string | null,
    public attachement?: string | null,
    public communes?: ICommune[] | null,
    public region?: IRegion
  ) {}
}

export function getProvinceIdentifier(province: IProvince): number | undefined {
  return province.id;
}
