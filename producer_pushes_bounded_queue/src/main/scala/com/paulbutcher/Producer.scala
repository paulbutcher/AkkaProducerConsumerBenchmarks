package com.paulbutcher

import akka.actor._

case class Produce()

class Producer(consumers: ActorRef) extends Actor {

  val pages = Pages(100000, "enwiki.xml")
  self ! Produce

  def receive = {
    case Produce =>
      for (page <- pages)
        consumers ! page
      consumers ! PoisonPill
  }
}