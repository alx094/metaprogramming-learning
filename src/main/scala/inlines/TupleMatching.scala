package inlines

object TupleMatching {

  trait Show[A]:
    def show(a: A): String

  object Show {
    given Show[String] with
      def show(a: String): String = a

    given Show[Int] with
      def show(a: Int): String = a.toString

    given Show[Boolean] with
      def show(a: Boolean) = a.toString
  }

  import scala.compiletime.summonInline

  inline def showTuple[T <: Tuple](tuple: T): String =
    inline tuple match
      case EmptyTuple => ""
      // (1, "a", true) == 1 *: "a" *: true
      case tup: (h *: t) =>
        val h *: t = tup
        summonInline[Show[h]].show(h) + " " + showTuple[t](t)

  val aTupleString = showTuple(("String", 2, true))

  val genericTuple: Tuple = ("String", 2, true)
  // val wontCompile = showTuple(genericTuple) // too general

  @main def printTuple =
    println(aTupleString)
}
