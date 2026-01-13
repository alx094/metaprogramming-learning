package inlines

object CompileTimeErasure {

  import compiletime.erasedValue

  inline def pmOnType[A] = inline erasedValue[A] match
    case _: String => "a string type"
    case _: Int    => "a int type"
    case _         => "not supported"

  val anInt = pmOnType[Int] // "a int type"
  val anString = pmOnType[String] // "a string type"
  val aBoolean = pmOnType[Boolean] // "not supported"

  // erasedValue[A] is a fictitious value, only used for inline expressions
  // we cannot use erasedValue[A] at runtime
  inline def returnToRuntime[A] =
    inline erasedValue[A] match
      case s: String => s.length

  // val aFailure =
  //  returnToRuntime[String] // fails because erasedValue cannot be used

  // example of compile-time calculations
  import compiletime.ops.int.*
  import compiletime.constValue

  transparent inline def factorial[N <: Int]: Int = inline erasedValue[N] match
    case _: 0 => 1
    // S[n] => Successor of a natural number where zero is the type 0 and successors are reduced
    case _: S[n] => constValue[n + 1] * factorial[n]

  val fac: 24 = factorial[4]
}
