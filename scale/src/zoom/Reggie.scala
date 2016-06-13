package zoom

object RegExpTest1 extends App {
  def containsScala(x: String): Boolean = {
    val z: Seq[Char] = x
    z match {
      case Seq('s','c','a','l','a', rest @ _*) =>
        println("rest is "+rest)
        true
      case Seq(_*) =>
        false
    }
  }
  println(containsScala("scalab"))
  println(containsScala("scalb"))
}