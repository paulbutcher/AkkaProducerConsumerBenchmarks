package com.paulbutcher

import akka.actor._
import collection.mutable.Queue

class Cache(producer: ActorRef) extends Actor {

  val workQueue = Queue[Page]()
  val requestQueue = Queue[ActorRef]()

  for (i <- 0 until 100)
    producer ! RequestWork

  def deliverWorkTo(consumer: ActorRef) {
    consumer ! workQueue.dequeue
    producer ! RequestWork
  }

  def receive = {
    case page: Page =>
      workQueue.enqueue(page)
      if (requestQueue.nonEmpty)
        deliverWorkTo(requestQueue.dequeue)

    case RequestWork =>
      if (workQueue.nonEmpty)
        deliverWorkTo(sender)
      else
        requestQueue.enqueue(sender)
  }
}