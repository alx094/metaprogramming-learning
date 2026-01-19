package inlines

case class Person(name: String, age: Int, programmer: Boolean)

enum Permissions:
  case READ, DELETE, WRITE

import scala.deriving.*
import scala.compiletime.*

object Mirrors {
  val personMirror = summon[Mirror.Of[Person]] // Mirror.ProductOf[Person]

  val alex: Person = personMirror.fromTuple(("alex", 31, true))
  val aTuple: (String, Int, Boolean) = Tuple.fromProductTyped(alex)

  val caseClassName =
    constValue[
      personMirror.MirroredLabel
    ] // The name of the class, known at compile time

  val fieldNames =
    constValueTuple[personMirror.MirroredElemLabels] // name of the field

  // mirror of sum types
  val permissionsMirror = summon[Mirror.Of[Permissions]]
  // we can get the type name
  // we can get a list of all cases
  val allCases = constValueTuple[permissionsMirror.MirroredElemLabels]

  // autoderivation for a serialization type class
  // showTuple[(String, Int, Boolean), ("Name", "age", "programmer")]("Alex", 31, true)
  // => ["name: Alex", "age: 31", "programmer:true"]
  inline def showTuple[E <: Tuple, L <: Tuple](elements: E): List[String] =
    inline (elements, erasedValue[L]) match
      case (EmptyTuple, EmptyTuple) => List()
      case (el: (eh *: et), lab: (lh *: lt)) =>
        val (h *: t) = el
        val label = constValue[lh]
        val value = summonInline[Show[eh]].show(h)

        ("" + label + ": " + value) :: showTuple[et, lt](t)

  inline def showCC[A <: Product](using m: Mirror.ProductOf[A]): Show[A] =
    new Show[A]:
      // show(Person("Alex", 31, true))
      override def show(a: A): String =
        val valueTuple = Tuple.fromProductTyped[A](a) // ("Alex", 31, true)
        val fieldRep =
          showTuple[m.MirroredElemTypes, m.MirroredElemLabels](
            valueTuple
          ) // ["name:Alex", "age:31", "programmer:true"]
        fieldRep.mkString("{", ", ", "}")

  @main def exMirrors =
    val yoda = Person("Yoda", 800, false)
    val showPerson = showCC[Person]
    val yodaJson = showPerson.show(yoda)
    println(yodaJson)
}
