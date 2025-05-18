package fri.uniza.sk.sharpgames.data

class ScoreRepository(private val scoreDao: ScoreDao) {
  suspend fun addScore(score: Score): String = scoreDao.addScore(score)
}
