
trait Similarity {
  def isSimilar(x: Any): Boolean

  def isNotSimilar(x: Any): Boolean = !isSimilar(x)
}

class Point2(xc: Int, yc: Int) extends Similarity {
  var x: Int = xc
  var y: Int = yc

  def isSimilar(obj: Any) = {
    println(this + " - " + obj)
    obj.isInstanceOf[Point2] &&
      obj.asInstanceOf[Point2].x == this.x }

  override def toString(): String = "(" + x + "," + y + ")"
}

object TraitsTest extends App {
  val p1 = new Point2(2, 3)
  val p2 = new Point2(2, 4)
  val p3 = new Point2(3, 3)
  println(p1.isNotSimilar(p2))
  println(p1.isNotSimilar(p3))
  println(p1.isNotSimilar(2))
}