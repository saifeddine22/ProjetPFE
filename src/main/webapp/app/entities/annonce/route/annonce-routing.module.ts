import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AnnonceComponent } from '../list/annonce.component';
import { AnnonceDetailComponent } from '../detail/annonce-detail.component';
import { AnnonceUpdateComponent } from '../update/annonce-update.component';
import { AnnonceRoutingResolveService } from './annonce-routing-resolve.service';
import { CommentaireComponent } from 'app/entities/commentaire/list/commentaire.component';
import { PhotoComponent } from 'app/entities/photo/list/photo.component';

const annonceRoute: Routes = [
  {
    path: '',
    component: AnnonceComponent,
    data: {
      defaultSort: 'dateAnnonce,desc',
    },
  },
  {
    path: ':id/view',
    component: AnnonceDetailComponent,
    resolve: {
      annonce: AnnonceRoutingResolveService,
    },
  },
  {
    path: 'new',
    component: AnnonceUpdateComponent,
    resolve: {
      annonce: AnnonceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnnonceUpdateComponent,
    resolve: {
      annonce: AnnonceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/commentaires',
    component: CommentaireComponent,

    resolve: {
      annonce: AnnonceRoutingResolveService,
    },
    data: {
      defaultSort: 'id,asc',
    },
  },

  {
    path: ':mesAnnonces',
    component: AnnonceComponent,
    data: {
      defaultSort: 'dateAnnonce,desc',
      mesAnnonces: true,
    },
  },
  {
    path: ':id/photos',
    component: PhotoComponent,

    resolve: {
      annonce: AnnonceRoutingResolveService,
    },
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(annonceRoute)],
  exports: [RouterModule],
})
export class AnnonceRoutingModule {}
