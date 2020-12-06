import scala.collection.immutable.List
import java.io.*

val storedProjects = MutableList<Project?>(0) { i -> null }

class Project(var projectName: String) {
    companion object
    {
        fun decodePersistence(persistence: String) : Project
        {
            val data = persistence.split("#").map { s: String -> s.replace("[HASH]", "#") }
            val outProject = Project(data[0])
            for(taskData in data.drop(1))
            {
                if(taskData.length > 0) {
                    val task = Task.decodePersistence(taskData)
                    outProject.projectTasks.add(task)
                    outProject.taskTeam.put(task, task.taskTeam)
                }
            }
            val taskMap = outProject.projectTasks.associateBy { t: Task? -> t!!.taskID }
            for(task in outProject.projectTasks)
            {
                task!!.pumpFollowers(taskMap as Map<Long, Task>)
            }
            return outProject
        }
        fun saveProjects()
        {
            val projectsString : String = "PMA Project Archive:\n" + storedProjects.map({ t: Project? -> t!!.persistenceString() }).joinToString(
                "\n"
            )
            val projectsFile : File = File("PMAProjects.txt")
            val bw : BufferedWriter = BufferedWriter(FileWriter(projectsFile))
            bw.write(projectsString)
            bw.close()
        }
        fun loadProjects()
        {
            if(File("PMAProjects.txt").exists()) {
                storedProjects.clear()
                storedProjects.addAll(BufferedReader(FileReader(File("PMAProjects.txt"))).use { br: BufferedReader -> br.readText() }
                    .split("\n").drop(1).map { str: String -> Project.decodePersistence(str) as Project? })
            }
        }
    }
    private var remTime = 0L
    private var criticalTasks = ArrayList<Task>()
    private fun updateCritical()
    {
        if (GUI.useKotlinCriticalPath)
        {
            val r = KotlinCriticalPathTracer().returnTimeRemainingByCritPath(this)
            remTime = r[0] as Long
            criticalTasks = r[1] as ArrayList<Task>
        }
        else
        {
            val r = GUI.accessScala(this)
            remTime = r[0] as Long
            criticalTasks = ArrayList<Task>((r[1] as Array<Task>).toMutableList())
        }
    }
    fun getRemainingTime() : Long {
        updateCritical()
        return remTime
    }
    fun getCriticalPathTasks() : ArrayList<Task> {
        updateCritical()
        return criticalTasks
    }
    fun persistenceString() : String {
        val sanitize = { s: String -> s.replace("#", "[HASH]") }
        return sanitize(projectName) + "#" + projectTasks.map { t: Task? -> sanitize(t?.persistenceString() as String) }.joinToString(
            "#"
        )
    }
    fun fullDisplayDetails() : String {
        var createProjectString = "PROJECT DETAILS:\n\n"
        createProjectString += "Name: " + projectName + "\nTime remaining per critical path: " + Task.getLengthAsReadableTime(getRemainingTime()) + "\n\nProject task tree:\n\n"
        for(task in getRootTasks())
        {
            createProjectString += getTaskTree(task, "")
        }
        return createProjectString
    }
    private fun getTaskTree(task: Task, leftPadding: String) : String
    {
        var createProjectString = ""
        createProjectString += task.displayFullDetails(leftPadding)
        if(task.followingTasks.size > 0)
        {
            createProjectString += leftPadding + "Following task(s), indented:\n\n"
            for(ftask in task.followingTasks)
            {
                createProjectString += getTaskTree(ftask as Task, leftPadding + "        ")
            }
        }
        else { createProjectString += "\n" }
        return createProjectString
    }
    //Creates a deep clone of the Project
    fun clone() : Project {
        val cloneProj = Project(projectName)
        for(t : Task in getRootTasks())
        {
            cloneTasks(null, t, cloneProj)
        }
        return cloneProj
    }
    fun cloneTasks(rootTask: Task?, currentTask: Task, cloneProj: Project) {
        val trueCurrentTask = Task(currentTask.taskName, currentTask.taskDescription, currentTask.taskLength)
        cloneProj.addTask(trueCurrentTask, currentTask.taskTeam, rootTask)
        for(t2 : Task? in currentTask.followingTasks)
        {
            if(t2 != null) { cloneTasks(trueCurrentTask, t2, cloneProj) }
        }
    }
    protected val projectTasks = MutableList<Task?>(0) { i -> null }
    protected val taskTeam : MutableMap<Task?, Team?> = projectTasks.associate { t -> Pair(t, null) } as MutableMap<Task?, Team?>
    fun addTask(task: Task, team: Team?, precedingTask: Task? = null)
    {
        projectTasks.add(task)
        if(team != null) {
            task.taskTeam = team
            taskTeam.putIfAbsent(task, team)
        }
        if(precedingTask != null && projectTasks.contains(precedingTask))
        {
            precedingTask.followingTasks.add(task)
        }
    }
    fun removeTask(task: Task, restructureTree: Boolean)
    {
        projectTasks.remove(task)
        taskTeam.remove(task)
        if(restructureTree) {
            var formerFollowingTasks = task.followingTasks.toTypedArray()
            for (otherTask in projectTasks) {
                if (otherTask?.followingTasks!!.contains(task) && formerFollowingTasks.size > 0) {
                    otherTask?.followingTasks!!.addAll(formerFollowingTasks)
                }
                otherTask?.followingTasks?.remove(task)
            }
            task.followingTasks.clear()
        }
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
            var cantAdd = false
            for(otask in projectTasks) {
                if(otask!!.followingTasks.contains(task)) {
                    cantAdd = true
                    break
                }
            }
            if(!cantAdd) { tasksList.add(task) }
        }
        return tasksList.toTypedArray() as Array<Task>
    }
}