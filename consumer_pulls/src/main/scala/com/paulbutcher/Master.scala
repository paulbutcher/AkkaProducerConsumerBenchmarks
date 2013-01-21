package com.paulbutcher

import akka.actor._

class Master extends Actor {

  var consumers = Set[ActorRef]()
  val producer = context.actorOf(Props[Producer], "producer")

  for (i <- 0 to 8)
    consumers += context.actorOf(Props(new Consumer(producer)), "consumer")
  consumers foreach context.watch _

  def receive = {
    case Terminated(counter) =>
      consumers -= counter
      if (consumers.isEmpty)
        context.system.shutdown
  }
}