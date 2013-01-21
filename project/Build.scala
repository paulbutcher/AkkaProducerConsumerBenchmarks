import sbt._
import Keys._

object WordCountBuild extends Build {
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "com.paulbutcher",
    version := "0.1",
    scalaVersion := "2.10.0",
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"))

  lazy val core = Project(
    "core", 
    file("core"),
    settings = buildSettings ++ Seq(
      name := "Utils", 
      libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.0"))

  lazy val consumer_pulls = Project(
    "consumer_pulls", 
    file("consumer_pulls"),
    settings = buildSettings ++ Seq(name := "Consumer Pulls")) dependsOn(core)
}