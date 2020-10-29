import { UserServiceService } from './../services/user-service.service';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../auth-services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  emailText:string;
  passwordText:string;
  message:string;

  constructor(private authenticationService:AuthenticationService,private router:Router,private userServiceService:UserServiceService) { }

  ngOnInit() {

  }

  authenticate() {
    if(this.emailText && this.passwordText){
      this.userServiceService.login(this.emailText,this.passwordText).subscribe(
        (data)=>{
          if(data.token){
              sessionStorage.setItem('token', data.token);
              this.router.navigate(['']);
             }
      },(err)=>{
        this.message=JSON.stringify(err.error);
      });
     }
    }


  redirectRegister(){
    this.router.navigate(['/register'])
  }
}
