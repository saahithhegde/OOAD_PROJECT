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

  deleteBook(deleteListing:BookDto){
    this.spinnerService.show();
    this.bookServiceService.deleteUserListing(deleteListing).subscribe(
      (data) => {
        if (data.isbn) {
          setTimeout(() => this.spinnerService.hide(), 3000);
          this.notificationService.showWarning("Deleted Listing", "Deleted");
          this.removeListing(data);
        }
      }, (err) => {
        setTimeout(() => this.spinnerService.hide(), 3000);
        this.notificationService.showError(JSON.stringify(err.error), "error");
      });


  }
  removeListing(book:BookDto){
    this.userBooksArray.forEach( (item, index) => {
      if(item.bookID === book.bookID) this.userBooksArray.splice(index,1);
    });
  }

}
