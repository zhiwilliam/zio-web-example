package com.wzhi.authenticator

sealed abstract class AuthError(val message: String) extends Exception(message)
object AuthError {
  case object NoLogin          extends AuthError("Could not find login from header")
  case object NoAuthHeader     extends AuthError("Could not find an Authorization header")
  case object NoDatetimeHeader extends AuthError("Could not find an X-Datetime header")
  case object BadMAC           extends AuthError("Bad MAC")
  case object InvalidMacFormat extends AuthError("The MAC is not a valid Base64 string")
  case object InvalidDatetime  extends AuthError("The datetime is not a valid UTC datetime string")
  case object Timeout          extends AuthError("The request time window is closed")
}