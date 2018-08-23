package model

import java.util.Date

case class Email(s: String, r: String, sub: String, mes: String){
  var sender: String = s
  var receiver: String = r
  var subject: String = sub
  var message: String = mes
}
//object EmailItem{
//  def main(args: Array[String]) {
//    val email = new Email(1, "s","r","mes", "d", Calendar.getInstance().getTime())
//
//  }
//}


