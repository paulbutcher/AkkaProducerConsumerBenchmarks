package com.paulbutcher

import akka.actor._

case class RequestWork(batchSize: Int)

class ProducerPull extends Actor {

  val pages = Pages(100000, "enwiki.xml")

  def receive = {
    case RequestWork(batchSize) =>
      if (pages.hasNext)
        sender ! Work(pages.take(batchSize).toVector)
      else
        context.stop(self)
  }
}