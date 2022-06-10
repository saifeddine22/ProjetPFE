import { IActivite } from 'app/entities/activite/activite.model';

export interface ICategorie {
  id?: number;
  nom?: string | null;
  nomAr?: string | null;
  activites?: IActivite[] | null;
}

export class Categorie implements ICategorie {
  constructor(public id?: number, public nom?: string | null, public nomAr?: string | null, public activites?: IActivite[] | null) {}
}

export function getCategorieIdentifier(categorie: ICategorie): number | undefined {
  return categorie.id;
}
