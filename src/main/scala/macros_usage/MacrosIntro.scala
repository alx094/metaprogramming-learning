package macros_usage

import macros.MacrosIntro.*

object MacrosIntro {
  val firstMacroValue = firstMacro(30, "Scala")

  // The following will fail
  // val aNum = 23
  // val aString = "Oi!"
  // val failureMacroValue =
  //   firstMacro(aNum, aString) // value not know at compile time
  val properMacroValue = firstMacro(2 + 3, "Hola mundo")

  // will expand the expressions literally in the macro implementation
  val inlineExpandedMacro = firstMacroIA(1 + 3 / 4, "Scala".repeat(3))
}
