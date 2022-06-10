import { IAnnonce } from 'app/entities/annonce/annonce.model';
import { ICategorie } from 'app/entities/categorie/categorie.model';

export interface IActivite {
  id?: number;
  nom?: string | null;
  nomAr?: string | null;
  annonces?: IAnnonce[] | null;
  categorie?: ICategorie;
}

export class Activite implements IActivite {
  constructor(
    public id?: number,
    public nom?: string | null,
    public nomAr?: string | null,
    public annonces?: IAnnonce[] | null,
    public categorie?: ICategorie
  ) {}
}

export function getActiviteIdentifier(activite: IActivite): number | undefined {
  return activite.id;
}
