package controllers

import javax.inject._
import model.UserCollection
import play.api._
import play.api.mvc._
import service.EmailServiceComponent
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._
import model.Email

trait EmailController extends Controller {
  self: EmailServiceComponent =>0

  implicit val writer = new Writes[Email] {
    def writes(email: Email): JsValue = {
      Json.obj(
        "sender" -> email.sender,
        "receiver" -> email.receiver,
        "subject" -> email.subject,
        "message" -> email.message,
        "date" -> email.date.toString()
      )
    }
  }
  def GetMessages(username: String) = Action {
    val messages = emailService.getUsersMessages(username)
    Ok(Json.toJson(messages))
  }
}
