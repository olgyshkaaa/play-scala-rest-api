package model

import java.util.Calendar

import model.User
import model.Email
import java.util.concurrent.ConcurrentHashMap

import org.joda.time.DateTime

import scala.collection.mutable.ListBuffer


class UserCollection
{
  val Alice = new User(1, "Alice", "airoboy@mail.ru", ListBuffer(new Email(1, "Alice", "Bob", "Hello", "HelloWorld", Calendar.getInstance().getTime())))
  val Bob = new User(2, "Bob", "kabirov.airat@inbox.ru", ListBuffer())
  val Eve = new User(3, "Eve", "olgyshkaaa@gmail.com", ListBuffer())

  val users = List(Alice, Bob, Eve)
//  val emails = List()
}
