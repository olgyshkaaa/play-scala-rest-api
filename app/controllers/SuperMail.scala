package controllers

import play.api._
import play.api.mvc._
import service.EmailServiceComponentImpl

class SuperMail extends EmailController
  with EmailServiceComponentImpl {

  def index = Action {
    Ok(views.html.index())
  }

}