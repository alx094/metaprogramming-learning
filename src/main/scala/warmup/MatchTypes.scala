package warmup

def lastDigitOf(number: BigInt): Int = (number % 10).toInt

def lastCharOf(string: String): Char =
  if string.isEmpty then throw new NoSuchElementException
  else string.charAt(string.length - 1)

def lastElementOf[A](list: List[A]): A =
  if list.isEmpty then throw new NoSuchElementException
  else list.last

type ConstituentPartOf[A] = A match {
  case BigInt  => Int
  case String  => Char
  case List[a] => a
}

val aNumber: ConstituentPartOf[BigInt] = 2
val aChar: ConstituentPartOf[String] = 'a'
val anElement: ConstituentPartOf[List[Int]] = 22

def lastPartOf[A](thing: A): ConstituentPartOf[A] = thing match
  case number: BigInt => lastDigitOf(number)
  case string: String => lastCharOf(string)
  case list: List[_]  => lastElementOf(list)

val lastPartOfString: Char = lastPartOf("Scala")

//recursion on match types
type LowestLevelPartOf[A] = A match {
  case List[a] => LowestLevelPartOf[a]
  case _       => A
}

val lastPartOfNestedList: LowestLevelPartOf[List[List[List[List[Int]]]]] = 10

@main def main() = {}
