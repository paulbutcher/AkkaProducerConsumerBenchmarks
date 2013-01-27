package com.paulbutcher

import akka.actor._

case class Processed()

class ProducerFlowControl(consumers: ActorRef) extends Actor {

  val pages = Pages(100000, "enwiki.xml")

  for (page <- pages.take(100))
    consumers ! page

  def receive = {
    case Processed =>
      if (pages.hasNext)
        consumers ! pages.next
      else
        context.stop(self)
  }
}