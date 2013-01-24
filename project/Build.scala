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
    "core", 
    file("core"),
    settings = buildSettings ++ Seq(
      name := "core", 
      libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.0"))

  lazy val producer_pushes_bounded_queue = Project(
    "producer_pushes_bounded_queue", 
    file("producer_pushes_bounded_queue"),
    settings = buildSettings ++ Seq(name := "producer_pushes_bounded_queue")) dependsOn(core)

  lazy val producer_pushes_unbounded_queue = Project(
    "producer_pushes_unbounded_queue", 
    file("producer_pushes_unbounded_queue"),
    settings = buildSettings ++ Seq(name := "producer_pushes_unbounded_queue")) dependsOn(core)

  lazy val consumer_pulls = Project(
    "consumer_pulls", 
    file("consumer_pulls"),
    settings = buildSettings ++ Seq(name := "consumer_pulls")) dependsOn(core)

  lazy val consumer_pulls_batched = Project(
    "consumer_pulls_batched", 
    file("consumer_pulls_batched"),
    settings = buildSettings ++ Seq(name := "consumer_pulls_batched")) dependsOn(core)

  lazy val consumer_pulls_cached = Project(
    "consumer_pulls_cached", 
    file("consumer_pulls_cached"),
    settings = buildSettings ++ Seq(name := "consumer_pulls_cached")) dependsOn(core)
}