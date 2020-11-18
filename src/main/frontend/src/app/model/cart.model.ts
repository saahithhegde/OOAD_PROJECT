import {BookDto} from "./book.model";

export class CartDto{
  total:number;
  cartDetails:Array<CartDetails>;
}

export class CartDetails{
  email:string;
  bookID:number;
  bookDetails:BookDto;
}
