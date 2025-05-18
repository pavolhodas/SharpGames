package fri.uniza.sk.sharpgames.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScoreViewModel(private val repository: ScoreRepository) : ViewModel() {
  // Using Compose's State<T> instead of LiveData
  private val _uiState = MutableStateFlow<ScoreUiState>(ScoreUiState.Empty)
  val uiState: StateFlow<ScoreUiState> = _uiState.asStateFlow()

  fun addScore(gameName: String, gameScore: Int) {
    viewModelScope.launch {
      _uiState.value = ScoreUiState.Loading
      try {
        val score = Score(gameName = gameName, score = gameScore)
        val documentId = repository.addScore(score)
        _uiState.value = ScoreUiState.Success(documentId)
      } catch (e: Exception) {
        _uiState.value = ScoreUiState.Error(e.message ?: "Unknown error")
      }
    }
  }

  sealed class ScoreUiState {
    object Empty : ScoreUiState()
    object Loading : ScoreUiState()
    data class Success(val documentId: String) : ScoreUiState()
    data class Error(val message: String) : ScoreUiState()
  }
}
