import { BookServiceService } from './../services/book-service.service';
import { BookDto } from './../model/book.model';
import { Component, OnInit } from '@angular/core';
import { NotificationService } from '../services/notification.service';
import { Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@Component({
  selector: 'app-sell-books',
  templateUrl: './sell-books.component.html',
  styleUrls: ['./sell-books.component.css']
})
export class SellBooksComponent implements OnInit {
   addBookDto:BookDto
   message:string;
   userBooksArray:Array<BookDto>;

  constructor(private spinnerService: Ng4LoadingSpinnerService,private notificationService:NotificationService,private bookServiceService:BookServiceService) { }

  ngOnInit() {
    this.addBookDto=new BookDto();
    this.getUserBooks();


  }

  getUserBooks(){
    this.spinnerService.show()
    this.userBooksArray=new Array<BookDto>();
    this.bookServiceService.getUserListing().subscribe(
      (data)=>{
        if(data){
            this.userBooksArray=data;
            setTimeout(()=>this.spinnerService.hide(),2000)
           }
    },(err)=>{
      this.notificationService.showError(JSON.stringify(err.error),"error");
      setTimeout(()=>this.spinnerService.hide(),2000)
    });

  }

  addBook(){
    this.spinnerService.show();
    this.bookServiceService.addBook(this.addBookDto).subscribe(
      (data)=>{
        if(data){
            this.getUserBooks();
            this.notificationService.showSuccess("New book Added","success");
            this.addBookDto=new BookDto();
            setTimeout(()=>this.spinnerService.hide(),2000)
           }
    },(err)=>{
      this.notificationService.showError(JSON.stringify(err.error),"error");
      setTimeout(()=>this.spinnerService.hide(),2000)
    });
  }

    deleteBook(){

    }
}
