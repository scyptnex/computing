package scala

object Gcd extends App {
  def gcd(x: Int, y: Int): Int =
    y match {
      case _ if x < y => gcd(y, x)
      case 0 => x
      case _ => gcd(y, x%y)
    }
  println(gcd(5, 25))
  println(gcd(10, 25))
}
