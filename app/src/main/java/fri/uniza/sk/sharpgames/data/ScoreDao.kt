package fri.uniza.sk.sharpgames.data

interface ScoreDao {
  suspend fun addScore(score: Score): String
  suspend fun getHighestScore(gameName: String): Int
}
