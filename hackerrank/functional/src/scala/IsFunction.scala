package scala

/*
2
3
1 1
2 2
3 3
4
1 2
2 4
3 6
4 8
 */

object IsFunction {
  def main(args: Array[String]) {
    val lst = io.Source.stdin.getLines()
    for(ln <- 0 until lst.next().trim.toInt){
      var pairs = List.empty[(Int, Int)]
      lst.take(lst.next().trim.toInt).map(_.trim).map(s => s.split(" ").map(_.toInt)).map(a => (a(0), a(1))).foreach(i => pairs :+= i)
      val mp = pairs.toMap
      if(pairs.forall(t => mp(t._1) == t._2)) println("YES")
      else println("NO")
    }
  }
}
