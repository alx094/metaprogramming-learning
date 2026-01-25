package inlines

case class Person(name: String, age: Int, programmer: Boolean) derives Show
// compiler will look for a method derived in the companion object of show
// such as it returns a Show[Person]
// Will give us a GIVEN Show[Person]

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

  @main def exMirrors =
    val yoda = Person("Yoda", 800, false)
    val showPerson = Show.derived[Person] // explicit call
    val showPerson_v2 = summon[Show[Person]]
    val yodaJson = showPerson.show(yoda)
    println(yodaJson)
}
