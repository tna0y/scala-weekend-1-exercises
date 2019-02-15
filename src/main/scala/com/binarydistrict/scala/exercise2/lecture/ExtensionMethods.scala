package com.binarydistrict.scala.exercise2.lecture

object ExtensionMethods extends App {

  implicit class RichInt(val self: Int) extends AnyVal {
    def toFunny: String = s"$self :P"
  }

  println(1.toFunny)
}
