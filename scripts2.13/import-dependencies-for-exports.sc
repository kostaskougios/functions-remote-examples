import functions.coursier.Resolver._
import Versions._

// This creates a text file with 1 dependency per line for ls-exports, find it under
// ~/.functions-remote-config/local/dependencies. Normally an "export" module should
// have no dependencies to other libraries.
createDependenciesForArtifact(s"com.example:ls-exports_3:$LsVersion")
