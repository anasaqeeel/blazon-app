package com.blazon.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blazon.app.data.model.Service
import com.blazon.app.data.repository.MockDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MembershipViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<MembershipUiState>(MembershipUiState.Loading)
    val uiState: StateFlow<MembershipUiState> = _uiState.asStateFlow()
    
    private val _selectedServices = MutableStateFlow<List<Service>>(emptyList())
    val selectedServices: StateFlow<List<Service>> = _selectedServices.asStateFlow()
    
    private val _isCreating = MutableStateFlow(false)
    val isCreating: StateFlow<Boolean> = _isCreating.asStateFlow()

    fun loadServices(branchId: String) {
        viewModelScope.launch {
            _uiState.value = MembershipUiState.Loading
            try {
                val services = MockDataRepository.getServicesByBranch(branchId)
                _uiState.value = MembershipUiState.Success(services)
            } catch (e: Exception) {
                _uiState.value = MembershipUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun toggleService(service: Service) {
        val current = _selectedServices.value.toMutableList()
        if (current.contains(service)) {
            current.remove(service)
        } else {
            current.add(service)
        }
        _selectedServices.value = current
    }
    
    fun createMembership(userId: String, branchId: String) {
        if (_selectedServices.value.isEmpty()) return
        
        viewModelScope.launch {
            _isCreating.value = true
            // Simulate API call
            kotlinx.coroutines.delay(1000)
            _isCreating.value = false
            _selectedServices.value = emptyList()
        }
    }
}

sealed class MembershipUiState {
    object Loading : MembershipUiState()
    data class Success(val services: List<Service>) : MembershipUiState()
    data class Error(val message: String) : MembershipUiState()
}

