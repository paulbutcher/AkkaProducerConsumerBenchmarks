package com.paulbutcher

import akka.actor._
import collection.Map
import collection.mutable.HashMap

case class Counts(counts: Map[String, Int])

class Accumulator extends Actor {
  
  val counts = new HashMap[String, Int].withDefaultValue(0)

  def receive = {
    case Counts(partialCounts) =>
      for ((word, count) <- partialCounts)
        counts(word) += count
  }

  override def postStop() {
    // for ((k, v) <- counts)
    //   println(s"$k=$v")
  }
}