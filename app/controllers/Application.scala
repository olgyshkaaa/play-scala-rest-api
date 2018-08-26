package controllers

import javax.inject.Singleton
import play.api.mvc._
import service.EmailServiceComponentImpl

class Application extends EmailController
  with EmailServiceComponentImpl {

  def index = Action {
    Ok(views.html.index())
  }

}