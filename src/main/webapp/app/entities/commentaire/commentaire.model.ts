import dayjs from 'dayjs/esm';
import { IAnnonce } from 'app/entities/annonce/annonce.model';

export interface ICommentaire {
  id?: number;
  details?: string | null;
  dateCommentaire?: dayjs.Dayjs | null;
  annonce?: IAnnonce | null;
}

export class Commentaire implements ICommentaire {
  constructor(
    public id?: number,
    public details?: string | null,
    public dateCommentaire?: dayjs.Dayjs | null,
    public annonce?: IAnnonce | null
  ) {}
}

export function getCommentaireIdentifier(commentaire: ICommentaire): number | undefined {
  return commentaire.id;
}
