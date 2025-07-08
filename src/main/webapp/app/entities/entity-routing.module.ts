import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'personne',
        data: { pageTitle: 'bricoviteApp.personne.home.title' },
        loadChildren: () => import('./personne/personne.module').then(m => m.PersonneModule),
      },
      {
        path: 'annonce',
        data: { pageTitle: 'bricoviteApp.annonce.home.title' },
        loadChildren: () => import('./annonce/annonce.module').then(m => m.AnnonceModule),
      },
      {
        path: 'photo',
        data: { pageTitle: 'bricoviteApp.photo.home.title' },
        loadChildren: () => import('./photo/photo.module').then(m => m.PhotoModule),
      },
      {
        path: 'region',
        data: { pageTitle: 'bricoviteApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      {
        path: 'province',
        data: { pageTitle: 'bricoviteApp.province.home.title' },
        loadChildren: () => import('./province/province.module').then(m => m.ProvinceModule),
      },
      {
        path: 'commune',
        data: { pageTitle: 'bricoviteApp.commune.home.title' },
        loadChildren: () => import('./commune/commune.module').then(m => m.CommuneModule),
      },
      {
        path: 'activite',
        data: { pageTitle: 'bricoviteApp.activite.home.title' },
        loadChildren: () => import('./activite/activite.module').then(m => m.ActiviteModule),
      },
      {
        path: 'categorie',
        data: { pageTitle: 'bricoviteApp.categorie.home.title' },
        loadChildren: () => import('./categorie/categorie.module').then(m => m.CategorieModule),
      },
      {
        path: 'commentaire',
        data: { pageTitle: 'bricoviteApp.commentaire.home.title' },
        loadChildren: () => import('./commentaire/commentaire.module').then(m => m.CommentaireModule),
      },
      {
        path: 'note',
        data: { pageTitle: 'bricoviteApp.note.home.title' },
        loadChildren: () => import('./note/note.module').then(m => m.NoteModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */

      {
        path: '',
        data: { pageTitle: '' },
        loadChildren: () => import('./mentionLegal/mentionLegal.module').then(m => m.MentionLegalModule),
      },
    ]),
  ],
})
export class EntityRoutingModule {}
