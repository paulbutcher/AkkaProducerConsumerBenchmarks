package com.paulbutcher

import akka.actor._
import akka.routing.{Broadcast, FromConfig}

class WordCountFlowControl extends Actor {

  val consumers = context.actorOf(Props[ConsumerFlowControl].
    withRouter(FromConfig()).
    withDispatcher("consumer-dispatcher"), "consumers")
  val producer = context.actorOf(Props(new ProducerFlowControl(consumers)), "producer")

  context.watch(consumers)
  context.watch(producer)

  def receive = {
    case Terminated(`producer`) => consumers ! Broadcast(PoisonPill)
    case Terminated(`consumers`) => context.system.shutdown
  }
}

object WordCountFlowControl {

  def getActor() = new WordCountFlowControl

  def getConfig(routerType: String, dispatcherType: String)(numConsumers: Int) = s"""
      akka.actor.deployment {
        /WordCount/consumers {
          router = $routerType
          nr-of-instances = $numConsumers
        }
      }

      consumer-dispatcher {
        type = $dispatcherType
      }"""
}