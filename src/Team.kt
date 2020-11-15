class Team (val teamName : String, val teamID : UInt) {
    val teamMembers = MutableList<Task?>(0) { i -> null }
}