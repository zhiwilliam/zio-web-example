package com.wzhi.authenticator

import zio._
import Authenticator._
import com.wzhi.authenticator
import org.http4s._
import org.http4s.headers.Authorization

trait AuthenticationHeaders[R <: Authenticator] {
  type AuthHTask[T] = RIO[R, T]

  private def unauthenticated = IO.succeed(Left(new Exception("Unauthenticated")))

  def getToken(req: Request[AuthHTask]) : AuthHTask[Either[Throwable, AuthToken]] = {
    val userNamePasswordOpt: Option[Array[String]] =
      for {
        auth <- req.headers.get(Authorization).map(_.value)
        asSplit = auth.split(" ")
        if asSplit.size == 2
      } yield asSplit
    userNamePasswordOpt.map { asSplit =>
      val tok = authenticator.authenticate(asSplit(0), asSplit(1))
      tok.either
    }.getOrElse(unauthenticated)
  }

}

object AuthenticationHeaders {
  def addAuthentication[Tsk[_]](request: Request[Tsk], username: String, password: String): Request[Tsk] =
    request.withHeaders(request.headers.put(Header("Authorization", s"$username $password")))
}