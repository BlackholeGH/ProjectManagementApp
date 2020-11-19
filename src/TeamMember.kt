class TeamMember(var name : String, var role : String, var idNumber : Int) {
    companion object
    {
        fun decodePersistence(pString : String) : TeamMember
        {
            val params : Array<String> = pString.split(':').toTypedArray()
            val reColonize = { s : String -> s.replace("[COLON]", ":") }
            return TeamMember(reColonize(params[0]), reColonize(params[1]), params[2].toInt())
        }
    }
    fun persistenceString() : String
    {
        return name.replace(":", "[COLON]") + ":" + role.replace(":", "[COLON]") + ":" + idNumber.toString()
    }
}