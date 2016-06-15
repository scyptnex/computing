package scala

import scala.io.StdIn

/**
5
abc
abcde
abab
aaa
z
  */
object StringRot {

  def main(args: Array[String]): Unit = {
    def rots(s:String):List[String] = List.range(1,s.length+1).map(i => s.substring(i) + s.substring(0, i))
    for(i <- 1 to StdIn.readInt()){
      println(rots(StdIn.readLine()).mkString(" "))
    }
  }

}
