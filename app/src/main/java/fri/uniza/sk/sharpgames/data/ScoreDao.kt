package fri.uniza.sk.sharpgames.data


//inspiracia celej firebase implementacie z youtube:
//https://www.youtube.com/playlist?list=PLIIWAqaTrNlg7q0cfajkBj8OwG60qpBVL
interface ScoreDao {
  suspend fun addScore(score: Score): String
  suspend fun getHighestScore(gameName: String): Int
}
