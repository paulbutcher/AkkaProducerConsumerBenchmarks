package com.paulbutcher

import akka.actor._

case class RequestWork()

class Producer extends Actor {

  val pages = Pages(100000, "enwiki.xml")

  def receive = {
    case RequestWork =>
      if (pages.hasNext)
        sender ! pages.next
      else
        sender ! PoisonPill
  }
}