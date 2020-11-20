import java.io.*

var teamsMap = mutableMapOf<Int, Team?>();

class Team (val teamName : String, val teamID : Int) {
    val teamMembers = MutableList<TeamMember?>(0) { i -> null }
    companion object
    {
        fun decodePersistence(pString : String) : Team
        {
            val params : Array<String> = pString.split('|').toTypedArray()
            val deSanitize = { s : String -> s.replace("[PIPE]", "|").replace("[SEMICOLON]", ";") }
            val decodedTeam : Team = Team(params[0], params[1].toInt())
            for(member in params[2].split(';').map { s: String -> TeamMember.decodePersistence(deSanitize(s)) }) {
                decodedTeam.teamMembers.add(member)
            }
            return decodedTeam
        }
        fun saveTeams()
        {
            val teamsSaveString : String = "PMA Teams Save Archive:\n" + teamsMap.values.map({t : Team? -> t!!.persistenceString() }).joinToString("\n")
            val teamsFile : File = File("PMATeams.txt")
            val bw : BufferedWriter = BufferedWriter(FileWriter(teamsFile))
            bw.write(teamsSaveString)
            bw.close()
        }
        fun loadTeams()
        {
            //Some functional programming with lambdas for you here ;)
            if(File("PMATeams.txt").exists()) {
                teamsMap = BufferedReader(FileReader(File("PMATeams.txt"))).use { br: BufferedReader -> br.readText() }
                    .split("\n").drop(1).map { str: String -> Team.decodePersistence(str) as Team? }
                    .associateBy { t: Team? -> t?.teamID ?: -1 } as MutableMap
            }
        }
    }
    fun persistenceString() : String
    {
        val memberSanitize = { s : String -> s.replace("|", "[PIPE]").replace(";", "[SEMICOLON]").replace("\n", " ") }
        var outString = memberSanitize(teamName) + "|" + teamID.toString() + "|" + (teamMembers.map { t -> memberSanitize(t?.persistenceString() as String) }.joinToString(";"))
        return outString
    }
}