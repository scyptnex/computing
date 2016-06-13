package scala

/**
  * Created by nic on 13/06/16.
  */
object OddOnes extends App{
  def f(arr:List[Int]):List[Int] = arr.zipWithIndex.filter(t => t._2%2 == 1).map(t => t._1)
  def f2(arr:List[Int]):Int = arr.filter(i => Math.abs(i)%2==1).sum
  println(f2(List()))
  println(f2(List(0)))
  println(f2(List(0, 1)))
  println(f2(List(0, 1, 2)))
  println(f2(List(0, 1, 2, 3)))
  println(f2(List(0, 1, 2, 3, 4).reverse))
  println(f2(List(0, -1, -2, -3, -4)))
}
