import { IUser } from 'app/entities/user/user.model';

export interface IPersonne {
  id?: number;
  cnie?: string;
  tel?: string | null;
  user?: IUser;
}

export class Personne implements IPersonne {
  constructor(public id?: number, public cnie?: string, public tel?: string | null, public user?: IUser) {}
}

export function getPersonneIdentifier(personne: IPersonne): number | undefined {
  return personne.id;
}
