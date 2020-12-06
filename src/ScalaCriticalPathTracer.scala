import java.util

import scala.jdk.CollectionConverters._

class ScalaCriticalPathTracer {
  // Trace recursive function uses mapping function to get the elements from the collection of tasks
  def returnTimeRemainingByCriticalPath(project : Project) : Array[AnyRef] = {
    def trace(t : Task) : Array[AnyRef] = Array((t.timeRemaining() + (if(t.getFollowingTasks.size > 0) { t.getFollowingTasks.asScala.map((t2 : Task) => trace(t2)(0).asInstanceOf[Long]).max } else { 0 })).asInstanceOf[AnyRef], (if(t.getFollowingTasks.size > 0) { trace(t.getFollowingTasks.asScala.maxBy((t2 : Task) => trace(t2)(0).asInstanceOf[Long]))(1).asInstanceOf[Array[Task]].toList :+ t } else { List[Task](t) }).toArray[Task].asInstanceOf[AnyRef])
    return if(project.getRootTasks.size > 0) { trace(project.getRootTasks.maxBy((t : Task) => (trace(t))(0).asInstanceOf[Long])) } else { Array(0.asInstanceOf[Long].asInstanceOf[AnyRef], List[Task]().toArray[Task].asInstanceOf[AnyRef]) }
  }
}
