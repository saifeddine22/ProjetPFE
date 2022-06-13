import { IActivite } from 'app/entities/activite/activite.model';

export interface ICategorie {
  id?: number;
  nomFr?: string | null;
  nomAr?: string | null;
  activites?: IActivite[] | null;
}

export class Categorie implements ICategorie {
  constructor(public id?: number, public nomFr?: string | null, public nomAr?: string | null, public activites?: IActivite[] | null) {}
}

export function getCategorieIdentifier(categorie: ICategorie): number | undefined {
  return categorie.id;
}
