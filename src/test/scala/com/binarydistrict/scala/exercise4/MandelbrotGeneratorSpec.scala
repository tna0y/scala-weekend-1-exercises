package com.binarydistrict.scala.exercise4

import org.scalatest.{FunSuite, Matchers}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

class MandelbrotGeneratorSpec extends FunSuite with Matchers {
  val singleThreadGenerator = new SingleThreadBlockingGenerator
  val multiThreadGenerator = new MultiThreadGenerator(segments = 4)(ExecutionContext.global)

  val params = MandelbrotParams(900, 600, -2f, 1f, -1f, 1f, 1000)

  test("multi thread generator should generate a valid mandelbrot set") {
    val correctResults = Await.result(singleThreadGenerator(params), 10.seconds)
    val concurrentResults = Await.result(multiThreadGenerator(params), 10.seconds).take(600)
    concurrentResults.foreach(_ should have size correctResults.head.size)
    concurrentResults should have size correctResults.size
    concurrentResults.zip(correctResults).foreach {
      case (correct, concurrent) =>
        correct.zip(concurrent).foreach {
          case (correctValue, concurrentValue) =>
            // due to rounding results may vary slightly
            correctValue - concurrentValue +- 1
        }
    }
  }
}
