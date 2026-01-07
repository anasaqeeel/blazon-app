package com.blazon.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blazon.app.data.model.Branch
import com.blazon.app.data.repository.MockDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VisitTrackingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<VisitTrackingUiState>(VisitTrackingUiState.Loading)
    val uiState: StateFlow<VisitTrackingUiState> = _uiState.asStateFlow()
    
    private val _qrScanned = MutableStateFlow(false)
    val qrScanned: StateFlow<Boolean> = _qrScanned.asStateFlow()
    
    private val _wifiConnected = MutableStateFlow(false)
    val wifiConnected: StateFlow<Boolean> = _wifiConnected.asStateFlow()
    
    private val _isVerifying = MutableStateFlow(false)
    val isVerifying: StateFlow<Boolean> = _isVerifying.asStateFlow()
    
    private val _verifyResult = MutableStateFlow<VerifyResult?>(null)
    val verifyResult: StateFlow<VerifyResult?> = _verifyResult.asStateFlow()

    fun loadBranch(branchId: String) {
        viewModelScope.launch {
            _uiState.value = VisitTrackingUiState.Loading
            try {
                val branch = MockDataRepository.getBranchById(branchId)
                _uiState.value = if (branch != null) {
                    VisitTrackingUiState.Success(branch)
                } else {
                    VisitTrackingUiState.Error("Branch not found")
                }
            } catch (e: Exception) {
                _uiState.value = VisitTrackingUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun simulateQRScan() {
        _qrScanned.value = true
    }
    
    fun simulateWiFiConnection() {
        _wifiConnected.value = true
    }
    
    fun verifyVisit(userId: String, branchId: String) {
        if (!_qrScanned.value || !_wifiConnected.value) return
        
        viewModelScope.launch {
            _isVerifying.value = true
            kotlinx.coroutines.delay(1500) // Simulate API call
            _verifyResult.value = VerifyResult(
                success = true,
                message = "Visit verified successfully!"
            )
            _isVerifying.value = false
        }
    }
}

sealed class VisitTrackingUiState {
    object Loading : VisitTrackingUiState()
    data class Success(val branch: Branch) : VisitTrackingUiState()
    data class Error(val message: String) : VisitTrackingUiState()
}

data class VerifyResult(
    val success: Boolean,
    val message: String
)

