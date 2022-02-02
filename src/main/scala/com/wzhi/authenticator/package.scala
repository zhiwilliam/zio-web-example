package com.wzhi

import authenticator.Authenticator._

package object authenticator {
  import zio._
  def authenticate(userName: String, password: String): RIO[Authenticator, AuthToken] =
    ZIO.accessM[Authenticator](_.get.authenticate(userName, password))
}