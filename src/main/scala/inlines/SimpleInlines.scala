package inlines

import scala.util.Random

def increment(a: Int): Int = a + 1
inline def inc(a: Int): Int = a + 1

val aNumber = 4
val four = inc(aNumber) //reduces to aNumber + 1 at Compile time

val eight = inc(2 * aNumber + 1)
/*
 * Reduces to
 * {
 *   val proxy = 2 * aNumber + 1
 *   proxy + 1
 * }
 * */

inline def incia(inline x: Int) = x + 1
val eight_v2 = incia(2 * aNumber + 1) // reduces to 2 * aNumber + 1 + 1

//transparent inlines, more information to the compiler
transparent inline def wrap(x: Int): Option[Int] = Some(x)

val typeCheckOk: Option[Int] = wrap(2)
val aSome: Some[Int] = wrap(2)

def testInline =
  inline def loop[A](
      inline start: A,
      inline condition: A => Boolean,
      inline advance: A => A
  )(inline action: A => Any) =
    var a = start
    while (condition(a)) {
      action(a)
      a = advance(a)
    }

  val start = System.currentTimeMillis()
  val r = Random().nextInt(10_000)
  val u = Random().nextInt(10_000)
  val array = Array.ofDim[Int](10_000)

  loop(0, _ < 10_000, _ + 1) { i =>
    loop(0, _ < 10_000, _ + 1) { j =>
      array(j) = array(j) + u
    }
    array(i) = array(i) + r
  }

  println(s"Inline version: ${(System.currentTimeMillis() - start) / 1000.0}s")

def testNoInline =
  def loop[A](
      start: A,
      condition: A => Boolean,
      advance: A => A
  )(action: A => Any) =
    var a = start
    while (condition(a)) {
      action(a)
      a = advance(a)
    }

  val start = System.currentTimeMillis()
  val r = Random().nextInt(10_000)
  val u = Random().nextInt(10_000)
  val array = Array.ofDim[Int](10_000)

  loop(0, _ < 10_000, _ + 1) { i =>
    loop(0, _ < 10_000, _ + 1) { j =>
      array(j) = array(j) + u
    }
    array(i) = array(i) + r
  }

  println(
    s"No Inline version: ${(System.currentTimeMillis() - start) / 1000.0}s"
  )

@main def main =
  testInline
  testNoInline
//Inline version: 0.049s
//No Inline version: 0.182s
