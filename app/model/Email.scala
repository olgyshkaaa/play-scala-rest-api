package model

import java.util.Date

case class Email(Id: Int, s: String, r: String, sub: String, mes: String, date : Option[Date]){
  var id: Int = Id
  var sender: String = s
  var receiver: String = r
  var subject: String = sub
  var message: String = mes
  var sentdate: Option[Date] = date
}



