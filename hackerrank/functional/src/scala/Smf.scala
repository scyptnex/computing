package scala

/**
  * Created by nic on 13/06/16.
  */
object Smf {
  def main(args: Array[String]) {
    println(io.Source.stdin.getLines().take(2).map(_.toInt).sum)
  }
}