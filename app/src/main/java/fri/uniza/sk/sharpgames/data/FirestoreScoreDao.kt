package fri.uniza.sk.sharpgames.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreScoreDao : ScoreDao {
  private val db = FirebaseFirestore.getInstance()
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

  override suspend fun getHighestScore(gameName: String): Int {
    return try {
      val querySnapshot = scoreCollection
        .whereEqualTo("gameName", gameName)
        .orderBy("score", com.google.firebase.firestore.Query.Direction.DESCENDING)
        .limit(1)
        .get()
        .await()

      querySnapshot.documents
        .firstOrNull()
        ?.getLong("score")
        ?.toInt() ?: 0

    } catch (e: Exception) {
      Log.e("Firestore", "Error fetching high score", e)
      0
    }
  }

}
