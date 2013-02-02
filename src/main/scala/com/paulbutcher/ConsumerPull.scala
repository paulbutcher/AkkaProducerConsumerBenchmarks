package com.paulbutcher

import akka.actor._
import collection.mutable.HashMap

case class Work(pages: Seq[Page])

class ConsumerPull(producer: ActorRef, batchSize: Int) extends Actor {

  val counts = new HashMap[String, Int].withDefaultValue(0)

  override def preStart() {
    producer ! RequestWork(batchSize)
  }

  def receive = {
    case Work(pages) =>
      producer ! RequestWork(batchSize)
      for (Page(title, text) <- pages)
        for (word <- Words(text))
          counts(word) += 1
  }
}