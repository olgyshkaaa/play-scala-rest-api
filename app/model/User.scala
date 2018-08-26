package model


case class User (
  Id: Int,
  Name: String,
  Email: String,
  Password: String
           )
{
  var id: Int = Id
  var name: String = Name
  var email: String = Email
  var password: String = Password
}


