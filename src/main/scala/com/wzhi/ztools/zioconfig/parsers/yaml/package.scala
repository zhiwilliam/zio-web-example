package com.wzhi.ztools.zioconfig.parsers

import java.io.InputStreamReader

import com.typesafe.config.ConfigFactory
import io.circe.yaml.parser
import zio._
import zio.config._
import zio.config.typesafe.TypesafeConfigSource.fromTypesafeConfig

package object yaml {
  def read2ConfigSource: ZManaged[InputStreamReader, Throwable, ConfigSource] = {
    val configSource = for {
      streamReader <- ZIO.environment[InputStreamReader]
      json <- Task.fromEither(parser.parse(streamReader))
      conf <- Task.effect(ConfigFactory.parseString(json.toString()))
      confSource <- Task.effect(fromTypesafeConfig(Task.succeed(conf)))
    } yield confSource
    ZManaged.fromEffect(configSource)
  }
}
