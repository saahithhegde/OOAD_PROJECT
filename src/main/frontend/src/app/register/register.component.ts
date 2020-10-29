import { Router } from '@angular/router';
import { UserServiceService } from './../services/user-service.service';
import { Component, OnInit } from '@angular/core';
import { Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { NotificationService } from '../services/notification.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  emailText:string;
  passwordText:string;
  phoneText:string;
  addressText:string;
  questionValue:string;
  answerText:string;
  confirmPasswordText:string;
  message:string;


  constructor(private notificationService:NotificationService,private userServiceService:UserServiceService,private router:Router,private spinnerService: Ng4LoadingSpinnerService) { }

  ngOnInit() {
  }

  cancel(){
    this.spinnerService.show();
    this.router.navigate(['/login']);
    setTimeout(()=>this.spinnerService.hide(),2000)
  }

  register(){
    if(this.emailText && this.passwordText && this.questionValue && this.answerText){
      this.spinnerService.show()
      this.userServiceService.register(this.emailText,this.passwordText,this.questionValue,this.answerText,this.phoneText,this.addressText).subscribe(
        (data)=>{
          if(data){
              this.notificationService.showSuccess("created new user","success");
              setTimeout(()=>this.spinnerService.hide(),2000)
              setTimeout(() => {this.router.navigate(['/login']);},5000);
             }
      },(err)=>{
        setTimeout(()=>this.spinnerService.hide(),2000)
        this.notificationService.showError(JSON.stringify(err.error),"error");
      });
    }
  }

}

