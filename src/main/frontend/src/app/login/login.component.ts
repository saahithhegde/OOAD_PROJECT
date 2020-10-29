import { UserServiceService } from './../services/user-service.service';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../auth-services/authentication.service';
import { Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { NotificationService } from '../services/notification.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  emailText:string;
  passwordText:string;
  message:string;

  constructor(private notificationService:NotificationService,private authenticationService:AuthenticationService,private router:Router,private userServiceService:UserServiceService,private spinnerService: Ng4LoadingSpinnerService) { }

  ngOnInit() {

  }

  authenticate() {
    if(this.emailText && this.passwordText){
      this.spinnerService.show();
      this.userServiceService.login(this.emailText,this.passwordText).subscribe(
        (data)=>{
          if(data.token){
              sessionStorage.setItem('token', data.token);
              setTimeout(()=>this.spinnerService.hide(),3000)
              this.router.navigate(['']);
             }
      },(err)=>{
        setTimeout(()=>this.spinnerService.hide(),3000);
        this.notificationService.showError(JSON.stringify(err.error),"error");
      });
     }
    }


  redirectRegister(){
    this.spinnerService.show();
    this.router.navigate(['/register']);
    setTimeout(()=>this.spinnerService.hide(),3000)

  }
}
