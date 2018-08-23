package controllers

import javax.inject._
import model.Email
import model.User
import play.api._
import play.api.mvc._
import service.EmailServiceComponent
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import java.util.Date


trait EmailController extends Controller {
  self: EmailServiceComponent =>0

  implicit val writer = new Writes[Email] {
    def writes(email: Email): JsValue = {
      Json.obj(
        "sender" -> email.sender,
        "receiver" -> email.receiver,
        "subject" -> email.subject,
        "message" -> email.message
      )
    }
  }

  implicit val emailReads: Reads[Email] = (
      (__ \ "sender").read[String] and
      (__ \ "receiver").read[String] and
      (__ \ "subject").read[String] and
      (__ \ "message").read[String]
    )(Email.apply _)

  def GetMessages(username: String) = Action {
    val messages = emailService.getUsersMessages(username)
    Ok(Json.toJson(messages))
  }

  def SendEmailAndStore = Action(parse.json) {request =>
    val emailResult = request.body.validate[Email]
      emailResult.fold(
      errors => {
        BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toJson(errors)))
      },
        email => {
          emailService.sendMessage(email)
          Created
      }
    )
  }
}
