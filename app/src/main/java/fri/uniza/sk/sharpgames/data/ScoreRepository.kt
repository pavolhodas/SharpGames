package fri.uniza.sk.sharpgames.data

class ScoreRepository(private val scoreDao: ScoreDao) {
  suspend fun addScore(score: Score): String = scoreDao.addScore(score)
  suspend fun getHighestScore(gameName: String): Int = scoreDao.getHighestScore(gameName)
}
