package commands.ls

import cats.effect.kernel.Async

/** to export:
  *
  * //> exported
  */
trait StressTestFunctions[F[_]: Async]:
  /** //> HTTP-GET
    */
  def add(a: Int, b: Int)(): F[Int]
