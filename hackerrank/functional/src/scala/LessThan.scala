package scala

/**
  * Created by nic on 13/06/16.
  */
object LessThan extends App {

  def f(delim:Int,arr:List[Int]):List[Int] = arr.filter(i => i<delim)

  println(f(4, List()))
  println(f(4, List(1)))
  println(f(4, List(5,6,7)))

}
