package client

object ThreadCounter:
  def countThreads(): Int =
    var rootGroup   = Thread.currentThread().getThreadGroup
    var parentGroup = rootGroup.getParent
    while (parentGroup != null) {
      rootGroup = parentGroup
      parentGroup = rootGroup.getParent
    }

    // Enumerate all threads in the root thread group
    var threads = new Array[Thread](rootGroup.activeCount())
    while (rootGroup.enumerate(threads, true) == threads.length) {
      threads = new Array[Thread](threads.length * 2)
    }

    // Filter out nulls and count
    threads.count(_ != null)
