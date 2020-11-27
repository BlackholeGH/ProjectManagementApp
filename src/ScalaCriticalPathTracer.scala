import scala.jdk.CollectionConverters._

class ScalaCriticalPathTracer {
  // Trace recursive function uses mapping function to get the elements from the collection of tasks
  def returnTimeRemainingByCriticalPath(project : Project) : Long = {
    def trace(t : Task) : Long = t.timeRemaining() + (if(t.getFollowingTasks.size > 0) { t.getFollowingTasks.asScala.map((t2 : Task) => trace(t2)).max } else { 0 })
    return if(project.getRootTasks.size > 0) { project.getRootTasks.map((t : Task) => trace(t)).max } else { 0 }
  }
}
