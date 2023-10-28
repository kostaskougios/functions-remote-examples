package commands.ls.impl

import commands.ls.LsFunctions
import commands.model.{LsFile, LsOptions, LsResult}

class LsFunctionsImpl extends LsFunctions:
  override def fileSize(path: String): Long = path.length * 1000

  override def ls(path: String, lsOptions: LsOptions): LsResult =
    LsResult(Seq(LsFile(path + "/file1"), LsFile(path + "/file2")))
