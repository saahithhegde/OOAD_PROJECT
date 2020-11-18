import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppConstants } from '../constants/app.constants';
import { BookDto } from '../model/book.model';
import { OrderParentDto } from '../model/orderParent.model';
import { CommonService } from './common.service';

@Injectable({
  providedIn: 'root'
})
export class OrderServiceService {

  constructor(private commonServices:CommonService,private httpClient:HttpClient) { }

  getUserOrders():Observable<Array<OrderParentDto>>{
    var header=this.commonServices.getHeaders();
    return this.httpClient.get<Array<OrderParentDto>>(AppConstants.GETUSERORDERDETAILS,{headers:header});
  } 

  getUserSales():Observable<Array<OrderParentDto>>{
    var header=this.commonServices.getHeaders();
    return this.httpClient.get<Array<OrderParentDto>>(AppConstants.GETUSERPURCHASEDETAILS,{headers:header});
  } 
}
