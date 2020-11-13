import { SellBooksComponent } from './sell-books/sell-books.component';
import { BookDetailsComponent } from './book-details/book-details.component';
import { LandingHomepageComponent } from './landing-homepage/landing-homepage.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UserDetailsComponent } from './user-details/user-details.component';
import { HeaderComponent } from './header/header.component';
import { NavbarComponent } from './navbar/navbar.component';
import { FooterComponent } from './footer/footer.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { CartComponent } from './cart/cart.component';
import {AuthGuardService} from './auth-services/auth-guard.service'

const routes: Routes = [
  { path: '', component: LandingHomepageComponent,canActivate:[AuthGuardService],
  children:[
    { path: '', component: DashboardComponent,canActivate:[AuthGuardService]},
    { path: 'cart', component: CartComponent,canActivate:[AuthGuardService]},
    { path: 'sell', component: SellBooksComponent,canActivate:[AuthGuardService]},
    { path: 'bookdetails/:isbn', component: BookDetailsComponent,canActivate:[AuthGuardService]},
    { path: 'dashboard', component: DashboardComponent,canActivate:[AuthGuardService]},
    { path: 'user', component: UserDetailsComponent,canActivate:[AuthGuardService]}
  ]},
  { path: 'login', component: LoginComponent },
  { path:'register',component:RegisterComponent},
  { path:'forgotpassword',component:ForgotPasswordComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
 }
