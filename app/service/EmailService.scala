package service

import java.util.Calendar
import model.User
import model.Email
import model.UserCollection
import java.util.concurrent.ConcurrentHashMap
import javax.mail._
import javax.mail.internet._
import java.util._

import scala.collection.mutable.ListBuffer

trait EmailServiceComponent {

  val userCollection: UserCollection
  val emailService: EmailService

  trait EmailService {

    def sendMessage(email: Email)

    def getUsersMessages(username: String) : ListBuffer[Email]
  }
}

trait EmailServiceComponentImpl extends EmailServiceComponent {

  override val userCollection = new UserCollection {}
  override val emailService = new EmailServiceImpl

  class EmailServiceImpl extends EmailService {

    override def sendMessage(email: Email) {
      val sender = userCollection.users.find(_.name == email.sender)
      val senderEmail = sender.get.email
      val receiverEmail = userCollection.users.find(_.name == email.receiver).get.email

      val properties = new Properties()
      properties.put("mail.smtp.host", "smtp.mail.ru")
      properties.put("mail.smtp.starttls.enable","true")
      val session = Session.getDefaultInstance(properties)
      val message = new MimeMessage(session)
      message.setFrom(s"<$senderEmail>")
      message.addRecipients(Message.RecipientType.TO,
        s"<$receiverEmail>")
      message.setSubject(email.subject)
      message.setText(email.message)
      Transport.send(message, senderEmail, "njkcnsqckjy")

      sender.get.Messages += email

    }

    override def getUsersMessages(username: String): ListBuffer[Email] = {
      var user = userCollection.users.find(_.name == username)
      return user.get.Messages
    }
  }
}
