package scala

object Pascal {
  def main(args:Array[String]) {
    def pascal(n:Int, last:List[Int]) {
      println(last.mkString(" "))
      if(n > 1) pascal(n-1, (0::last).zip(last:+0).map(t => t._1 + t._2))
    }
    pascal(scala.io.StdIn.readInt, List(1))
  }
}
