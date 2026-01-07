package com.blazon.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blazon.app.data.model.Service
import com.blazon.app.data.model.ServiceCategory
import com.blazon.app.data.repository.MockDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ServicesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<ServicesUiState>(ServicesUiState.Loading)
    val uiState: StateFlow<ServicesUiState> = _uiState.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<ServiceCategory>(ServiceCategory.Hair)
    val selectedCategory: StateFlow<ServiceCategory> = _selectedCategory.asStateFlow()

    fun loadServices(branchId: String) {
        viewModelScope.launch {
            _uiState.value = ServicesUiState.Loading
            try {
                val services = MockDataRepository.getServicesByBranch(branchId)
                _uiState.value = ServicesUiState.Success(services)
            } catch (e: Exception) {
                _uiState.value = ServicesUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    
    fun selectCategory(category: ServiceCategory) {
        _selectedCategory.value = category
    }
}

sealed class ServicesUiState {
    object Loading : ServicesUiState()
    data class Success(val services: List<Service>) : ServicesUiState()
    data class Error(val message: String) : ServicesUiState()
}

