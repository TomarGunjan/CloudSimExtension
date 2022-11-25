ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.0.2"

lazy val root = (project in file("."))
  .settings(
    name := "CloudSimExtension"
  )

val sfl4sVersion = "2.0.0-alpha5"
val logbackVersion = "1.3.0-alpha10"
val typesafeConfigVersion = "1.4.1"
val scalacticVersion = "3.2.9"

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-api" % sfl4sVersion,
  "com.typesafe" % "config" % typesafeConfigVersion,
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "org.scalatest" %% "scalatest" % scalacticVersion % Test,
  "org.cloudsimplus" % "cloudsim-plus" % "6.4.3"
)

