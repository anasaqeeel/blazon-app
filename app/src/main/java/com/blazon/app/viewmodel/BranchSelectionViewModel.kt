package com.blazon.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blazon.app.data.model.Branch
import com.blazon.app.data.repository.MockDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BranchSelectionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<BranchSelectionUiState>(BranchSelectionUiState.Loading)
    val uiState: StateFlow<BranchSelectionUiState> = _uiState.asStateFlow()

    init {
        loadBranches()
    }

    private fun loadBranches() {
        viewModelScope.launch {
            _uiState.value = BranchSelectionUiState.Loading
            try {
                val branches = MockDataRepository.getBranches()
                _uiState.value = BranchSelectionUiState.Success(branches)
            } catch (e: Exception) {
                _uiState.value = BranchSelectionUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class BranchSelectionUiState {
    object Loading : BranchSelectionUiState()
    data class Success(val branches: List<Branch>) : BranchSelectionUiState()
    data class Error(val message: String) : BranchSelectionUiState()
}

