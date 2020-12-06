class KotlinCriticalPathTracer {
    fun returnTimeRemainingByCritPath(project : Project) : Array<Any>
    {
        var maxLen = 0L
        var pathTasks : ArrayList<Task> = ArrayList<Task>()
        for(ftask in project.getRootTasks())
        {
            val thisLength : Array<Any> = getLongestBranch(ftask)
            if(thisLength[0] as Long > maxLen)
            {
                maxLen = thisLength[0] as Long
                pathTasks = thisLength[1] as ArrayList<Task>
            }
        }
        return arrayOf(maxLen as Any, pathTasks as Any)
    }
    private fun getLongestBranch(task : Task) : Array<Any>
    {
        var lengthValue = task.timeRemaining()
        var pathTasks : ArrayList<Task> = ArrayList<Task>()
        pathTasks.add(task)
        if(task.followingTasks.size > 0)
        {
            var maxLen = 0L
            for(ftask in task.followingTasks)
            {
                val thisLength = getLongestBranch(ftask as Task)
                if(thisLength[0] as Long > maxLen)
                {
                    maxLen = thisLength[0] as Long
                    pathTasks = thisLength[1] as ArrayList<Task>
                    pathTasks.add(task)
                }
            }
            lengthValue += maxLen
        }
        return arrayOf(lengthValue as Any, pathTasks as Any)
    }
}