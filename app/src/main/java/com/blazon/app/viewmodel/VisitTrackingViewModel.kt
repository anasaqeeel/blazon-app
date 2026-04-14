package com.blazon.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blazon.app.data.model.Branch
import com.blazon.app.data.model.Service
import com.blazon.app.data.model.ServiceProgress
import com.blazon.app.data.repository.EarnResult
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

    private val _selectedService = MutableStateFlow<Service?>(null)
    val selectedService: StateFlow<Service?> = _selectedService.asStateFlow()

    private val _selectedServiceProgress = MutableStateFlow<ServiceProgress?>(null)
    val selectedServiceProgress: StateFlow<ServiceProgress?> = _selectedServiceProgress.asStateFlow()

    private val _isVerifying = MutableStateFlow(false)
    val isVerifying: StateFlow<Boolean> = _isVerifying.asStateFlow()

    private val _verifyResult = MutableStateFlow<VerifyResult?>(null)
    val verifyResult: StateFlow<VerifyResult?> = _verifyResult.asStateFlow()

    private val _earnResult = MutableStateFlow<EarnResult?>(null)
    val earnResult: StateFlow<EarnResult?> = _earnResult.asStateFlow()

    private var loadedUserId: String? = null

    fun loadBranch(branchId: String, userId: String) {
        loadedUserId = userId
        viewModelScope.launch {
            _uiState.value = VisitTrackingUiState.Loading
            try {
                val branch = MockDataRepository.getBranchById(branchId)
                val services = MockDataRepository.getServicesByBranch(branchId)
                _uiState.value = if (branch != null) {
                    VisitTrackingUiState.Success(branch, services)
                } else {
                    VisitTrackingUiState.Error("Branch not found")
                }
            } catch (e: Exception) {
                _uiState.value = VisitTrackingUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun simulateQRScan() { _qrScanned.value = true }

    fun simulateWiFiConnection() { _wifiConnected.value = true }

    fun selectService(service: Service) {
        _selectedService.value = service
        val userId = loadedUserId ?: return
        viewModelScope.launch {
            val progress = MockDataRepository.getServiceProgress(userId)
                .firstOrNull { it.serviceName == service.name }
            _selectedServiceProgress.value = progress
        }
    }

    fun verifyVisit(userId: String, branchId: String) {
        val service = _selectedService.value ?: return
        if (!_qrScanned.value || !_wifiConnected.value) return

        viewModelScope.launch {
            _isVerifying.value = true
            kotlinx.coroutines.delay(1200)

            val result = MockDataRepository.earnPointsForService(userId, service.id)
            _earnResult.value = result

            _verifyResult.value = VerifyResult(
                success = true,
                message = if (result.isFreeService) "Visit verified — this one's on us!"
                          else "Visit verified successfully!"
            )
            _isVerifying.value = false
        }
    }
}

sealed class VisitTrackingUiState {
    object Loading : VisitTrackingUiState()
    data class Success(val branch: Branch, val services: List<Service>) : VisitTrackingUiState()
    data class Error(val message: String) : VisitTrackingUiState()
}

data class VerifyResult(
    val success: Boolean,
    val message: String
)
