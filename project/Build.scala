import sbt._
import Keys._
import sbtassembly.Plugin._
import AssemblyKeys._

object WordCountBuild extends Build {
  val buildSettings = Defaults.defaultSettings ++ assemblySettings ++ Seq(
    organization := "com.paulbutcher",
    version := "0.1",
    scalaVersion := "2.10.0",
    scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature"))

  lazy val core = Project(
    "WordCount", 
    file("."),
    settings = buildSettings ++ Seq(
      name := "WordCount", 
      libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.0"))
}