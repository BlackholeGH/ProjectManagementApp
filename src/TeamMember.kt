class TeamMember(var name : String, var role : String, var idNumber : Int) {

    //The use of Null safety feature to prevent NullPointerException errors.
    companion object
    {
        fun decodePersistence(pString : String) : TeamMember?
        {
            val params : Array<String> = pString.split(':').toTypedArray()
            if(params.size <= 1) { return null }
            val reColonize = { s : String -> s.replace("[COLON]", ":") }
            return TeamMember(reColonize(params[0]), reColonize(params[1]), params[2].toInt())
        }
    }
    //Returns team member names, roles and ID numbers to PMATeams text file.
    fun persistenceString() : String
    {
        return name.replace(":", "[COLON]") + ":" + role.replace(":", "[COLON]") + ":" + idNumber.toString()
    }
}

