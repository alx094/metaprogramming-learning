package inlines

inline def compileTimeError(x: Int): Int =
  compiletime.error("This should fail to compile")

// This fails with custom error
//val three = compileTimeError(3)

inline def pmWithCTE(x: Option[Any]): String =
  inline x match {
    case Some(v: Int)    => v.toString
    case Some(v: String) => v
    case None            => "Nothing"
    case _               => compiletime.error("Value not supported")
  }

val anInt = pmWithCTE(Some(3))
//val notSupported = pmWithCTE(Option("not supported"))
//val notSupported = pmWithCTE(Option(true))

inline def improperCTError(x: String) = compiletime.error(s"Fail with: $x")

// val improperError = improperCTError(
//   "Something"
// ) // fails because arguments to compiletime.error must be constant (no interpolation)
