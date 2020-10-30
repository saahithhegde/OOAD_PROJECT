import { Router } from '@angular/router';
import { UserServiceService } from './../services/user-service.service';
import { Component, OnInit } from '@angular/core';
import { Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { NotificationService } from '../services/notification.service';
import { ForgotModelDto } from '../model/forgotPassword.model';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {
  forgotPasswordDto:ForgotModelDto;
  confirmPasswordText:string;

  constructor(private notificationService:NotificationService,private userServiceService:UserServiceService,private router:Router,private spinnerService: Ng4LoadingSpinnerService) { }

  ngOnInit() {
    this.forgotPasswordDto=new ForgotModelDto();
  }

  cancel(){
    this.spinnerService.show();
    this.router.navigate(['/login']);
    setTimeout(()=>this.spinnerService.hide(),2000)
  }

  forgotpassword(){
    if(this.forgotPasswordDto){
      this.spinnerService.show()
      this.userServiceService.forgotpassword(this.forgotPasswordDto).subscribe(
        (data)=>{
          if(data.email){
              this.forgotPasswordDto=new ForgotModelDto();
              this.notificationService.showSuccess("Successfully changed password","success");
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
