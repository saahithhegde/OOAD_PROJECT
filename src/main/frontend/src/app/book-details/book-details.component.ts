import { Component, OnInit } from '@angular/core';
import {Subscription} from "rxjs";
import {ActivatedRoute, Router} from '@angular/router';
import {BookServiceService} from "../services/book-service.service";
import {Ng4LoadingSpinnerService} from "ng4-loading-spinner";
import {NotificationService} from "../services/notification.service";
import {BookDto} from "../model/book.model";
import {CartServiceService} from "../services/cart-service.service";

@Component({
  selector: 'app-book-details',
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {
  private routeSub: Subscription;
  bookArray:Array<BookDto>;


  constructor(private router:Router,private route: ActivatedRoute,private bookServiceService:BookServiceService,private spinnerService:Ng4LoadingSpinnerService,private notificationService:NotificationService,private cartServiceService:CartServiceService) { }

  ngOnInit() {
    this.bookArray=new Array<BookDto>();
    this.routeSub = this.route.params.subscribe(params => {
      this.getBooksByIsbn(params['isbn'])
    });
  }

  getBooksByIsbn(isbn:string) {
    this.spinnerService.show();
    this.bookServiceService.getBookByIsbn(isbn).subscribe(
      (data) => {
        if(data.length>0) {
          this.bookArray = data;
        }
        else{
          this.router.navigate(['/dashboard'])
        }
      }, (err) => {
        setTimeout(() => this.spinnerService.hide(), 3000);
        this.notificationService.showError(JSON.stringify(err.error), "error");
      });
  }

  addToCart(book:BookDto) {
    this.spinnerService.show();
    this.cartServiceService.addToCart(book).subscribe(
      (data) => {
        if (data.email && data.bookID) {
          this.notificationService.showSuccess("successfully added to cart", "Success");
          setTimeout(() => this.spinnerService.hide(), 3000);
        } else {
          this.notificationService.showError("Failed to  add to cart", "Failed");
        }
      }, (err) => {
        setTimeout(() => this.spinnerService.hide(), 3000);
        this.notificationService.showError(JSON.stringify(err.error), "error");
      });

  }

  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }
}
