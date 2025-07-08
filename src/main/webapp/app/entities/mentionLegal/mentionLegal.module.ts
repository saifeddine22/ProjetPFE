import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MentionLegalRoutingModule } from './route/mentionLegal-routing.module';
import { PolitiquesComponent } from './politiques/politiques.component';
import { ConditionComponent } from './condition/condition.component';
import { AboutUsComponent } from './about-us/about-us.component';

@NgModule({
  imports: [SharedModule, MentionLegalRoutingModule],
  declarations: [PolitiquesComponent, ConditionComponent, AboutUsComponent],
})
export class MentionLegalModule {}
