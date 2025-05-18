package fri.uniza.sk.sharpgames.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ScoreViewModelFactory : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(ScoreViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return ScoreViewModel(
        ScoreRepository(FirestoreScoreDao())
      ) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
