package com.wzhi

import cats.data.{Kleisli, OptionT}
import fs2.Stream.Compiler._
import org.http4s._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import authenticator._
import Authenticator._
import com.wzhi.ztools.zioconfig.{readDescription, readHocconFromFile, readHocconFromResource, readYamlFromResource}
import org.http4s.dsl.Http4sDsl
import zio._
import zio.blocking.Blocking
import zio.clock.Clock
import zio.config.magnolia.descriptor
import zio.console._
import zio.interop.catz._

object Main extends App with AuthenticationMiddleware {

  class AutherizedServices[R <: Authenticator] {

    type AuthenticatorTask[T] = RIO[R, T]
    private val dsl = Http4sDsl[AuthenticatorTask]

    import dsl._

    val service = AuthedRoutes.of[AuthToken, AuthenticatorTask] {
      case GET -> Root as authToken =>
        Ok(s"hello! ${authToken.tok}")
    }
  }

  type AppEnvironment = Authenticator with Clock

  val autherizedServices = new AutherizedServices[AppEnvironment] {}

  val authenticatedService = authenticationMiddleware(autherizedServices.service)

  val secApp = Router[AppTask](
    "" -> authenticatedService
  ).orNotFound

  def createServer(host: String, port: Int,
                   app: Kleisli[AppTask, Request[AppTask], Response[AppTask]] ) = ZIO.runtime[AppEnvironment]
    .flatMap {
      implicit rts =>
        BlazeServerBuilder[AppTask](rts.platform.executor.asEC)
          .bindHttp(port, host)
          .withHttpApp(app)
          .resource
          .toManagedZIO
          .useForever
          .foldCauseM(
            err => putStrLn(err.prettyPrint).as(ExitCode.failure),
            _ => ZIO.succeed(ExitCode.success))
    }

  case class Service(host: String, port: Int)
  case class ApplicationConfig(service: Service)
  val appConf = descriptor[ApplicationConfig]
  val server = for {
    appConfig <- readDescription(appConf)
    server1 <- createServer(appConfig.service.host, appConfig.service.port, secApp)
  } yield server1

  def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] =
    server.provideCustomLayer(
      friendly ++ readHocconFromResource("application.conf").toLayer).exitCode

  /*
  case class Service(host: String, port: Int)
  case class ApplicationConfig(service: Service)
  val appConf = descriptor[ApplicationConfig]
  appConfig <- readDescription(appConf).provideLayer(readYamlFromResource("test.yaml").toLayer).orDie
   */
}
