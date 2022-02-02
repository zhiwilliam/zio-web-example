package com.wzhi.ztools.zioconfig.parsers

import java.io.InputStreamReader

import com.typesafe.config.ConfigFactory
import zio._
import zio.config._
import zio.config.typesafe.TypesafeConfigSource.fromTypesafeConfig

package object hoccon {
  def read2ConfigSource: ZManaged[InputStreamReader, Throwable, ConfigSource] = {
    val configSource = for {
      streamReader <- ZIO.environment[InputStreamReader]
      confSource <- Task.effect(fromTypesafeConfig(Task.effect(ConfigFactory.parseReader(streamReader))))
    } yield confSource
    ZManaged.fromEffect(configSource)
  }
}
