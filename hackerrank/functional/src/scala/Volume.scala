package scala

/**
  * Created by nic on 13/06/16.
  */
object Volume extends App{
  def f(coefficients:List[Int],powers:List[Int],x:Double):Double = coefficients.zip(powers).map(t => t._1*Math.pow(x, t._2)).sum

  def area(coefficients:List[Int],powers:List[Int],x:Double):Double = Math.pow(f(coefficients, powers, x), 2)*Math.PI

  def summation(func:(List[Int],List[Int],Double)=>Double,upperLimit:Int,lowerLimit:Int,coefficients:List[Int],powers:List[Int]):Double = List.range(lowerLimit*1000 + 1, upperLimit*1000 + 1).map(i=>i/1000.0).map(d => func(coefficients, powers, d)*0.001).sum

  println(summation(f, 4,1,List(1, 2, 3, 4, 5), List(6, 7, 8, 9, 10)))
  println(summation(area, 4,1,List(1, 2, 3, 4, 5), List(6, 7, 8, 9, 10)))
}
