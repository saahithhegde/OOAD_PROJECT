import { UserModelDto } from './../model/user.model';
import { UserServiceService } from './../services/user-service.service';
import { Component, OnInit } from '@angular/core';
import { Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { NotificationService } from '../services/notification.service';
import {PaymentServiceService} from "../services/payment-service.service";
import {UserPaymentTypes} from "../model/user-payment-types.model";

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
    cardEdit: boolean=false;
    userPaymentDetails:UserPaymentTypes;
    userPaymentDetailsArray:Array<UserPaymentTypes>;
  constructor(private notificationService:NotificationService,private userServiceService:UserServiceService,private spinnerService: Ng4LoadingSpinnerService,private paymentService:PaymentServiceService) { }

  ngOnInit() {
    this.userPaymentDetails=new UserPaymentTypes();
    this.userPaymentDetailsArray=new Array<UserPaymentTypes>();
    this.getUserDetails();
    this.getUserPaymentDetails();
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
      setTimeout(()=>this.spinnerService.hide(),2000);
      this.onEditclose();
    });
  }

  addUserPaymentDetails(){
    this.spinnerService.show();
    this.paymentService.addUserPaymentTypes(this.userPaymentDetails).subscribe(
      (data)=>{
        if(data){
          this.userPaymentDetailsArray.push(data)
          this.notificationService.showSuccess("added user card","success");
          setTimeout(()=>this.spinnerService.hide(),2000);
          this.userPaymentDetails=new UserPaymentTypes();
          this.onCardClose();
        }
      },(err)=>{
        this.notificationService.showError(JSON.stringify(err.error),"error");
        setTimeout(()=>this.spinnerService.hide(),2000);
        this.onCardClose();
      });
  }

  deleteUserPaymentDetails(userPaymentDetails:UserPaymentTypes){
    this.spinnerService.show();
    this.paymentService.deleteUserPaymentTypes(userPaymentDetails).subscribe(
      (data)=>{
        if(data){
          this.getUserPaymentDetails();
          setTimeout(()=>this.spinnerService.hide(),2000)
          this.notificationService.showWarning("deleted user card","success");
        }
      },(err)=>{
        this.notificationService.showError(JSON.stringify(err.error),"error");
        setTimeout(()=>this.spinnerService.hide(),2000);
      });
  }

  getUserPaymentDetails(){
    this.spinnerService.show();
    this.paymentService.getUserPaymentTypes().subscribe(
      (data)=>{
        if(data){
          this.userPaymentDetailsArray=data;
          setTimeout(()=>this.spinnerService.hide(),2000)
        }
      },(err)=>{
        this.notificationService.showError(JSON.stringify(err.error),"error");
        setTimeout(()=>this.spinnerService.hide(),2000);
      });
  }

  onEdit(){
    this.formEdit=true;
  }

  onEditclose(){
    this.formEdit=false;
  }

  onCardEdit(){
    this.cardEdit=true;
  }

  onCardClose(){
    this.cardEdit=false;
  }

}
