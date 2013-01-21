package com.paulbutcher

import akka.actor._
import collection.mutable.HashMap

class Consumer(producer: ActorRef) extends Actor {

  val counts = new HashMap[String, Int].withDefaultValue(0)

  override def preStart() {
    producer ! RequestWork
  }

  def receive = {
    case Page(title, text) =>
      for (word <- Words(text))
        counts(word) += 1
      producer ! RequestWork
  }
}