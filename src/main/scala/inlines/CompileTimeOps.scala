package inlines

object CompileTimeOps {
  object Ints {
    import scala.compiletime.ops.int.*

    val two: 1 + 1 = 2
    val four: 2 * 2 = 4
    val truth: 3 <= 4 = true
    val aString: ToString[2 * 4] = "8"
  }

  object Booleans {
    import scala.compiletime.ops.boolean.*

    val lie: ![true] = false
    val combination: true && false = false
  }

  object Strings {
    import scala.compiletime.ops.string.*

    val aLiteral: "Scala" = "Scala"
    val aLength: Length["Scala"] = 5
    val regexMatcher: Matches["Scala", ".*al*"] = true
  }

  // const values
  object values {
    import scala.compiletime.ops.int.*
    import scala.compiletime.ops.string.{+ => _, *}
    import scala.compiletime.{constValue, constValueOpt}
    val five = constValue[2 + 3]

    val five_v2 = constValue[Length["Scala"]]
    // anything other than literal will fail
    // val anInt = constValue[Int]

    // constValueOpt will give you an option, if you have literal => Some otherwise None
    val fiveOpt = constValueOpt[2 + 3]
    val fiveNone = constValueOpt[Int]

    inline def customErrorCode[N <: Int] =
      compiletime.error("Error code: " + constValue[ToString[N]])

    // val customError = customErrorCode[6]

  }
}
