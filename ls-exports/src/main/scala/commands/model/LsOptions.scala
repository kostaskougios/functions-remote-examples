package commands.model

case class LsOptions(includeDirs: Boolean = true)

object LsOptions:
  val Defaults = LsOptions()
