package scala

/**
  * Created by nic on 13/06/16.
  */
object Volume extends App{
  def f(coefficients:List[Int],powers:List[Int],x:Double):Double =
  {
    coefficients.zip(powers).map(t => t._1*Math.pow(x, t._2)).sum
  }


  def area(coefficients:List[Int],powers:List[Int],x:Double):Double =
  {
    0
  }

  // This is the part where the series is summed up
  // This function is invoked once with func = f to compute the area         // under the curve
  // Then it is invoked again with func = area to compute the volume
  // of revolution of the curve
  def summation(func:(List[Int],List[Int],Double)=>Double,upperLimit:Int,lowerLimit:Int,coefficients:List[Int],powers:List[Int]):Double =
  {
    List.range(lowerLimit*1000, upperLimit*1000).map(i=>i/1000.0).map(d => func(coefficients, powers, d)*0.001).sum
  }

  println(summation(f, 4,1,List(1, 2, 3, 4, 5), List(6, 7, 8, 9, 10)))
}
