val zioConfigVersion = "2.0.0"
val configDependencies = List(
  "dev.zio" %% "zio-config" % zioConfigVersion,
  "dev.zio" %% "zio-config-magnolia" % zioConfigVersion,
  "dev.zio" %% "zio-config-refined" % zioConfigVersion,
  "dev.zio" %% "zio-config-typesafe" % zioConfigVersion
).map(_ excludeAll
  ExclusionRule("zio")
)

lazy val circe = {
  val circeVersion = "0.13.0"
  Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser",
    "io.circe" %% "circe-yaml"
  ).map(_ % circeVersion)
}

lazy val http4s = {
  val http4sVersion  = "1.0-232-85dadc2" //"0.21.3"
  Seq(
    "org.http4s" %% "http4s-blaze-server",
    "org.http4s" %% "http4s-dsl",
    "org.http4s" %% "http4s-circe",
  ).map(_ % http4sVersion)
}

lazy val zio = {
  val zioVersion  = "1.0.12"
  Seq(
    "dev.zio"    %% "zio",
  ).map(_ % zioVersion)
}

lazy val zioLogging = {
  val zioLoggingVersion  = "0.5.14"
  Seq(
    "dev.zio"    %% "zio",
    "dev.zio"   %% "zio-logging",
    "dev.zio"   %% "zio-logging-slf4j"
  ).map(_ % zioLoggingVersion)
}

lazy val cats = {
  val catsVersion  = "2.7.0"//"2.1.1"
  Seq(
    "org.typelevel" %% "cats-core"
  ).map(_ % catsVersion)
}

lazy val catsEffect = {
  val catsEffectVersion  = "2.3.1" //"2.2.0"
  Seq(
    "org.typelevel" %% "cats-effect"
  ).map(_ % catsEffectVersion)
}

lazy val zioInteropCats = {
  val zioInteropCatsVersion  = "2.3.1.0" //"2.2.0"
  Seq(
    "dev.zio"    %% "zio-interop-cats"
  ).map(_ % zioInteropCatsVersion)
}


/*lazy val tsec = {
  val tsecVersion = "0.4.0"
  val fuuidVersion = "0.8.0-M2"
  Seq("io.github.jmcardon" %% "tsec-http4s" % tsecVersion,
    "io.chrisdavenport" %% "fuuid"  %  fuuidVersion,
    "com.chuusai" %% "shapeless"          % "2.3.7",
  )
}*/

lazy val root = (project in file("."))
  .settings(
    organization := "com.wzhi",
    name := "zio-web-example",
    version := "0.0.0",
    scalaVersion := "2.13.8",
    libraryDependencies ++= Seq(
      "org.apache.httpcomponents" % "httpasyncclient" % "4.1.3",
      "org.slf4j" % "slf4j-simple"          % "1.7.33",
    ) ++ zio ++ cats ++ catsEffect ++ zioInteropCats
      ++ zioLogging ++ http4s ++ configDependencies ++ circe //++ tsec
  )

addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings",
  "-P:kind-projector:underscore-placeholders"
)

