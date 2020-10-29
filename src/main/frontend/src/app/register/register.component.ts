import { Router } from '@angular/router';
import { UserServiceService } from './../services/user-service.service';
import { Component, OnInit } from '@angular/core';

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


  constructor(private userServiceService:UserServiceService,private router:Router) { }

  ngOnInit() {
  }

  cancel(){
    this.router.navigate(['/login']);
  }

  register(){
    if(this.emailText && this.passwordText && this.questionValue && this.answerText){
      this.userServiceService.register(this.emailText,this.passwordText,this.questionValue,this.answerText,this.phoneText,this.addressText).subscribe(
        (data)=>{
          if(data){
              this.message="User Created Successfully"
              setTimeout(() => {this.router.navigate(['/login']);},5000);
             }
      },(err)=>{
        this.message=JSON.stringify(err.error);
      });
    }
  }

}

