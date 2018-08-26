package service


import java.util.Calendar

import model.User
import model.Email
import javax.mail._
import javax.mail.internet._
import scalikejdbc._


trait EmailServiceComponent {

  val emailService: EmailService

  trait EmailService {

    def sendMessage(email: Email)

    def getUsersMessages(username: String) : List[Email]
  }
}

trait EmailServiceComponentImpl extends EmailServiceComponent {

  override val emailService = new EmailServiceImpl
  implicit val session = AutoSession

  class EmailServiceImpl extends EmailService {


    override def sendMessage(email: Email) {
      val sender = sql"select * from users where name = ${email.sender}"
        .map(u => new User(u.int("id"), u.string("name"), u.string("email"), u.string("password")))
        .single
        .apply()
      val receiver = sql"select email from users where name = ${email.receiver}"
        .map(u => u.string("email"))
        .single
        .apply()
      sendEmail(email,sender,receiver)
      sql"insert into emails(id, sender, receiver, subject, message, sentdate, userid) values(${email.id}, ${email.sender}, ${email.receiver}, ${email.subject}, ${email.message}, ${email.sentdate}, ${sender.get.id})"
        .update
        .apply()

    }

    override def getUsersMessages(username: String): List[Email] = {
      return sql"select * from Emails where userId = (select id from users where name = $username )"
        .map(rs => new Email(rs.int("id"), rs.string("sender"), rs.string("receiver"), rs.string("subject"), rs.string("message"), Option(rs.date("sentdate"))))
        .list
        .apply()
    }

    def sendEmail(email: Email, sender: Option[User], receiver: Option[String]): Unit ={
      val senderEmail = sender.get.email
      val properties = new java.util.Properties()
      properties.put("mail.smtp.host", "smtp.mail.ru")
      properties.put("mail.smtp.starttls.enable","true")
      val emailSession = Session.getDefaultInstance(properties)
      val message = new MimeMessage(emailSession)
      message.setFrom(s"<$senderEmail>")
      message.addRecipients(Message.RecipientType.TO,
        s"<${receiver.get}>")
      message.setSubject(email.subject)
      message.setText(email.message)
      Transport.send(message, senderEmail, sender.get.password)
      /// email.sentdate = Option(message.getSentDate())
      email.sentdate = Option(Calendar.getInstance().getTime())
    }
  }
}
