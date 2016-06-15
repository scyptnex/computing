package scala

object IsFunction {
  def main(args: Array[String]) {
    val lst = io.Source.stdin.getLines()
    for(ln <- 0 until lst.next().trim.toInt){
      val pairs = lst.take(lst.next().trim.toInt).map(_.trim).map(s => s.split(" ").map(_.toInt)).map(a => (a(0), a(1))).toList
      val mp = pairs.toMap
      if(pairs.forall(t => mp(t._1) == t._2)) println("YES")
      else println("NO")
    }
  }
}
