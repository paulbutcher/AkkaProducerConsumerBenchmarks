package com.paulbutcher

import akka.actor._
import akka.routing.{Broadcast, FromConfig}

class WordCountCached(batchSize: Int) extends Actor {

  val producer = context.actorOf(Props[ProducerPull], "producer")
  val cache = context.actorOf(Props(new Cache(producer, batchSize)))
  val consumers = context.actorOf(Props(new ConsumerPull(cache, batchSize)).
    withRouter(FromConfig()), "consumers")

  context.watch(consumers)
  context.watch(cache)
  context.watch(producer)

  def receive = {
    case Terminated(`producer`) => cache ! PoisonPill
    case Terminated(`cache`) => consumers ! Broadcast(PoisonPill)
    case Terminated(`consumers`) => context.system.shutdown
  }
}

object WordCountCached {

  def getActor(batchSize: Int)() = new WordCountCached(batchSize)

  def getConfig(numConsumers: Int) = s"""
      akka.actor.deployment {
        /WordCount/consumers {
          router = round-robin
          nr-of-instances = $numConsumers
        }
      }"""
}