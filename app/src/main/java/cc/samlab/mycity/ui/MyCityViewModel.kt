package cc.samlab.mycity.ui

import androidx.lifecycle.ViewModel
import cc.samlab.mycity.model.Category
import cc.samlab.mycity.model.MyCityUiState
import cc.samlab.mycity.model.Recommendation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyCityViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MyCityUiState())
    val uiState: StateFlow<MyCityUiState> = _uiState.asStateFlow()

    fun updateSelectedCategory(category : Category) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCategory = category
            )
        }
    }

    fun updateSelectedRecommendation(recommendation: Recommendation){
        _uiState.update { currentState ->
            currentState.copy(
                selectedRecommendation = recommendation
            )
        }
    }
}