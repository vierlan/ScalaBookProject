package controllers

import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.Inject


class ApplicationController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index(): Action[AnyContent] = Action {
    Ok
  }

  def create(): Action[AnyContent] = Action {
    Ok
  }
  def read(id: String): Action[AnyContent] =  Action {
    Ok
  }
  def update(id: String): Action[AnyContent] = Action {
    Ok
  }
  def delete(id: String): Action[AnyContent] = Action {
    Ok
  }

}
