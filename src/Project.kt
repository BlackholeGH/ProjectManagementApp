import java.util.Collections.*;

class Project (var projectName : String) {
    protected val projectTasks = MutableList<Task?>(0) { i -> null }
    protected val taskTeam : MutableMap<Task?, Team?> = projectTasks.associate { t -> Pair(t, null) } as MutableMap<Task?, Team?>
    fun addTask(task : Task, team : Team, precedingTask : Task? = null)
    {
        task.taskTeam = team
        projectTasks.add(task)
        taskTeam.putIfAbsent(task, team)
        if(precedingTask != null && projectTasks.contains(precedingTask))
        {
            precedingTask.followingTasks.add(task)
        }
    }
    fun removeTask(task : Task)
    {
        projectTasks.remove(task)
        taskTeam.remove(task)
        var formerFollowingTasks = task.followingTasks.toTypedArray()
        for(otherTask in projectTasks) {
            if (otherTask?.followingTasks!!.contains(task) && formerFollowingTasks.size > 0) {
                otherTask?.followingTasks!!.addAll(formerFollowingTasks)
            }
            otherTask?.followingTasks?.remove(task)
        }
        task.followingTasks.clear()
    }
    fun getProjectTeams() : Array<Team>
    {
        val teamsList = MutableList<Team?>(0) { i -> null }
        for(Task in projectTasks)
        {
            if(!teamsList.contains(taskTeam[Task]))
            {
                teamsList.add(taskTeam[Task])
            }
        }
        return teamsList as Array<Team>
    }
    fun getRootTasks() : Array<Task>
    {
        val tasksList = MutableList<Task?>(0) { i -> null }
        for(task in projectTasks)
        {
            if(task?.followingTasks?.size == 0)
            {
                tasksList.add(task)
            }
        }
        return tasksList as Array<Task>
    }
}