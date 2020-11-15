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
    }
    var taskProgress = 0f
        set(progVal : Float)
        {
            taskProgress = Math.max(0f, Math.min(1f, progVal))
        }
    fun timeRemaining() : Long {
        return (taskLength as Float * (1f - taskProgress)) as Long
    }
    var taskTeam : Team? = null
    var taskInstanceProject : Project? = null
    val taskID = taskIDCount
    init {
        taskIDCount = taskIDCount + 1L
    }
    override fun equals(other: Any?): Boolean {
        if (other is Task) { return (other as Task).taskID == taskID }
        return super.equals(other)
    }
    val followingTasks = MutableList<Task?>(0) { i -> null }
}