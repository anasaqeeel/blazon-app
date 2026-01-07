package com.blazon.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blazon.app.data.model.Branch
import com.blazon.app.data.model.User
import com.blazon.app.data.repository.MockDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadData(branchId: String, userId: String) {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            try {
                val branch = MockDataRepository.getBranchById(branchId)
                val user = MockDataRepository.getUserData(userId)
                
                if (branch != null && user != null) {
                    _uiState.value = ProfileUiState.Success(branch, user)
                } else {
                    _uiState.value = ProfileUiState.Error("Data not found")
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val branch: Branch, val user: User) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

