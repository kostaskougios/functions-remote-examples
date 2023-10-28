package commands.ls

import commands.model.{LsOptions, LsResult}

/** The exported functions of ls module
  *
  * This marks this trait as exported : //> exported
  */
trait LsFunctions:
  def ls(path: String, lsOptions: LsOptions = LsOptions.Defaults): LsResult
  def fileSize(path: String): Long
