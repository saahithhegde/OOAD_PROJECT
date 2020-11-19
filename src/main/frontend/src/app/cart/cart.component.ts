import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {BookServiceService} from "../services/book-service.service";
import {Ng4LoadingSpinnerService} from "ng4-loading-spinner";
import {NotificationService} from "../services/notification.service";
import {CartServiceService} from "../services/cart-service.service";
import {CartDetails, CartDto} from "../model/cart.model";
import {BookDto} from "../model/book.model";
import {UserPaymentTypes} from "../model/user-payment-types.model";
import {PaymentServiceService} from "../services/payment-service.service";
import {PdfService} from "../services/pdf.service";
import {OrderParentDto} from "../model/orderParent.model";

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cartArray: CartDto
  paymentType: string;
  userPaymentDetailsArray:Array<UserPaymentTypes>;
  orderParentOrder: OrderParentDto

  constructor(private pdfservice:PdfService,private router:Router,private bookServiceService: BookServiceService, private spinnerService: Ng4LoadingSpinnerService, private notificationService: NotificationService, private cartServiceService: CartServiceService,private paymentService:PaymentServiceService) {
  }

  ngOnInit() {
    this.orderParentOrder = new OrderParentDto();
    this.userPaymentDetailsArray=new Array<UserPaymentTypes>();
    this.getUserCartDetails();
    this.getUserPaymentDetails();
  }

  getUserCartDetails() {
    this.cartArray = new CartDto();
    this.cartServiceService.getUserCart().subscribe(
      (data) => {
        if (data.cartDetails.length>0) {
          this.cartArray = data
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
        if (data) {
          setTimeout(() => this.spinnerService.hide(), 3000);
          this.notificationService.showWarning("Deleted entry", "Deleted");
          this.getUserCartDetails();
        }
      }, (err) => {
        setTimeout(() => this.spinnerService.hide(), 3000);
        this.notificationService.showError(JSON.stringify(err.error), "error");
      });
  }
  getUserPaymentDetails(){
    this.spinnerService.show();
    this.paymentService.getUserPaymentTypes().subscribe(
      (data)=>{
        if(data.length>0){
          this.userPaymentDetailsArray=data;
          setTimeout(()=>this.spinnerService.hide(),2000)
        }
        else{
          this.notificationService.showWarning("No Card Details", "Register Payment Types");
          this.spinnerService.show();
          setTimeout(() => {this.router.navigate(['/user']);},5000);
          this.notificationService.showSuccess("Enter Card Details", "Add Details");
          setTimeout(() => this.spinnerService.hide(), 3000);
        }
      },(err)=>{
        this.notificationService.showError(JSON.stringify(err.error),"error");
        setTimeout(()=>this.spinnerService.hide(),2000);
      });
  }
  checkout(){
    this.spinnerService.show();
    this.paymentService.checkoutFromCart(this.paymentType).subscribe(
      (data)=>{
        this.orderParentOrder=data;
        console.log(this.orderParentOrder)
        this.notificationService.showSuccess("Successfully Bought Books", "Success");
        this.getUserCartDetails();
        setTimeout(()=>this.spinnerService.hide(),2000);
    },(err)=>{
        if(err.status===422){
          this.notificationService.showWarning("Few Items not Available refreshing cart", "Failed");
          this.getUserCartDetails();
          setTimeout(() => this.spinnerService.hide(), 2000);
        }
        else {
          this.notificationService.showError(JSON.stringify(err.error), "error");
          setTimeout(() => this.spinnerService.hide(), 2000);
        }
      });
  }

  generatepdf(){
    var data = document.getElementById('contentToConvert');
    this.pdfservice.captureScreen(data);
  }
}

