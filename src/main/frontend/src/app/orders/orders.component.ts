import { Component, OnInit } from '@angular/core';
import { Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { OrderParentDto } from '../model/orderParent.model';
import { NotificationService } from '../services/notification.service';
import { OrderServiceService } from '../services/order-service.service';
import {PdfService} from "../services/pdf.service";

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {
  //orderArray:Array<OrdersDto>
  //orderDetailsArray:Array<OrderDetailsDto>
  orderParentOrder: Array<OrderParentDto>
  orderParentSales: Array<OrderParentDto>

  constructor(private pdfservice:PdfService,private spinnerService: Ng4LoadingSpinnerService,private notificationService:NotificationService, private orderServiceService: OrderServiceService) { }

  orderHeadElements = ['OrderID', 'Order Date', 'Payment Type', 'Total Cost', 'Order Details','Invoice'];
  salesHeadElements = ['Book ISBN', 'Sale Date', 'Buyer', 'Payment Type', 'Total Price'];

  ngOnInit() {

      this.getUserOrderDetails();
      this.getUserSaleDetails();
  }

  getUserOrderDetails(){
    this.spinnerService.show()
    this.orderParentOrder = new Array<OrderParentDto>();
    this.orderServiceService.getUserOrders().subscribe(
      (data)=>{
        if(data){
          this.orderParentOrder = data;
          setTimeout(()=>this.spinnerService.hide(),2000)
        }
      },(err)=>{
        this.notificationService.showError(JSON.stringify(err.error),"error");
        setTimeout(()=>this.spinnerService.hide(),2000)
      });
  }

  getUserSaleDetails(){
    this.spinnerService.show()
    this.orderParentSales = new Array<OrderParentDto>();
    this.orderServiceService.getUserSales().subscribe(
      (data)=>{
        if(data){
          this.orderParentSales = data;
          setTimeout(()=>this.spinnerService.hide(),2000)
        }
      },(err)=>{
        this.notificationService.showError(JSON.stringify(err.error),"error");
        setTimeout(()=>this.spinnerService.hide(),2000)
      });
  }

  generateInvoice(orderID: any) {
    var data = document.getElementById(orderID);
    this.pdfservice.captureScreen(data);
  }
}
