package com.paulbutcher

import akka.actor._
import com.typesafe.config.ConfigFactory

case class Run(name: String, actor: () => Actor, config: (Int) => String)

object Benchmarks extends App {

  val runs = Seq(
      Run("push_rr", WordCountPush.getActor _, WordCountPush.getConfig("round-robin", "Dispatcher") _),
      Run("push_sm", WordCountPush.getActor _, WordCountPush.getConfig("smallest-mailbox", "Dispatcher") _),
      Run("push_bal", WordCountPush.getActor _, WordCountPush.getConfig("round-robin", "BalancingDispatcher") _),
      Run("flow_rr", WordCountFlowControl.getActor _, WordCountFlowControl.getConfig("round-robin", "Dispatcher") _),
      Run("flow_sm", WordCountFlowControl.getActor _, WordCountFlowControl.getConfig("smallest-mailbox", "Dispatcher") _),
      Run("flow_bal", WordCountFlowControl.getActor _, WordCountFlowControl.getConfig("round-robin", "BalancingDispatcher") _),
      Run("pull_1", WordCountPull.getActor(1) _, WordCountPull.getConfig _),
      Run("pull_10", WordCountPull.getActor(10) _, WordCountPull.getConfig _),
      Run("pull_20", WordCountPull.getActor(20) _, WordCountPull.getConfig _),
      Run("pull_50", WordCountPull.getActor(50) _, WordCountPull.getConfig _),
      Run("pull_cached", WordCountCached.getActor(1) _, WordCountCached.getConfig _)
    )

  while (true)
    runs foreach execute _

  def execute(run: Run) {
    val times = for (i <- 1 to 10) yield {
        System.gc             // GC so each test starts from a clean heap
        Thread.sleep(10000)   // Sleep between tests to avoid thermal throttling
        execute(run, i)
      }

    println(s"${run.name},${times.mkString(",")}")
  }

  def execute(run: Run, numConsumers: Int) = {

    val config = ConfigFactory.parseString(run.config(numConsumers))

    val system = ActorSystem("Benchmarks", ConfigFactory.load(config))
    val startTime = System.currentTimeMillis

    system.actorOf(Props(run.actor()), "WordCount")
    system.awaitTermination

    System.currentTimeMillis - startTime
  }
}