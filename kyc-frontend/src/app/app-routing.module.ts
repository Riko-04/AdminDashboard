import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { AdminDashboardComponent } from './pages/admin-dashboard/admin-dashboard.component';
import { CustomerDashboardComponent } from './pages/customer-dashboard/customer-dashboard.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { UserManagementComponent } from './pages/user-management/user-management.component';
import { GroupManagementComponent } from './pages/group-management/group-management.component';
import { TermOfServicesComponent } from './pages/term-of-services/term-of-services.component';
import { PrivacyPolicyComponent } from './pages/privacy-policy/privacy-policy.component';
import { ContactUsComponent } from './pages/contact-us/contact-us.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'terms', component: TermOfServicesComponent },
  { path: 'privacy', component: PrivacyPolicyComponent },
  { path: 'contact', component: ContactUsComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'admin', component: AdminDashboardComponent },
  { path: 'customer', component: CustomerDashboardComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'users', component: UserManagementComponent },
  { path: 'groups', component: GroupManagementComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
