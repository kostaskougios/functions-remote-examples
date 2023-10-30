import functions.coursier.Resolver._
import Versions._

// This will create a text file with all of receiver's dependencies. This way the IsolatedClassLoaderTransport
// will be able to load all jars without the transport depending on any external libraries.
// The text files are under ~/.functions-remote-config/local/dependencies
createDependenciesForArtifact(s"com.example:ls-receiver_3:$LsVersion")
