import { IAnnonce } from 'app/entities/annonce/annonce.model';

export interface IPhoto {
  id?: number;
  url?: string | null;
  libelle?: string | null;
  imageContentType?: string | null;
  image?: string | null;
  annonce?: IAnnonce | null;
}

export class Photo implements IPhoto {
  constructor(
    public id?: number,
    public url?: string | null,
    public libelle?: string | null,
    public imageContentType?: string | null,
    public image?: string | null,
    public annonce?: IAnnonce | null
  ) {}
}

export function getPhotoIdentifier(photo: IPhoto): number | undefined {
  return photo.id;
}
