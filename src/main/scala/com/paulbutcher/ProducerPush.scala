package com.paulbutcher

import akka.actor._

class ProducerPush(consumers: ActorRef) extends Actor {

  val pages = Pages(100000, "enwiki.xml")
  for (page <- pages)
    consumers ! page
  context.stop(self)

  def receive = {
    case _ => ???
  }
}