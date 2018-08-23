package model

import java.util.Date

class Email( i :Int, s: String, r: String, sub: String, mes: String, d: Date){
  var id: Int = i
  var sender: String = s
  var receiver: String = r
  var subject: String = sub
  var message: String = mes
  var date: Date = d

}
//object EmailItem{
//  def main(args: Array[String]) {
//    val email = new Email(1, "s","r","mes", "d", Calendar.getInstance().getTime())
//
//  }
//}


