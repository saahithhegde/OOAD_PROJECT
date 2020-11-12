import { Component, OnInit } from '@angular/core';
import {Subscription} from "rxjs";
import { ActivatedRoute } from '@angular/router';
import {BookServiceService} from "../services/book-service.service";
import {Ng4LoadingSpinnerService} from "ng4-loading-spinner";
import {NotificationService} from "../services/notification.service";
import {BookDto} from "../model/book.model";

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {
  private routeSub: Subscription;
  bookArray:Array<BookDto>;


  constructor(private route: ActivatedRoute,private bookServiceService:BookServiceService,private spinnerService:Ng4LoadingSpinnerService,private notificationService:NotificationService) { }

  ngOnInit() {
    this.bookArray=new Array<BookDto>();
    this.routeSub = this.route.params.subscribe(params => {
      this.getBooksByIsbn(params['isbn'])
    });
  }

  getBooksByIsbn(isbn:string) {
    console.log(isbn);
    this.bookServiceService.getBookByIsbn(isbn).subscribe(
      (data) => {
        this.bookArray=data;
      }, (err) => {
        setTimeout(() => this.spinnerService.hide(), 3000);
        this.notificationService.showError(JSON.stringify(err.error), "error");
      });
  }

  addToCart(book:BookDto){


  }


  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }
}
