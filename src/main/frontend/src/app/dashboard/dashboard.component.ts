import { NotificationService } from './../services/notification.service';
import { Component, OnInit } from '@angular/core';
import { Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { BookDto } from '../model/book.model';
import { BookServiceService } from '../services/book-service.service';
import {Router} from "@angular/router";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  dashBoardBookArray:Array<BookDto>

  constructor(private bookServiceService:BookServiceService,private spinnerService:Ng4LoadingSpinnerService,private notificationService:NotificationService,private router:Router) { }

  ngOnInit() {
    this.getDashboard();
  }

  getDashboard(){
    this.spinnerService.show();
    this.bookServiceService.getDashBoardDetails().subscribe(
      (data)=>{
        this.dashBoardBookArray=data;
        setTimeout(()=>this.spinnerService.hide(),3000);
      },
      (err)=>{
        setTimeout(()=>this.spinnerService.hide(),3000);
        this.notificationService.showError(JSON.stringify(err.error),"error");
      }
    )}

    redirectToBook(isbn:string){
      this.spinnerService.show();
      this.router.navigate(['/bookdetails/', isbn]);
      setTimeout(()=>this.spinnerService.hide(),3000)

    }


}
