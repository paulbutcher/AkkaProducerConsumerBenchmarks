package com.paulbutcher

import akka.actor._

object WordCount extends App {

  val numConsumers = args(0).toInt
  val batchSize = args(1).toInt

  val system = ActorSystem("WordCount")
  val startTime = System.currentTimeMillis
  system.registerOnTermination(println(s"Elapsed time: ${System.currentTimeMillis - startTime}"))

  system.actorOf(Props[Master], "master")
}