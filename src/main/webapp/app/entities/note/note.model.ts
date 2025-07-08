import { IAnnonce } from 'app/entities/annonce/annonce.model';

export interface INote {
  id?: number;
  valeur?: number | null;
  annonce?: IAnnonce | null;
}

export class Note implements INote {
  constructor(public id?: number, public valeur?: number | null, public annonce?: IAnnonce | null) {}
}

export function getNoteIdentifier(note: INote): number | undefined {
  return note.id;
}
