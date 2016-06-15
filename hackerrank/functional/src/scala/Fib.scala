package scala

object Fib extends App{
  def fibonacci(x:Int):Int = {
    def fib(n:Int, t:(Int, Int)):(Int,Int) = if(n <= 1) t else fib(n-1, (t._1 + t._2, t._1))
    fib(x, (1, 0))._2
  }
  println(List.range(1, 10).map(fibonacci))
}
