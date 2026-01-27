package inlines

object InlinesConditions {

  inline def condition(b: Boolean) =
    inline if b then "yes" else "no"

  val aCondition = condition(true)
  /*
   * Compiler will evaluate the inline condition at compile time,
   * */

// Compiler can do basic computation
  val positive = condition(true && !false) // also known at compile time

//val variable = true
//val question = condition(variable)
// does not compile because the variable is not know statically

  transparent inline def conditionUnion(b: Boolean): String | Int =
    inline if b then "yes" else 0

  val aString = conditionUnion(true) // known to be a String
  val anInt = conditionUnion(true && !true) // known to be an Int

  inline def matcher(i: Int) = inline i match {
    case 1 => "one"
    case 2 => "two"
    case _ => "Other"
  }

  val one = matcher(1) // known to be one

  transparent inline def matcherT(i: Int): String | Int = inline i match {
    case 1 => 1
    case 2 => "two"
    case _ => 0
  }

  val theOne = matcherT(1) // known to be an Int

  transparent inline def matcherOption(opt: Option[Any]) = inline opt match {
    case Some(value: String) => value
    case Some(value: Int)    => value.toString
    case None                => "Nothing"
  }

  val something = matcherOption(Some("Something"))
// val aBoolean = matcherOption(Some(true)) // does not compile

  val anOption = Option("known at run time")
// val aPerfectString = matcherOption(anOption) // Does not compile because it does not know it is a Some at compile time

// Recursion
// The compiler can reduce the value at compile time if inline the if statement
  inline def sum(x: Int): Int =
    inline if x <= 0 then 0 else x + sum(x - 1)

  val ten = sum(4) // compiler can reduce to ten
// recursion has its limits
// val aBigSum = sum(4_000_000) // => overflow

// transparent can know the type
  transparent inline def sumT(x: Int): Int =
    inline if x <= 0 then 0 else x + sumT(x - 1)

  val tenT: 10 = sumT(4)

}
