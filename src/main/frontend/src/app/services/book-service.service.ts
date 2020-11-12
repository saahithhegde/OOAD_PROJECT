import { BookDto } from './../model/book.model';
import { HttpClient } from '@angular/common/http';
import { CommonService } from './common.service';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppConstants } from '../constants/app.constants';

@Injectable({
  providedIn: 'root'
})
export class BookServiceService {

  constructor(private commonServices:CommonService,private httpClient:HttpClient) { }

  addBook(bookDetails:BookDto):Observable<BookDto>{
    var header=this.commonServices.getHeaders();
    return this.httpClient.post<BookDto>(AppConstants.ADDBOOK,bookDetails,{headers:header});
  }

  getUserListing():Observable<Array<BookDto>>{
    var header=this.commonServices.getHeaders();
    return this.httpClient.get<Array<BookDto>>(AppConstants.GETUSERBOOKS,{headers:header});
  }

  deleteUserListing(deleteBook:BookDto):Observable<BookDto>{
    var header=this.commonServices.getHeaders();
    return this.httpClient.post<BookDto>(AppConstants.DELETEUSERBOOK,{headers:header});
  }

  getDashBoardDetails():Observable<Array<BookDto>>{
    return this.httpClient.get<Array<BookDto>>(AppConstants.GETDASHBOOK);
  }

  getBookByIsbn(isbn:string):Observable<Array<BookDto>>{
    return this.httpClient.get<Array<BookDto>>(`/api/book/isbn/${isbn}`);
  }

}
