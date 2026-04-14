package com.blazon.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blazon.app.data.model.*
import com.blazon.app.data.repository.MockDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun loadData(branchId: String, userId: String = "user-1") {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                val branch = MockDataRepository.getBranchById(branchId)
                val availability = MockDataRepository.getBarberAvailability(branchId)
                val promotions = MockDataRepository.getPromotionsByBranch(branchId)
                val user = MockDataRepository.getUserData(userId)
                val loyaltyInfo = MockDataRepository.getLoyaltyInfo(userId)
                val serviceProgress = MockDataRepository.getServiceProgress(userId)

                if (branch != null) {
                    _uiState.value = HomeUiState.Success(
                        branch = branch,
                        availability = availability,
                        promotions = promotions,
                        user = user,
                        loyaltyInfo = loyaltyInfo,
                        serviceProgress = serviceProgress
                    )
                } else {
                    _uiState.value = HomeUiState.Error("Branch not found")
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val branch: Branch,
        val availability: BarberAvailability?,
        val promotions: List<Promotion>,
        val user: User?,
        val loyaltyInfo: LoyaltyInfo? = null,
        val serviceProgress: List<ServiceProgress> = emptyList()
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}
