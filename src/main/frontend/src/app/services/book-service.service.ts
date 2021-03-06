import { BookDto } from './../model/book.model';
import {HttpClient, HttpHeaders, HttpRequest} from '@angular/common/http';
import { CommonService } from './common.service';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppConstants } from '../constants/app.constants';

@Injectable({
  providedIn: 'root'
})
export class BookServiceService {

  constructor(private commonServices:CommonService,private httpClient:HttpClient) { }

  addBook(bookDetails:BookDto,file:any):Observable<BookDto>{
    var tokenValue=sessionStorage.getItem("token")
    const headers= new HttpHeaders().set('Token', tokenValue)
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('book',JSON.stringify(bookDetails));
    return this.httpClient.post<BookDto>(AppConstants.ADDBOOK,formData,{headers:headers});
  }

  getUserListing():Observable<Array<BookDto>>{
    var header=this.commonServices.getHeaders();
    return this.httpClient.get<Array<BookDto>>(AppConstants.GETUSERBOOKS,{headers:header});
  }

  deleteUserListing(deleteBook:BookDto):Observable<BookDto>{
    var header=this.commonServices.getHeaders();
    return this.httpClient.post<BookDto>(AppConstants.DELETEUSERBOOK,deleteBook,{headers:header});
  }

  getDashBoardDetails():Observable<Array<BookDto>>{
    return this.httpClient.get<Array<BookDto>>(AppConstants.GETDASHBOOK);
  }

  getBookByIsbn(isbn:string):Observable<Array<BookDto>>{
    return this.httpClient.get<Array<BookDto>>(`/api/book/isbn/${isbn}`);
  }

  searchBooks(keyword:string):Observable<Array<BookDto>>{
    return this.httpClient.get<Array<BookDto>>(`/api/book/search/${keyword}`);
  }

  searchBooksByCategory(category:string):Observable<Array<BookDto>>{
    return this.httpClient.get<Array<BookDto>>(`/api/book/category/${category}`);
  }

}
