package com.paulbutcher

import akka.actor._
import akka.routing.{Broadcast, FromConfig}

class WordCountPull(batchSize: Int) extends Actor {

  val producer = context.actorOf(Props[ProducerPull], "producer")
  val consumers = context.actorOf(Props(new ConsumerPull(producer, batchSize)).
    withRouter(FromConfig()), "consumers")

  context.watch(consumers)
  context.watch(producer)

  def receive = {
    case Terminated(`producer`) => consumers ! Broadcast(PoisonPill)
    case Terminated(`consumers`) => context.system.shutdown
  }
}

object WordCountPull {

  def getActor(batchSize: Int)() = new WordCountPull(batchSize)

  def getConfig(numConsumers: Int) = s"""
      akka.actor.deployment {
        /WordCount/consumers {
          router = round-robin
          nr-of-instances = $numConsumers
        }
      }"""
}