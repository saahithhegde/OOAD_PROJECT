import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {BookServiceService} from "../services/book-service.service";
import {Ng4LoadingSpinnerService} from "ng4-loading-spinner";
import {NotificationService} from "../services/notification.service";
import {CartServiceService} from "../services/cart-service.service";
import {CartDto} from "../model/cart.model";
import {BookDto} from "../model/book.model";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cartArray:Array<CartDto>;

  constructor(private bookServiceService:BookServiceService,private spinnerService:Ng4LoadingSpinnerService,private notificationService:NotificationService,private cartServiceService:CartServiceService) { }

  ngOnInit() {
    this.cartArray=new Array<CartDto>();
    this.getUserCartDetails();
  }

  getUserCartDetails() {
    this.cartServiceService.getUserCart().subscribe(
      (data) => {
        if (data) {
          this.cartArray=data;
          setTimeout(() => this.spinnerService.hide(), 3000);
        } else {
          this.notificationService.showWarning("Empty Cart", "No Items");
        }
      }, (err) => {
        setTimeout(() => this.spinnerService.hide(), 3000);
        this.notificationService.showError(JSON.stringify(err.error), "error");
      });
  }


  removeFromCart(bookDetails: BookDto) {
    this.cartServiceService.deleteFromCart(bookDetails).subscribe(
      (data) => {
        if (data.bookID && data.email) {
          setTimeout(() => this.spinnerService.hide(), 3000);
          this.notificationService.showWarning("Deleted entry", "Deleted");
          this.removeBook(data);
        }
      }, (err) => {
        setTimeout(() => this.spinnerService.hide(), 3000);
        this.notificationService.showError(JSON.stringify(err.error), "error");
      });

  }
  removeBook(book:CartDto){
    this.cartArray.forEach( (item, index) => {
      if(item.bookID === book.bookID) this.cartArray.splice(index,1);
    });
  }
}

