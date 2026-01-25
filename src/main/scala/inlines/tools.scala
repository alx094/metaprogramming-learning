package inlines

trait Show[A]:
  def show(a: A): String

object Show {
  given Show[String] with
    def show(a: String): String = a

  given Show[Int] with
    def show(a: Int): String = a.toString

  given Show[Boolean] with
    def show(a: Boolean) = a.toString

  import scala.deriving.*
  import scala.compiletime.*
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

  // Necessary for type class derivation
  // - Requires no argument List
  // - must return Show[A]
  inline def derived[A <: Product](using m: Mirror.ProductOf[A]): Show[A] =
    new Show[A]:
      // show(Person("Alex", 31, true))
      override def show(a: A): String =
        val valueTuple = Tuple.fromProductTyped[A](a) // ("Alex", 31, true)
        val fieldRep =
          showTuple[m.MirroredElemTypes, m.MirroredElemLabels](
            valueTuple
          ) // ["name:Alex", "age:31", "programmer:true"]
        fieldRep.mkString("{", ", ", "}")

}
