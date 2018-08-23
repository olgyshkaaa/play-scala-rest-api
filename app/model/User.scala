package model

import java.util.Date

import akka.http.scaladsl.model.DateTime

import scala.collection.mutable.ListBuffer

case class User (
  Id: Int,
  Name: String,
  Email: String,
  Messages: ListBuffer[Email]
           )
{
  var id: Int = Id
  var name: String = Name
  var email: String = Email
  var messages: ListBuffer[Email] = Messages
}


