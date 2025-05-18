package fri.uniza.sk.sharpgames.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class FirestoreScoreDao : ScoreDao {
  private val db = Firebase.firestore
  private val scoreCollection = db.collection("score")

  override suspend fun addScore(score: Score): String {
    return try {
      // Convert to map (Firestore prefers maps)
      val scoreMap = mapOf(
        "gameName" to score.gameName,
        "score" to score.score,
      )

      // Add document and await the result
      val documentReference = scoreCollection.add(scoreMap).await()
      documentReference.id // Return the generated ID
    } catch (e: Exception) {
      throw Exception("Failed to add score", e)
    }
  }
}
