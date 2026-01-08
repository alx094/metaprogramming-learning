package inlines

trait Semigroup[A] {
  def combine(a: A, b: A): A
}

def doubleSimple[A](a: A)(using Semigroup[A]) =
  summon[Semigroup[A]].combine(a, a)

import scala.compiletime.summonInline

/*
 * Even though we don't have all the information about A
 * summonInline defers the act of summoning to the call site of the function
 * and the type A will be concrete to the compiler and it will be clear
 * whether summoning is possible or not
 * */
inline def double[A](a: A) =
  summonInline[Semigroup[A]].combine(a, a)

given Semigroup[Int] = _ + _
val four = double(4)

trait Messenger[A] {
  def message: String
}

given Messenger[Int] with {
  override def message: String = "this is an Int"
}

/*
    With summonFrom, we can conditionally produce values at compile time (inlined) depending on the givens the compiler finds.
    The pattern match will return the expression for the first matched given found at the call site.
 */
import scala.compiletime.summonFrom
inline def produce[A] =
  summonFrom {
    case m: Messenger[A] => s"Found messenger: ${m.message}"
    case _               => "No messenger found"
  }

val intMessenger = produce[Int]
val stringMessenger = produce[String]
