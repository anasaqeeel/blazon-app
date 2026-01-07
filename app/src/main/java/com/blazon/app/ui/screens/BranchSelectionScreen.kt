package com.blazon.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blazon.app.data.model.Branch
import com.blazon.app.theme.*
import com.blazon.app.ui.components.PremiumCard
import com.blazon.app.viewmodel.BranchSelectionViewModel
import com.blazon.app.viewmodel.BranchSelectionUiState

@Composable
fun BranchSelectionScreen(
    onBranchSelected: (String) -> Unit,
    viewModel: BranchSelectionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlazonBlack)
            .padding(16.dp)
    ) {
        when (val state = uiState) {
            is BranchSelectionUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Blazon",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlazonGold
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CircularProgressIndicator(color = BlazonGold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading branches...",
                        color = BlazonMutedForeground
                    )
                }
            }
            
            is BranchSelectionUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 48.dp)
                        ) {
                            Text(
                                text = "Blazon",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = BlazonGold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Premium Men's Grooming",
                                fontSize = 14.sp,
                                color = BlazonMutedForeground
                            )
                        }
                    }
                    
                    item {
                        Text(
                            text = "Select Your Branch",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BlazonForeground,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp)
                        )
                    }
                    
                    items(state.branches) { branch ->
                        BranchCard(
                            branch = branch,
                            onClick = { onBranchSelected(branch.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        Text(
                            text = "Your selected branch determines available services, pricing, and promotions",
                            fontSize = 12.sp,
                            color = BlazonMutedForeground,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
            
            is BranchSelectionUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Error",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlazonDestructive
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.message,
                        color = BlazonMutedForeground
                    )
                }
            }
        }
    }
}

@Composable
fun BranchCard(
    branch: Branch,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PremiumCard(
        modifier = modifier
            .clickable(onClick = onClick),
        isHighlighted = false
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = branch.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BlazonForeground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = branch.address,
                    fontSize = 14.sp,
                    color = BlazonMutedForeground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = branch.phone,
                    fontSize = 12.sp,
                    color = BlazonMutedForeground
                )
            }
        }
    }
}

