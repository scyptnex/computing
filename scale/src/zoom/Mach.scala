package zoom

object MatchTest1 extends scala.App {
  def matchTestA(x: Int): String = x match {
    case 1 => "one"
    case 2 => "two"
    case _ => "many"
  }
  println(matchTestA(3))
  println(matchTestA(2))
  println(matchTestA(1))
  def matchTestB(x: Any): Any = x match {
    case 1 => "one"
    case "two" => 2
    case y: Int => "scala.Int"
  }
  println(matchTestB(1))
  println(matchTestB("two"))
  println(matchTestB(2))
  println(matchTestB("hi"))
}