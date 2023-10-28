package exported

import commands.ls.LsFunctionsReceiverFactory
import commands.ls.impl.LsFunctionsImpl
import functions.model.Coordinates3

import java.util.function.BiFunction

/** When running a function with i.e. the classloader transport, we have to impl this "exported.Exported" class which is loaded once by the classloader and the
  * apply() function is called for every call to our functions.
  *
  * Exported extends the java's BiFunction trait, avoiding complications of using a scala trait (because it means the caller and receiver have to load the same
  * scala class which we don't want).
  */
object Exported extends BiFunction[String, Array[Byte], Array[Byte]]:
  // Our exports implementation
  private val impl      = new LsFunctionsImpl
  // a map of coordinates -> function invocation
  private val functions = LsFunctionsReceiverFactory.invokerMap(impl)

  override def apply(coordinates: String, data: Array[Byte]): Array[Byte] =
    functions(Coordinates3(coordinates)).apply(data)
