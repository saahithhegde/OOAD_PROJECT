import { UserModelDto } from './../model/user.model';
import { UserServiceService } from './../services/user-service.service';
import { Component, OnInit } from '@angular/core';

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
  constructor(private userServiceService:UserServiceService) { }

  ngOnInit() {
    this.getUserDetails();
  }

  getUserDetails(){

      this.userServiceService.getUserDetails().subscribe(
        (data)=>{
          if(data){
              this.userDetails=data;
              this.modifiedUserDetails=data;
             }
      },(err)=>{
        this.message=(JSON.stringify(err.error));
      });
  }

  updateUserDetails(){
    this.userServiceService.updateUserDetails(this.modifiedUserDetails).subscribe(
      (data)=>{
        if(data){
          this.userDetails=data;
          this.modifiedUserDetails=data
          this.editMessage="Updated Details Sucessfully";
        }
    },(err)=>{
      this.editMessage=(JSON.stringify(err.error));
    });
  }

  onEdit(){
    this.formEdit=true;
  }

  onEditclose(){
    this.formEdit=false;
  }

}
