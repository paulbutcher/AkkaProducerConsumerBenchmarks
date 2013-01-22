package com.paulbutcher

import akka.actor._

object WordCount extends App {

  val system = ActorSystem("WordCount")

  system.actorOf(Props[Master], "master")
}