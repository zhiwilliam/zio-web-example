package com.wzhi.ztools

import com.wzhi.ztools.source.bufferedReader
import com.wzhi.ztools.source.from.file.{fileSource, resourceInputStream, resourceSource}
import com.wzhi.ztools.zioconfig.parsers._
import zio._
import zio.config._

package object zioconfig {
  def readDescription[A: Tag](descriptor: ConfigDescriptor[A]): ZIO[Has[ConfigSource], Throwable, A] = for {
    configSource <- ZIO.service[ConfigSource]
    desc <- Task.effect(descriptor.from(configSource))
    value <- read(desc)
  } yield value

  def readYamlFromResource(resourceFilePath: String): ZManaged[Any, Throwable, ConfigSource] =
    resourceSource(resourceFilePath) >>> bufferedReader >>> yaml.read2ConfigSource

  def readYamlFromFile(filePath: String): ZManaged[Any, Throwable, ConfigSource] =
    fileSource(filePath) >>> bufferedReader >>> yaml.read2ConfigSource

  def readPropertiesFromResource(resourceFilePath: String): ZManaged[Any, Throwable, ConfigSource] =
    resourceSource(resourceFilePath) >>> bufferedReader >>> properties.read2ConfigSource()

  def readPropertiesFromFile(filePath: String): ZManaged[Any, Throwable, ConfigSource] =
    fileSource(filePath) >>> bufferedReader >>> properties.read2ConfigSource()

  def readHocconFromResource(resourceFilePath: String): ZManaged[Any, Throwable, ConfigSource] =
    resourceSource(resourceFilePath) >>> bufferedReader >>> hoccon.read2ConfigSource

  def readHocconFromFile(filePath: String): ZManaged[Any, Throwable, ConfigSource] =
    fileSource(filePath) >>> bufferedReader >>> hoccon.read2ConfigSource

  def readXmlFromResource(resourceFilePath: String): ZManaged[Any, Throwable, ConfigSource] =
    resourceInputStream(resourceFilePath) >>> xml.read2ConfigSource()

  def readFromSysEnv(keyDelimiter: Option[Char] = Some('_'),
                     valueDelimiter: Option[Char] = Some(',')): ZManaged[system.System, Throwable, ConfigSource] =
    ZManaged.fromEffect(Task.effect(ConfigSource.fromSystemEnv(keyDelimiter, valueDelimiter)))

  def readFromCommandLine( argsList: List[String],
                           keyDelimiter: Option[Char] = Some('_'),
                           valueDelimiter: Option[Char] = Some(',')): ZManaged[Any, Throwable, ConfigSource] =
    ZManaged.fromEffect(Task.effect(ConfigSource.fromCommandLineArgs(argsList, keyDelimiter, valueDelimiter)))

  def readFromMap( map: Map[String, String],
                   keyDelimiter: Option[Char] = Some('_'),
                   valueDelimiter: Option[Char] = Some(',')): ZManaged[Any, Throwable, ConfigSource] =
    ZManaged.fromEffect(Task.effect(ConfigSource.fromMap(map, "constant", keyDelimiter, valueDelimiter)))

}