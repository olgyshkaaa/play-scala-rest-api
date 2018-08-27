package controllers

import model.Email
import play.api.mvc._
import service.{EmailService}
import play.api.libs.json._
import play.api.libs.json.Writes._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import java.util.Date

import javax.inject.{Inject, Singleton}


trait EmailController  extends InjectedController  {

  implicit val writer = new Writes[Email] {
    def writes(email: Email): JsValue = {
      Json.obj(
        "id" -> email.id,
        "sender" -> email.sender,
        "receiver" -> email.receiver,
        "subject" -> email.subject,
        "message" -> email.message,
        "sentdate" -> email.sentdate
      )
    }
  }

  implicit val emailReads: Reads[Email] = (
    (__ \ "id").read[Int] and
      (__ \ "sender").read[String] and
      (__ \ "receiver").read[String] and
      (__ \ "subject").read[String] and
      (__ \ "message").read[String] and
      (__ \ "sentdate").readNullable[Date]
    )(Email.apply _)

  def getMessages(username: String):Action[AnyContent]

  def sendEmailAndStore:Action[JsValue]
}

@Singleton
class EmailControllerImpl @Inject()(emailService: EmailService)  extends EmailController
{

  def index = Action {
    Ok(views.html.index())
  }

  override  def getMessages(username: String) = Action {
    val messages = emailService.getUsersMessages(username)
    Ok(Json.toJson(messages))
  }

  override def sendEmailAndStore = Action(parse.json) { request =>
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
