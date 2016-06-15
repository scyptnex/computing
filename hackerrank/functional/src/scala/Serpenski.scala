package scala

object Serpenski extends App{
  def drawTriangles(n: Int) {
    def serp(arr:Array[Array[String]], llcr:Int, llcc:Int, height:Int, iter:Int):Array[Array[String]] = {
      def fl(row:Int, col:Int, width:Int, s:String) = List.range(col, col+width).foreach(i => arr(row).update(i, s))
      def uptri(llcr:Int, llcc:Int, height:Int) = List.range(0, height).foreach(i => fl(llcr-i, llcc+i, (2*(height-i))-1, "1"))
      if(iter == 0) uptri(llcr, llcc, height)
      else{
        serp(arr, llcr, llcc, height/2, iter-1)
        serp(arr, llcr, llcc+height, height/2, iter-1)
        serp(arr, llcr-height/2, llcc+height/2, height/2, iter-1)
      }
      arr
    }
    println(serp(Array.fill[String](32,63)("_"), 31,0,32,n).map(arr => arr.mkString("")).mkString("\n"))
  }
  for( i <- 0 to 5){
    drawTriangles(i)
  }
}
