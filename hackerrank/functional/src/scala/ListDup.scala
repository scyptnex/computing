package scala

/**
  * Created by nic on 13/06/16.
  */
object ListDup extends App {

  def f(num:Int,arr:List[Int]):List[Int] = arr.flatMap(i => List.fill(num)(i))

  println(f(4, List()))
  println(f(4, List(1)))
  println(f(4, List(5,6,7)))

}
