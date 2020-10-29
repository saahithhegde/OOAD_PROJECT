import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class CommonService {

  constructor() { }

  getHeaders(){
      var tokenValue=sessionStorage.getItem("token")
      const headers= new HttpHeaders()
      .set('Token', tokenValue)
      .set('Content-Type', 'application/json');
      return headers
  }
}
