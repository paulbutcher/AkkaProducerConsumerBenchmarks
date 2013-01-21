package com.paulbutcher

import java.text.BreakIterator

class Words(text: String) extends Iterator[String] {
  val wordBoundary = BreakIterator.getWordInstance
  wordBoundary.setText(text)
  var start = wordBoundary.first
  var end = wordBoundary.next
  
  def hasNext = end != BreakIterator.DONE
  
  def next() = {
    val s = text.subSequence(start, end)
    start = end
    end = wordBoundary.next
    s.toString
  }
}

object Words {
  def apply(text:String) = new Words(text)
}