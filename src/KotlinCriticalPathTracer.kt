class KotlinCriticalPathTracer {
    fun returnTimeRemainingByCritPath(project : Project) : Long
    // This recursive function gets the root tasks and then traverses through the branches until it finds the value of
    // the longest critical path.
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

    // Another recursive function which scans through all the tasks and finds the longest consecutive task from the
    // chain
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