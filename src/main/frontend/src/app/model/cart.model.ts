import {BookDto} from "./book.model";

export class CartDto{
  email:string;
  bookID:number;
  bookDetails:BookDto;
}
