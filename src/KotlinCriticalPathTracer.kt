class KotlinCriticalPathTracer {
    fun returnTimeRemainingByCritPath(project : Project) : Long
    {
        var maxLen = 0L
        for(ftask in project.getRootTasks())
        {
            val thisLength = getLongestBranch(ftask)
            if(thisLength > maxLen)
            {
                maxLen = thisLength
            }
        }
        return maxLen
    }
    private fun getLongestBranch(task : Task) : Long
    {
        var lengthValue = task.timeRemaining()
        if(task.followingTasks.size > 0)
        {
            var maxLen = 0L
            for(ftask in task.followingTasks)
            {
                val thisLength = getLongestBranch(ftask as Task)
                if(thisLength > maxLen)
                {
                    maxLen = thisLength
                }
            }
            lengthValue += maxLen
        }
        return lengthValue
    }
}