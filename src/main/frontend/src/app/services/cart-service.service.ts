import { Injectable } from '@angular/core';
import {CommonService} from "./common.service";
import {HttpClient} from "@angular/common/http";
import {BookDto} from "../model/book.model";
import {AppConstants} from "../constants/app.constants";

@Injectable({
  providedIn: 'root'
})
export class CartServiceService {

  constructor(private commonServices:CommonService,private httpClient:HttpClient) { }

  addToCart(book:BookDto){
    var header=this.commonServices.getHeaders();
    return this.httpClient.post(AppConstants.ADDTOCART,book,{headers:header});
  }

  getUserCart(){
    var header=this.commonServices.getHeaders();
    return this.httpClient.get(AppConstants.GETUSERCART,{headers:header});
  }

  deleteFromCart(){

  }
}
