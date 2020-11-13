import { UserModelDto } from './../model/user.model';
import { UserServiceService } from './../services/user-service.service';
import { Component, OnInit } from '@angular/core';
import { Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { NotificationService } from '../services/notification.service';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {
    userDetails=new UserModelDto();
    modifiedUserDetails=new UserModelDto();
    formEdit:boolean=false;
    message:string;
    editMessage:string;
    confirmPasswordText:string;
    xyz
  constructor(private notificationService:NotificationService,private userServiceService:UserServiceService,private spinnerService: Ng4LoadingSpinnerService ) { }

  ngOnInit() {
    this.getUserDetails();
  }

  getUserDetails(){
    this.spinnerService.show();
      this.userServiceService.getUserDetails().subscribe(
        (data)=>{
          if(data){
              this.userDetails=data;
              this.modifiedUserDetails=data;
              setTimeout(()=>this.spinnerService.hide(),2000)
             }
      },(err)=>{
        this.notificationService.showError(JSON.stringify(err.error),"error");
        setTimeout(()=>this.spinnerService.hide(),2000)
      });
  }

  updateUserDetails(){
    this.spinnerService.show();
    this.userServiceService.updateUserDetails(this.modifiedUserDetails).subscribe(
      (data)=>{
        if(data){
          this.userDetails=data;
          this.modifiedUserDetails=data
          this.notificationService.showSuccess("updated user","success");
          setTimeout(()=>this.spinnerService.hide(),2000)
          this.onEditclose();
        }
    },(err)=>{
      this.notificationService.showError(JSON.stringify(err.error),"error");
      setTimeout(()=>this.spinnerService.hide(),2000)
    });
  }

  onEdit(){
    this.formEdit=true;
  }

  onEditclose(){
    this.formEdit=false;
  }

}
