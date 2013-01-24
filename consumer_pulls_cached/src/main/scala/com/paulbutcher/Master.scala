package com.paulbutcher

import akka.actor._

class Master extends Actor {

  val producer = context.actorOf(Props[Producer], "producer")
 
  val cache = context.actorOf(Props(new Cache(producer)))
  context.watch(cache)

  var consumers = Set[ActorRef]()
  for (i <- 0 until WordCount.numConsumers)
    consumers += context.actorOf(Props(new Consumer(cache)), s"consumer$i")
  consumers foreach context.watch _

  def receive = {
    case Terminated(`cache`) =>
      for (consumer <- consumers)
        consumer ! PoisonPill

    case Terminated(consumer) =>
      consumers -= consumer
      if (consumers.isEmpty)
        context.system.shutdown
  }
}