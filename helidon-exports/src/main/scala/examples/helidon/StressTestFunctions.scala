package examples.helidon

/** to export:
  *
  * //> exported
  */
trait StressTestFunctions:
  /** //> HTTP-GET
    */
  def add(a: Int, b: Int)(): Int
