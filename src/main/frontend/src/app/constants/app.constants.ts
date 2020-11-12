export class AppConstants{
  //user apis
  public static readonly LOGIN="/api/users/login";
  public static readonly CREATEUSER="/api/users/createUser";
  public static readonly LOGOUT="/api/users/login";
  public static readonly GETPROFILE="/api/users/getProfile";
  public static readonly UPDATEPROFILE="/api/users/updateProfile";
  public static readonly FORGOTPASSWORD="/api/users/forgotPassword";

  //book apis
  public static readonly ADDBOOK="/api/book/add";
  public static readonly GETUSERBOOKS="/api/book/seller/books";
  public static readonly DELETEUSERBOOK="/api/book/seller/books";
  public static readonly GETDASHBOOK="/api/book/getDistinctIsbn";

  //cart api
  public static readonly ADDTOCART="api/cart/add";
  public static readonly GETUSERCART="api/cart/getUserCart";

}
