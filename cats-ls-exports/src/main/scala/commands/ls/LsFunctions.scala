package commands.ls

import cats.effect.kernel.Async
import commands.model.{LsFile, LsOptions, LsResult}

/** The exported functions of cats effects.
  *
  * This marks this trait as exported :
  *
  * //> exported
  *
  * NOTE: for cats effects exported functions, the return type should always be F.
  */
trait LsFunctions[F[_]: Async]:
  /** We can change the http-method via a special comment. This will be changed to POST:
    *
    * //> HTTP-POST
    *
    * We need a POST here because of LsOptions.
    *
    * The 1st param set in a method are parameters that are converted to URL params and the type must be compatible with http4s, i.e. IntVar, LongVar or String.
    *
    * For the ls method, path will be part of the URL but lsOptions must be posted. See the generated LsFunctionsHttp4sRoutes routes.
    */
  def ls(path: String)(lsOptions: LsOptions = LsOptions.Defaults): F[LsResult]

  /** Use GET method for this one :
    *
    * //> HTTP-GET
    */
  def fileSize(path: String)(): F[Long]

  /** This has dir & minFileSize as part of the URL and does an http-delete call/route.
    *
    * //> HTTP-DELETE
    */
  def deleteAllWithFileSizeLessThan(dir: String, minFileSize: Long)(): F[Seq[LsFile]]
