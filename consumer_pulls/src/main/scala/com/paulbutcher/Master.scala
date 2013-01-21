package com.paulbutcher

import akka.actor._
import akka.routing.RoundRobinRouter

class Master extends Actor {

  var counters = Set[ActorRef]()

  val accumulator = context.actorOf(Props[Accumulator], "accumulator")
  val parser = context.actorOf(Props[Parser])

  for (i <- 0 to 8)
    counters += context.actorOf(Props(new Counter(parser, accumulator)))
  counters foreach context.watch _
  context.watch(accumulator)

  def receive = {
    case Terminated(`accumulator`) => context.system.shutdown
    case Terminated(counter) =>
      counters -= counter
      if (counters.isEmpty)
        accumulator ! PoisonPill
  }
}