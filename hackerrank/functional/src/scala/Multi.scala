package scala

/**
  * Created by nic on 13/06/16.
  */
object Multi {

  def f(n: Int) = List.range(0,n).map(_ => "Hello World").foreach(s => println(s))

  def main(args: Array[String]) {
    f(0)
    f(4)
  }
}
