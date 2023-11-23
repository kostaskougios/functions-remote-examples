package examples.helidon

import examples.helidon.model.{LsFile, LsOptions, LsResult}

/** The exported functions for the helidon demo.
  *
  * This marks this trait as exported :
  *
  * //> exported
  *
  * NOTE: this should be run with jdk21 or newer and uses fiber to avoid blocking. Despite the calls seemingly been synchronous, in fact there is no
  * thread-blocking.
  */
trait HelidonFunctions:
  /** We can change the http-method via a special comment. This will be changed to POST:
    *
    * //> HTTP-POST
    *
    * We need a POST here because of LsOptions. The default method is PUT.
    *
    * The 1st param set in a method are parameters that are converted to URL params and the type must be primitives, Int, Long, String are supported.
    *
    * For the `ls` method, path will be part of the URL but lsOptions must be posted. See the generated LsFunctionsHelidonRoutes routes.
    */
  def ls(path: String)(lsOptions: LsOptions = LsOptions.Defaults): LsResult

  /** Use GET method for this one :
    *
    * //> HTTP-GET
    *
    * Note because of the http-get, the 2nd param set has to be empty because GET doesn't accept data in the body of the request.
    */
  def fileSize(path: String)(): Long

  /** This has dir & minFileSize as part of the URL and does an http-delete call/route.
    *
    * //> HTTP-DELETE
    */
  def deleteAllWithFileSizeLessThan(dir: String, minFileSize: Long)(): Seq[LsFile]
