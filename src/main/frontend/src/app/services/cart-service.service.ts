import { Injectable } from '@angular/core';
import {CommonService} from "./common.service";
import {HttpClient} from "@angular/common/http";
import {BookDto} from "../model/book.model";
import {AppConstants} from "../constants/app.constants";
import {Observable} from "rxjs";
import {CartDetails, CartDto} from "../model/cart.model";

@Injectable({
  providedIn: 'root'
})
export class CartServiceService {

  constructor(private commonServices:CommonService,private httpClient:HttpClient) { }

  addToCart(book:BookDto):Observable<CartDetails>{
    var header=this.commonServices.getHeaders();
    return this.httpClient.post<CartDetails>(AppConstants.ADDTOCART,book,{headers:header});
  }

  getUserCart():Observable<CartDto>{
    var header=this.commonServices.getHeaders();
    return this.httpClient.get<CartDto>(AppConstants.GETUSERCART,{headers:header});
  }

  deleteFromCart(book:BookDto):Observable<CartDto>{
    var header=this.commonServices.getHeaders();
    return this.httpClient.post<CartDto>(AppConstants.DELETEFROMCART,book,{headers:header});
  }

}
