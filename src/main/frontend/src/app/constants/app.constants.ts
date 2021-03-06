export class AppConstants{
  //user apis
  public static readonly LOGIN="/api/users/login";
  public static readonly CREATEUSER="/api/users/createUser";
  public static readonly LOGOUT="/api/users/logout";
  public static readonly GETPROFILE="/api/users/getProfile";
  public static readonly UPDATEPROFILE="/api/users/updateProfile";
  public static readonly FORGOTPASSWORD="/api/users/forgotPassword";
  public static readonly GETUSERORDERDETAILS="/api/users/buyHistory";
  public static readonly GETUSERPURCHASEDETAILS="/api/users/sellHistory";
  

  //book apis
  public static readonly ADDBOOK="/api/book/add";
  public static readonly GETUSERBOOKS="/api/book/seller/books";
  public static readonly DELETEUSERBOOK="/api/book/deleteListing";
  public static readonly GETDASHBOOK="/api/book/getDistinctIsbn";

  //cart api
  public static readonly ADDTOCART="api/cart/add";
  public static readonly GETUSERCART="api/cart/getUserCart";
  public static readonly DELETEFROMCART="api/cart/delete";
  

  //payment api checkout
  public static readonly CHECKOUT= "api/payment/submit";

  //payment api
  public static readonly ADDPAYMENTTYPES="api/users/add";
  public static readonly DELETEPAYMENTTYPES="api/users/delete";
  public static readonly GETPAYMENTTYPES="api/users/getUserPaymentTypes";

}
