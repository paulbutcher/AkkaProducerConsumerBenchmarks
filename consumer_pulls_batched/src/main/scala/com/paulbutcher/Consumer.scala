package com.paulbutcher

import akka.actor._
import collection.mutable.HashMap

case class Work(pages: Array[Page])

class Consumer(producer: ActorRef) extends Actor {

  val BATCH_SIZE = 10

  val counts = new HashMap[String, Int].withDefaultValue(0)

  override def preStart() {
    producer ! RequestWork(BATCH_SIZE)
  }

  def receive = {
    case Work(pages) =>
      for (Page(title, text) <- pages)
        for (word <- Words(text))
          counts(word) += 1
      producer ! RequestWork(BATCH_SIZE)
  }
}