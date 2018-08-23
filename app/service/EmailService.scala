package service

import java.util.Calendar
import model.User
import model.Email
import model.UserCollection
import java.util.concurrent.ConcurrentHashMap
import play.api.libs.mailer._


import scala.collection.mutable.ListBuffer

trait EmailServiceComponent {

  val userCollection: UserCollection
  val emailService: EmailService

  trait EmailService {

    def sendMessage(email: Email)

    def getUsersMessages(username: String) : ListBuffer[Email]
  }
}

trait EmailServiceComponentImpl extends EmailServiceComponent
with MailerComponents{

  override val userCollection = new UserCollection {}
  override val emailService = new EmailServiceImpl

  class EmailServiceImpl extends EmailService {

    override def sendMessage(email: Email) {
      var sender = userCollection.users.find(_.name == email.sender)
      sender.get.Messages += email

    }

    override def getUsersMessages(username: String): ListBuffer[Email] = {
      var user = userCollection.users.find(_.name == username)
      return user.get.Messages
    }
  }
}
