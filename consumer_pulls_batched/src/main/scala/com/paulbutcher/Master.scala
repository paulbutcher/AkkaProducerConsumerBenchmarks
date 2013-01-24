package com.paulbutcher

import akka.actor._

class Master extends Actor {

  var consumers = Set[ActorRef]()
  val producer = context.actorOf(Props[Producer], "producer")

  for (i <- 0 until WordCount.numConsumers)
    consumers += context.actorOf(Props(new Consumer(producer)), s"consumer$i")
  consumers foreach context.watch _

  def receive = {
    case Terminated(consumer) =>
      consumers -= consumer
      if (consumers.isEmpty)
        context.system.shutdown
  }
}