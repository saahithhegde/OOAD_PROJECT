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
  defaultBookArray:Array<BookDto>
  searchField: string;
  searchCategory:string;

  constructor(private bookServiceService:BookServiceService,private spinnerService:Ng4LoadingSpinnerService,private notificationService:NotificationService,private router:Router) { }

  ngOnInit() {
    this.getDashboard();
  }

  getDashboard(){
    this.spinnerService.show();
    this.bookServiceService.getDashBoardDetails().subscribe(
      (data)=>{
        this.dashBoardBookArray=data;
        this.defaultBookArray=data;
        console.log(this.defaultBookArray);
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


    searchByCategory($event:any){
    if(this.searchCategory=="none"){
      this.dashBoardBookArray=this.defaultBookArray;
    }
    else{
      this.spinnerService.show();
      this.bookServiceService.searchBooksByCategory(this.searchCategory).subscribe(
        (data)=>{
          if(data.length==0){
            this.notificationService.showError("No Book Found","Not Found");
          }
          this.dashBoardBookArray=data;
          this.spinnerService.hide();
        },
        (err) => {
          this.notificationService.showError(JSON.stringify(err.error),"error");
          this.spinnerService.hide();
        });
    }
  }

  search($event: any) {
    if(this.searchField==""){
      this.dashBoardBookArray=this.defaultBookArray;
    }
    else{
      this.spinnerService.show();
      this.bookServiceService.searchBooks(this.searchField).subscribe(
        (data)=>{
          if(data.length==0){
            this.notificationService.showError("No Book Found","Not Found");
          }
          this.dashBoardBookArray=data;
          this.spinnerService.hide();
          },
        (err) => {
          this.notificationService.showError(JSON.stringify(err.error),"error");
          this.spinnerService.hide();
        });
    }

  }
}
