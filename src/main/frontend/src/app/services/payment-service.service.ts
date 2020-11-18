import { Injectable } from '@angular/core';
import {CommonService} from "./common.service";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import { UserPaymentTypes } from '../model/user-payment-types.model';
import {AppConstants} from "../constants/app.constants";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class PaymentServiceService {

  constructor(private commonServices:CommonService,private httpClient:HttpClient) { }

  checkoutFromCart(paymentType:string):Observable<any>{
    var tokenValue=sessionStorage.getItem("token")
    const header= new HttpHeaders()
      .set('Token', tokenValue)
      .set('Content-Type', 'application/json')
      .set('paymentType',paymentType);
    return this.httpClient.get<any>(AppConstants.CHECKOUT,{headers:header})
  }

  addUserPaymentTypes(userPaymentTypes:UserPaymentTypes):Observable<UserPaymentTypes>{
    var header=this.commonServices.getHeaders();
    return this.httpClient.post<UserPaymentTypes>(AppConstants.ADDPAYMENTTYPES,userPaymentTypes,{headers:header})
  }

  deleteUserPaymentTypes(userPaymentTypes:UserPaymentTypes):Observable<UserPaymentTypes>{
    var header=this.commonServices.getHeaders();
    return this.httpClient.post<UserPaymentTypes>(AppConstants.DELETEPAYMENTTYPES,userPaymentTypes,{headers:header})
  }

  getUserPaymentTypes():Observable<Array<UserPaymentTypes>>{
    var header=this.commonServices.getHeaders();
    return this.httpClient.get<Array<UserPaymentTypes>>(AppConstants.GETPAYMENTTYPES,{headers:header})
  }
}
