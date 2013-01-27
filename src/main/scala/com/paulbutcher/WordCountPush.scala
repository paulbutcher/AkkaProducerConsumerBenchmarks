package com.paulbutcher

import akka.actor._
import akka.routing.{Broadcast, FromConfig}

class WordCountPush extends Actor {

  val consumers = context.actorOf(Props[ConsumerPush].
    withRouter(FromConfig()).
    withDispatcher("consumer-dispatcher"), "consumers")
  val producer = context.actorOf(Props(new ProducerPush(consumers)), "producer")

  context.watch(consumers)
  context.watch(producer)

  def receive = {
    case Terminated(`producer`) => consumers ! Broadcast(PoisonPill)
    case Terminated(`consumers`) => context.system.shutdown
  }
}

object WordCountPush {

  def getActor() = new WordCountPush

  def getConfig(routerType: String, dispatcherType: String)(numConsumers: Int) = s"""
      akka.actor.deployment {
        /WordCount/consumers {
          router = $routerType
          nr-of-instances = $numConsumers
        }
      }

      consumer-dispatcher {
        type = $dispatcherType
        mailbox-capacity = 100
      }"""
}