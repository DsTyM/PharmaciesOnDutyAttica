import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {SpinnerComponent} from './spinner/spinner.component';
import {RouterModule, Routes} from "@angular/router";
import {AvailablePharmacyComponent} from './available-pharmacy/available-pharmacy.component';

const appRoutes: Routes = [
  {
    path: 'available-pharmacies',
    component: AvailablePharmacyComponent,
    data: {title: 'Pharmacies On Duty'}
  },
  {
    path: '',
    redirectTo: '/available-pharmacies',
    pathMatch: 'full'
  }
];

@NgModule({
  declarations: [
    AppComponent,
    SpinnerComponent,
    AvailablePharmacyComponent
  ],
  imports: [
    RouterModule.forRoot(appRoutes),
    BrowserModule,
    NgbModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
