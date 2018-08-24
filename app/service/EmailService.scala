package service

import java.util.Calendar
import model.User
import model.Email
import model.UserCollection
import java.util.concurrent.ConcurrentHashMap
import javax.mail._
import javax.mail.internet._
import scalikejdbc._

import scala.collection.mutable.ListBuffer

trait EmailServiceComponent {

  val userCollection: UserCollection
  val emailService: EmailService

  trait EmailService {

    def sendMessage(email: Email)

    def getUsersMessages(username: String) : List[Email]
  }
}

trait EmailServiceComponentImpl extends EmailServiceComponent {

  override val userCollection = new UserCollection {}
  override val emailService = new EmailServiceImpl
  implicit val session = AutoSession

  class EmailServiceImpl extends EmailService {

    override def sendMessage(email: Email) {
      val sender = sql"select * from users where name = ${email.sender}"
        .map(u => new User(u.int("id"), u.string("name"), u.string("email"), u.string("password")))
        .single
        .apply()
      val receiverEmail = sql"select email from users where name = ${email.receiver}"
        .map(u => u.string("email"))
        .single
        .apply()
      val senderEmail = sender.get.email
      val properties = new java.util.Properties()
      properties.put("mail.smtp.host", "smtp.mail.ru")
      properties.put("mail.smtp.starttls.enable","true")
      val emailSession = Session.getDefaultInstance(properties)
      val message = new MimeMessage(emailSession)
      message.setFrom(s"<$senderEmail>")
      message.addRecipients(Message.RecipientType.TO,
        s"<${receiverEmail.get}>")
      message.setSubject(email.subject)
      message.setText(email.message)
      Transport.send(message, senderEmail, sender.get.password)
      email.sentdate = Option(message.getSentDate())

      sql"insert into emails(id, sender, receiver, subject, message, sentdate, userid) values(${email.id}, ${email.sender}, ${email.receiver}, ${email.subject}, ${email.message}, ${email.sentdate}, ${sender.get.id})"
        .update
        .apply()

    }

    override def getUsersMessages(username: String): List[Email] = {
      val userId = sql"select id from users where name = $username"
        .map(v => v.int("id"))
        .single
        .apply()

      return sql"select * from Emails where userId = 1"
        .map(rs => new Email(rs.int("id"), rs.string("sender"), rs.string("receiver"), rs.string("subject"), rs.string("message"), Option(rs.date("sentdate"))))
        .list
        .apply()
//      var user = userCollection.users.find(_.name == username)
 //     return user.get.Messages
    }
  }
}
