package com.blazon.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blazon.app.data.model.*
import com.blazon.app.data.repository.MockDataRepository
import com.blazon.app.data.repository.RedeemResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RewardsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<RewardsUiState>(RewardsUiState.Loading)
    val uiState: StateFlow<RewardsUiState> = _uiState.asStateFlow()

    private val _redeemResult = MutableStateFlow<RedeemResult?>(null)
    val redeemResult: StateFlow<RedeemResult?> = _redeemResult.asStateFlow()

    private val _isRedeeming = MutableStateFlow(false)
    val isRedeeming: StateFlow<Boolean> = _isRedeeming.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    fun loadData(userId: String) {
        viewModelScope.launch {
            _uiState.value = RewardsUiState.Loading
            try {
                val loyaltyInfo = MockDataRepository.getLoyaltyInfo(userId)
                val allRewards = MockDataRepository.getAllRewards()
                val transactions = MockDataRepository.getPointsHistory(userId)
                val challenges = MockDataRepository.getChallenges(userId)
                val serviceProgress = MockDataRepository.getServiceProgress(userId)

                if (loyaltyInfo != null) {
                    _uiState.value = RewardsUiState.Success(
                        loyaltyInfo = loyaltyInfo,
                        allRewards = allRewards,
                        recentTransactions = transactions.take(5),
                        challenges = challenges,
                        serviceProgress = serviceProgress
                    )
                } else {
                    _uiState.value = RewardsUiState.Error("Could not load rewards data")
                }
            } catch (e: Exception) {
                _uiState.value = RewardsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun selectTab(tab: Int) {
        _selectedTab.value = tab
    }

    fun redeemReward(userId: String, rewardId: String) {
        viewModelScope.launch {
            _isRedeeming.value = true
            _redeemResult.value = null
            val result = MockDataRepository.redeemReward(userId, rewardId)
            _redeemResult.value = result

            if (result.success) {
                // Reload data to reflect updated points
                loadData(userId)
            }
            _isRedeeming.value = false
        }
    }

    fun clearRedeemResult() {
        _redeemResult.value = null
    }
}

sealed class RewardsUiState {
    object Loading : RewardsUiState()
    data class Success(
        val loyaltyInfo: LoyaltyInfo,
        val allRewards: List<Reward>,
        val recentTransactions: List<PointsTransaction>,
        val challenges: List<Challenge>,
        val serviceProgress: List<ServiceProgress> = emptyList()
    ) : RewardsUiState()
    data class Error(val message: String) : RewardsUiState()
}
