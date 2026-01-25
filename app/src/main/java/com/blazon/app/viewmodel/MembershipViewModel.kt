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
    
    // Map of service ID to quantity (max 5)
    private val _serviceQuantities = MutableStateFlow<Map<String, Int>>(emptyMap())
    val serviceQuantities: StateFlow<Map<String, Int>> = _serviceQuantities.asStateFlow()
    
    private val _isCreating = MutableStateFlow(false)
    val isCreating: StateFlow<Boolean> = _isCreating.asStateFlow()
    
    companion object {
        const val MAX_QUANTITY = 5
    }

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
    
    fun increaseQuantity(service: Service) {
        val current = _serviceQuantities.value.toMutableMap()
        val currentQty = current[service.id] ?: 0
        if (currentQty < MAX_QUANTITY) {
            current[service.id] = currentQty + 1
            _serviceQuantities.value = current
        }
    }
    
    fun decreaseQuantity(service: Service) {
        val current = _serviceQuantities.value.toMutableMap()
        val currentQty = current[service.id] ?: 0
        if (currentQty > 0) {
            if (currentQty == 1) {
                current.remove(service.id)
            } else {
                current[service.id] = currentQty - 1
            }
            _serviceQuantities.value = current
        }
    }
    
    fun getQuantity(serviceId: String): Int {
        return _serviceQuantities.value[serviceId] ?: 0
    }
    
    fun getTotalPrice(): Int {
        val state = _uiState.value
        if (state !is MembershipUiState.Success) return 0
        
        return _serviceQuantities.value.entries.sumOf { (serviceId, qty) ->
            val service = state.services.find { it.id == serviceId }
            (service?.price ?: 0) * qty
        }
    }
    
    fun getTotalDuration(): Int {
        val state = _uiState.value
        if (state !is MembershipUiState.Success) return 0
        
        return _serviceQuantities.value.entries.sumOf { (serviceId, qty) ->
            val service = state.services.find { it.id == serviceId }
            (service?.duration ?: 0) * qty
        }
    }
    
    fun getTotalServices(): Int {
        return _serviceQuantities.value.values.sum()
    }
    
    fun hasSelectedServices(): Boolean {
        return _serviceQuantities.value.isNotEmpty()
    }
    
    fun createMembership(userId: String, branchId: String) {
        if (!hasSelectedServices()) return
        
        viewModelScope.launch {
            _isCreating.value = true
            // Simulate API call
            kotlinx.coroutines.delay(1000)
            _isCreating.value = false
            _serviceQuantities.value = emptyMap()
        }
    }
    
    // Legacy support - kept for compatibility
    private val _selectedServices = MutableStateFlow<List<Service>>(emptyList())
    val selectedServices: StateFlow<List<Service>> = _selectedServices.asStateFlow()
    
    fun toggleService(service: Service) {
        val currentQty = getQuantity(service.id)
        if (currentQty > 0) {
            decreaseQuantity(service)
        } else {
            increaseQuantity(service)
        }
    }
}

sealed class MembershipUiState {
    object Loading : MembershipUiState()
    data class Success(val services: List<Service>) : MembershipUiState()
    data class Error(val message: String) : MembershipUiState()
}
