package warmup

//s- interpolator
val aValue = 3.14159
val aString = s"This is a string with value $aValue"

// f-interpolator
val fInterpolator = f"The value of PI ip to 3 sig digits is $aValue%3.2f"

// raw-interpolator
val rawInterpolator = raw"The value of pi is $aValue\n this is NOT a new line"

case class Person(name: String, age: Int)
def string2Person(line: String): Person =
  val tokens = line.split(",")
  Person(tokens(0), tokens(1).toInt)

//Custom interpolator
// StringContext + extension method
//
extension (sc: StringContext)
  def pers(args: Any*): Person = {
    val concat = sc.s(args*)
    string2Person(concat)
  }

val alex = pers"Alex,31"
val name = "Alex"
val age = 31
val alex_v2 = pers"$name,$age"

@main def print(): Unit = {
  println(aString)
  println(fInterpolator)
  println(rawInterpolator)
  println(alex)
  println(alex_v2)
}
