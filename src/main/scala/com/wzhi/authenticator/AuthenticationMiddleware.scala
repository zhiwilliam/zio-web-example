package com.wzhi.authenticator

import zio._
import Authenticator._
import cats.data._
import zio.interop.catz._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware

trait AuthenticationMiddleware {

  type AppEnvironment <: Authenticator
  type AppTask[A] = RIO[AppEnvironment, A]

  val dsl: Http4sDsl[AppTask] = Http4sDsl[AppTask]
  import dsl._

  val authenticationHeaders = new AuthenticationHeaders[AppEnvironment] {}

  def authUser: Kleisli[AppTask, Request[AppTask], Either[String, AuthToken]] = {
    Kleisli({ request =>
      authenticationHeaders.getToken(request).map { e => {
        e.left.map (_.toString)
      }}
    }
    )
  }

  val onFailure: AuthedRoutes[String, AppTask] = Kleisli(req => OptionT.liftF {
    Forbidden()
  })

  val authenticationMiddleware: AuthMiddleware[AppTask, AuthToken] = AuthMiddleware(authUser, onFailure)
}
