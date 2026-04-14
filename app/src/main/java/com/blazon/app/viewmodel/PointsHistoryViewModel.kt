package com.blazon.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blazon.app.data.model.LoyaltyInfo
import com.blazon.app.data.model.PointsTransaction
import com.blazon.app.data.repository.MockDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PointsHistoryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<PointsHistoryUiState>(PointsHistoryUiState.Loading)
    val uiState: StateFlow<PointsHistoryUiState> = _uiState.asStateFlow()

    fun loadHistory(userId: String) {
        viewModelScope.launch {
            _uiState.value = PointsHistoryUiState.Loading
            try {
                val transactions = MockDataRepository.getPointsHistory(userId)
                val loyaltyInfo = MockDataRepository.getLoyaltyInfo(userId)

                if (loyaltyInfo != null) {
                    _uiState.value = PointsHistoryUiState.Success(
                        transactions = transactions,
                        loyaltyInfo = loyaltyInfo
                    )
                } else {
                    _uiState.value = PointsHistoryUiState.Error("Could not load history")
                }
            } catch (e: Exception) {
                _uiState.value = PointsHistoryUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class PointsHistoryUiState {
    object Loading : PointsHistoryUiState()
    data class Success(
        val transactions: List<PointsTransaction>,
        val loyaltyInfo: LoyaltyInfo
    ) : PointsHistoryUiState()
    data class Error(val message: String) : PointsHistoryUiState()
}
