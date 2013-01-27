package com.paulbutcher

import akka.actor._
import collection.mutable.Queue

class Cache(producer: ActorRef, batchSize: Int) extends Actor {

  val workQueue = Queue[Work]()
  val workerQueue = Queue[ActorRef]()

  for (i <- 0 to 100)
    producer ! RequestWork(batchSize)

  def sendToWorker(worker: ActorRef, work: Work) {
    worker ! work
    producer ! RequestWork(batchSize)
  }

  def receive = {
    case RequestWork(_) =>
      if (workQueue.nonEmpty)
        sendToWorker(sender, workQueue.dequeue)
      else
        workerQueue.enqueue(sender)

    case work: Work =>
      if (workerQueue.nonEmpty)
        sendToWorker(workerQueue.dequeue, work)
      else
        workQueue.enqueue(work)
  }
}