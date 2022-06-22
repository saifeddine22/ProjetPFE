import dayjs from 'dayjs/esm';
import { IPhoto } from 'app/entities/photo/photo.model';
import { ICommentaire } from 'app/entities/commentaire/commentaire.model';
import { INote } from 'app/entities/note/note.model';
import { IUser } from 'app/entities/user/user.model';
import { ICommune } from 'app/entities/commune/commune.model';
import { IActivite } from 'app/entities/activite/activite.model';

export interface IAnnonce {
  id?: number;
  titre?: string;
  description?: string;
  adresse?: string;
  geometry?: string;
  status?: boolean | null;
  dateAnnonce?: dayjs.Dayjs | null;
  photos?: IPhoto[] | null;
  commentaires?: ICommentaire[] | null;
  notes?: INote[] | null;
  user?: IUser;
  commune?: ICommune | null;
  activite?: IActivite;
}

export class Annonce implements IAnnonce {
  constructor(
    public id?: number,
    public titre?: string,
    public description?: string,
    public adresse?: string,
    public geometry?: string,
    public status?: boolean | null,
    public dateAnnonce?: dayjs.Dayjs | null,
    public photos?: IPhoto[] | null,
    public commentaires?: ICommentaire[] | null,
    public notes?: INote[] | null,
    public user?: IUser,
    public commune?: ICommune | null,
    public activite?: IActivite
  ) {
    this.status = this.status ?? false;
  }
}

export function getAnnonceIdentifier(annonce: IAnnonce): number | undefined {
  return annonce.id;
}
