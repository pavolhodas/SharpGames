package fri.uniza.sk.sharpgames.data


//inspiracia celej firebase implementacie z youtube:
//https://www.youtube.com/playlist?list=PLIIWAqaTrNlg7q0cfajkBj8OwG60qpBVL
class ScoreRepository(private val scoreDao: ScoreDao) {
  suspend fun addScore(score: Score): String = scoreDao.addScore(score)
  suspend fun getHighestScore(gameName: String): Int = scoreDao.getHighestScore(gameName)
}
