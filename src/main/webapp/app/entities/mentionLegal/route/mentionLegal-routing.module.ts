import { AboutUsComponent } from './../about-us/about-us.component';
import { PolitiquesComponent } from './../politiques/politiques.component';
import { ContactComponent } from '../contact/contact.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

/* import { UserRouteAccessService } from 'app/core/auth/user-route-access.service'; */
import { ConditionComponent } from '../condition/condition.component';

const mentionLegalRoute: Routes = [
  {
    path: 'contactUs',
    component: ContactComponent,
    /* canActivate: [UserRouteAccessService], */
  },
  {
    path: 'politique-de-confidentialite',
    component: PolitiquesComponent,
    /* canActivate: [UserRouteAccessService], */
  },
  {
    path: 'condition-d-utilisation',
    component: ConditionComponent,
    /* canActivate: [UserRouteAccessService], */
  },
  {
    path: 'aboutUs',
    component: AboutUsComponent,
    /* canActivate: [UserRouteAccessService], */
  },
];

@NgModule({
  imports: [RouterModule.forChild(mentionLegalRoute)],
  exports: [RouterModule],
})
export class MentionLegalRoutingModule {}
