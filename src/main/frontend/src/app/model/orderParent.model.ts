
export class OrderParentDto{
    orders: OrdersDto;
    orderDetails: Array<OrderDetailsDto>;
}

export class OrderDetailsDto{
    uid:Number;
    orderID:Number;
    buyer:string;
    seller:string;
    bookID:Number;
    isbn:Number;
    title:string;
    author:string;
    price:Number;
    category:string;
    image:string;
}

export class OrdersDto{
    orderID:Number;
    buyerID:Number;
    orderDate:Date;
    total:Number;
    paymentType:string;
}