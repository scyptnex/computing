package scala

/**
  * Created by nic on 13/06/16.
  */
object Pseries extends App{

  def fact(i:Int):Int = i match {
    case 0 => 1
    case a => a*fact(i-1)
  }

  def f(x:Float) = (1f :: List.range(1,10).map(i => Math.pow(x, i)/List.range(1,i+1).product).map(i => i.asInstanceOf[Float])).sum

  val x = f(20.0000f)
  println(f"$x%1.4f")
    println(f(5.0000f))
    println(f(0.5000f))
    println(f(-0.5000f))

  println(List.range(6,5).product)
  println(List.range(1,1).product)
  println(List.range(1,4).product)
  println(List.range(1,5).product)

}
