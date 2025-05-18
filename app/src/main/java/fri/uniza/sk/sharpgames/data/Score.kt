package fri.uniza.sk.sharpgames.data


//inspiracia celej firebase implementacie z youtube:
//https://www.youtube.com/playlist?list=PLIIWAqaTrNlg7q0cfajkBj8OwG60qpBVL
data class Score(
    val id: Long = 0,
    val gameName: String = "",
    val score: Int = 0
)
