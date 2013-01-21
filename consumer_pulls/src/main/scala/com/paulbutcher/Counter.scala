package com.paulbutcher

import akka.actor._
import collection.mutable.HashMap

class Counter(parser: ActorRef, accumulator: ActorRef) extends Actor {

  val counts = new HashMap[String, Int].withDefaultValue(0)

  override def preStart() {
    parser ! RequestWork
  }

  def receive = {
    case Page(title, text) =>
      for (word <- Words(text))
        counts(word) += 1
      parser ! RequestWork

    case NoMoreWork =>
      accumulator ! Counts(counts)
      context.stop(self)
  }
}