package com.paulbutcher

import akka.actor._

case class RequestWork()
case class NoMoreWork()

class Parser extends Actor {

  val pages = Pages(100000, "enwiki.xml")

  def receive = {
    case RequestWork =>
      if (pages.hasNext)
        sender ! pages.next
      else
        sender ! NoMoreWork
  }
}