import java.lang.Math

var taskIDCount : ULong = 0 as ULong

class Task (val taskName : String, var taskDescription : String, val taskLength : ULong) {
    companion object {
        fun getLengthAsReadableTime(timeLength : ULong) : String {
            var timeOut : String = (timeLength % 60u).toString() + " second" + if(timeLength % 60u != 1uL) { "s " } else { " " }
            if(timeLength >= 60u) {
                timeOut = ((timeLength % 3600u) / 60u).toString() + " minute" + if(((timeLength % 3600u) / 60u) != 1uL) { "s" } else { "" } + ", " + timeOut
                if(timeLength >= 3600u) {
                    timeOut = ((timeLength % 86400u) / 3600u).toString() + " hour" + if(((timeLength % 86400u) / 3600u) != 1uL) { "s" } else { "" } + ", " + timeOut
                    if(timeLength >= 86400u) {
                        timeOut = ((timeLength % 640800u) / 86400u).toString() + " day" + if(((timeLength % 640800u) / 86400u) != 1uL) { "s" } else { "" } + ", " + timeOut
                        if(timeLength >= 640800u) {
                            timeOut = (timeLength / 640800u).toString() + " week" + if((timeLength / 640800u) != 1uL) { "s" } else { "" } + ", " + timeOut
                        }
                    }
                }
            }
            return timeOut
        }
    }
    var taskProgress = 0f
        set(progVal : Float)
        {
            taskProgress = Math.max(0f, Math.min(1f, progVal))
        }
    fun timeRemaining() : ULong {
        return (taskLength as Float * (1f - taskProgress)) as ULong
    }
    var taskTeam : Team? = null
    var taskInstanceProject : Project? = null
    val taskID = taskIDCount
    init {
        taskIDCount = taskIDCount + 1 as ULong
    }
    override fun equals(other: Any?): Boolean {
        if (other is Task) { return (other as Task).taskID == taskID }
        return super.equals(other)
    }
    val followingTasks = MutableList<Task?>(0) { i -> null }
}