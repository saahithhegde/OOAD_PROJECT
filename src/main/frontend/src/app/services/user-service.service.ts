import { CommonService } from './common.service';
import { UserModelDto } from './../model/user.model';
import { AppConstants } from './../constants/app.constants';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenDto } from '../model/token.model';
import { ForgotModelDto } from '../model/forgotPassword.model';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {

  constructor( private httpClient:HttpClient,private commonServices:CommonService) { }

  login(email:string,password:string): Observable<TokenDto>{
   return this.httpClient.post<TokenDto>(AppConstants.LOGIN,{email,password});
  }

  register(email:string,password:string,question:string,answer:string,phoneNo:string,address:string): Observable<any>{
    return this.httpClient.post(AppConstants.CREATEUSER,{email,password,question,answer,phoneNo,address});
   }
  getUserDetails(): Observable<UserModelDto>{
    var header =this.commonServices.getHeaders();
    return this.httpClient.get<UserModelDto>(AppConstants.GETPROFILE,{headers:header});
   }
   logout(){
     this.httpClient.get(AppConstants.LOGOUT);
   }
   updateUserDetails(userDetials:UserModelDto):Observable<UserModelDto>{
     var header=this.commonServices.getHeaders();
     return this.httpClient.post<UserModelDto>(AppConstants.UPDATEPROFILE,userDetials,{headers:header});
   }
   forgotpassword(forgotPassword:ForgotModelDto): Observable<UserModelDto>{
    return this.httpClient.post<UserModelDto>(AppConstants.FORGOTPASSWORD,forgotPassword);
   }
}
