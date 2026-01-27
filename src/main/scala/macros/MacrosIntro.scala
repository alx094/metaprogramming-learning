package macros

// trait Expr // "code elements" = AST
// case class Num(n: Int) extends Expr
// case class Sum(e1: Expr, e2: Expr) extends Expr
// case class Div(e1: Expr, e2: Expr) extends Expr
// case class Sin(e: Expr) extends Expr

import quoted.*
object MacrosIntro {
  // compiler: code => AST  => binary => run binary
  // metaprogramming = programming with "code elements" as first class values
  // i.e. manipulate code at compile time
  // 2 + 3 / 4 + sin(30) => Sum(Num(2), Sum(Div(Num(3), Num(4)), Sin(Num(3))))

  // Phases in scala
  // code -> AST (quoting) -> new code (splicing) -> compiled later
  //  | ----------------------------------------- |
  //  |  macro                                    |

  /*
   * Macro structure:
   * - inline function with some args
   * - args/expresions can be QUOTED => turned into ASTs, as Expr[A]
   * - those ASTs are manipulated into some other ASTs, as Expr[B]
   * - the final AST is injected into the code (= SPLICED) => final value is returned
   * */
  inline def firstMacro(number: Int, string: String): String =
    ${ firstMacroImpl('number, 'string) } // S{ AST } => Splicing that AST

  def firstMacroImpl(
      numberAST: Expr[Int],
      stringAST: Expr[String]
  )(using Quotes): Expr[String] =
    // Expr[A] can be turned into a value if it's known at compile time
    val numValue = numberAST.valueOrAbort
    val stringValue = stringAST.valueOrAbort

    // expressions can be evaluated at compile time
    // you have access to the standard scala library
    val newString =
      if stringValue.length > 10 then stringValue.take(numValue)
      else stringValue.repeat(numValue)

    Expr("This macro impl is: " + newString)

    // macro with inline arguments
  inline def firstMacroIA(inline number: Int, inline string: String): String =
    ${
      firstMacroIAImpl('number, 'string)
    } // quoting an inline argument actually expands the entire expression, can only run basic computations (e.g. arithmetic o string concatenation)

  def firstMacroIAImpl(numAST: Expr[Int], stringAST: Expr[String])(using
      Quotes
  ): Expr[String] = Expr(
    "The number: " + numAST.show + " and the string: " + stringAST.show
  )
}
