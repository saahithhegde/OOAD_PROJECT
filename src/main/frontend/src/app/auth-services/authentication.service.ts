import { Router } from '@angular/router';
import { UserServiceService } from './../services/user-service.service';
import { Injectable } from '@angular/core';
import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private userServiceService:UserServiceService,private router:Router) { }


  isUserLoggedIn() {
    let user = sessionStorage.getItem('token')
    console.log(!(user === null))
    return !(user === null)
  }

  logOut() {
    this.userServiceService.logout();
    sessionStorage.clear();
  }
}
