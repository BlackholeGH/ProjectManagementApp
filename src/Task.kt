import java.io.*
import java.lang.Math

var taskIDCount : Long = 0
val templateTasks = mutableMapOf<String, Task?>();

class Task (val taskName : String, var taskDescription : String, val taskLength : Long) {
    companion object {
        fun getLengthAsReadableTime(timeLength : Long) : String {
            var timeOut : String = (timeLength % 60).toString() + " second" + if(timeLength % 60 != 1L) { "s " } else { " " }
            if(timeLength >= 60) {
                timeOut = ((timeLength % 3600) / 60).toString() + " minute" + if(((timeLength % 3600) / 60) != 1L) { "s" } else { "" } + ", " + timeOut
                if(timeLength >= 3600) {
                    timeOut = ((timeLength % 86400) / 3600).toString() + " hour" + if(((timeLength % 86400) / 3600) != 1L) { "s" } else { "" } + ", " + timeOut
                    if(timeLength >= 86400) {
                        timeOut = ((timeLength % 640800) / 86400).toString() + " day" + if(((timeLength % 640800) / 86400) != 1L) { "s" } else { "" } + ", " + timeOut
                        if(timeLength >= 640800) {
                            timeOut = (timeLength / 640800).toString() + " week" + if((timeLength / 640800) != 1L) { "s" } else { "" } + ", " + timeOut
                        }
                    }
                }
            }
            return timeOut
        }
        fun getLengthAsTimeArray(timeLength : Long) : IntArray {
            var outArray : IntArray = IntArray(5);
            outArray[0] = (timeLength % 60).toInt()
            outArray[1] = ((timeLength % 3600) / 60).toInt()
            outArray[2] = ((timeLength % 86400) / 3600).toInt()
            outArray[3] = ((timeLength % 640800) / 86400).toInt()
            outArray[4] = (timeLength / 640800).toInt()
            return outArray;
        }

        // Saves task information and decodes persistence string which returns their respective task items
        // Also fills in the preceding task

        fun decodePersistence(persistenceString : String) : Task {
            val poutArray : Array<String> = persistenceString.split("|").toTypedArray()
            val deSanitize = { s : String -> s.replace("[PIPE]", "|") }
            val outTask : Task = Task(deSanitize(poutArray[1]), deSanitize(poutArray[2]), poutArray[3].toLong())
            outTask.taskID = poutArray[0].toLong()
            if(outTask.taskID >= taskIDCount) { taskIDCount = outTask.taskID + 1 }
            outTask.taskProgress = poutArray[4].toFloat()
            try {
                outTask.taskTeam = teamsMap[Integer.parseInt(poutArray[5])]
            }
            catch(e : Exception) { }
            if(poutArray[6].length > 0) { outTask.ftIDsTempStore.addAll(poutArray[6].split(";").map{ s : String -> if(s.length > 0) { s.toLong() } else { 0 } }) }
            return outTask
        }
        fun saveTaskTemplates()
        {
            val taskTemplatesString : String = "PMA Task Template Save Archive:\n" + templateTasks.values.map({t : Task? -> t!!.persistenceString() }).joinToString("\n")
            val teamsFile : File = File("PMATaskTemplates.txt")
            val bw : BufferedWriter = BufferedWriter(FileWriter(teamsFile))
            bw.write(taskTemplatesString)
            bw.close()
        }
        fun loadTaskTemplates()
        {
            if(File("PMATaskTemplates.txt").exists()) {
                templateTasks.clear()
                templateTasks.putAll(BufferedReader(FileReader(File("PMATaskTemplates.txt"))).use { br: BufferedReader -> br.readText() }
                    .split("\n").drop(1).map { str: String -> Task.decodePersistence(str) as Task? }
                    .associateBy { t: Task? -> t!!.taskName })
            }
        }
    }
    fun persistenceString() : String {
        val sanitize = { s : String -> s.replace("|", "[PIPE]").replace("\n", " ") }
        return taskID.toString() + "|" + sanitize(taskName) + "|" + sanitize(taskDescription) + "|" + taskLength + "|" + taskProgress + "|" + (taskTeam?.teamID ?: "") + "|" + followingTasks.map { t : Task? -> t?.taskID.toString() }.joinToString(";")
    }
    fun displaySummary() : String {
        return taskName + "\n\n" + taskDescription + "\n\nProjected length: " + getLengthAsReadableTime(taskLength)
    }

    // Full details displayed on main viewport screen
    fun displayFullDetails(leftPadding : String) : String {
        var details = leftPadding + "Task name: " + taskName + "\n"
        details = details + leftPadding + "Task ID: " + taskID + "\n"
        details = details + leftPadding + "Estimated total time: " + getLengthAsReadableTime(taskLength) + "\n"
        details = details + leftPadding + "Estimated time remaining: " + getLengthAsReadableTime(timeRemaining()) + "\n"
        details = details + leftPadding + "Task description: " + taskDescription + "\n"
        details = details + leftPadding + "Assigned team: " + (taskTeam?.teamName ?: "No assigned team") + "\n"
        return details
    }
    var taskProgress = 0f
        set(progVal : Float)
        {
            field = Math.max(0f, Math.min(1f, progVal))
        }
    fun timeRemaining() : Long {
        return (taskLength.toFloat() * (1f - taskProgress)).toLong()
    }
    var taskTeam : Team? = null
    var taskInstanceProject : Project? = null
    var taskID = taskIDCount
    init {
        taskIDCount = taskIDCount + 1L
    }
    override fun equals(other: Any?): Boolean {
        if (other is Task) { return (other as Task).taskID == taskID }
        return super.equals(other)
    }
    val followingTasks = MutableList<Task?>(0) { i -> null }
    val ftIDsTempStore = MutableList<Long>(0) { i -> 0 }
    fun pumpFollowers(taskMap : Map<Long, Task>)
    {
        followingTasks.clear()
        followingTasks.addAll(ftIDsTempStore.map { l : Long -> taskMap[l] })
    }
}