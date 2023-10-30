package commands.ls

import cats.effect.kernel.Async
import commands.model.{LsOptions, LsResult}

/** The exported functions of cats effects.
  *
  * This marks this trait as exported : //> exported
  *
  * NOTE: for cats effects exported functions, the return type should always be F.
  */
trait LsFunctions[F[_]: Async]:
  def ls(path: String, lsOptions: LsOptions = LsOptions.Defaults): F[LsResult]
  def fileSize(path: String): F[Long]
