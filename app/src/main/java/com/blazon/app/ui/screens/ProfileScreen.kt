package com.blazon.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blazon.app.theme.*
import com.blazon.app.ui.components.PremiumButton
import com.blazon.app.ui.components.PremiumCard
import com.blazon.app.viewmodel.ProfileViewModel
import com.blazon.app.viewmodel.ProfileUiState

@Composable
fun ProfileScreen(
    branchId: String,
    userId: String,
    onChangeBranch: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(branchId, userId) {
        viewModel.loadData(branchId, userId)
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlazonBlack)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(BlazonCard, BlazonBlack)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Settings & Profile",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BlazonForeground
            )
        }
        
        // Content
        when (val state = uiState) {
            is ProfileUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BlazonGold)
                }
            }
            
            is ProfileUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .padding(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // User Info
                    PremiumCard {
                        Text(
                            text = "Name",
                            fontSize = 12.sp,
                            color = BlazonMutedForeground
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.user.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BlazonForeground
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Phone",
                            fontSize = 12.sp,
                            color = BlazonMutedForeground
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.user.phone,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BlazonForeground
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Loyalty Status",
                            fontSize = 12.sp,
                            color = BlazonMutedForeground
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.user.loyaltyStatus.name.uppercase(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BlazonGold
                        )
                    }
                    
                    // Current Branch
                    PremiumCard {
                        Text(
                            text = "Current Branch",
                            fontSize = 12.sp,
                            color = BlazonMutedForeground
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.branch.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BlazonForeground
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        PremiumButton(
                            text = "Change Branch",
                            onClick = onChangeBranch,
                            modifier = Modifier.fillMaxWidth(),
                            isPrimary = false
                        )
                    }
                    
                    // Loyalty Info
                    PremiumCard {
                        Text(
                            text = "Loyalty Rewards",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BlazonForeground
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total Visits",
                                fontSize = 14.sp,
                                color = BlazonMutedForeground
                            )
                            Text(
                                text = "${state.user.totalVisits}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BlazonForeground
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Discount Status",
                                fontSize = 14.sp,
                                color = BlazonMutedForeground
                            )
                            Text(
                                text = if (state.user.discountEligible) {
                                    "Unlocked"
                                } else {
                                    "${maxOf(0, 10 - state.user.totalVisits)} more visits"
                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (state.user.discountEligible) BlazonGold else BlazonMutedForeground
                            )
                        }
                    }
                    
                    // Support
                    PremiumCard {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Need Help?",
                                fontSize = 14.sp,
                                color = BlazonMutedForeground
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            PremiumButton(
                                text = "Contact Support",
                                onClick = { /* TODO */ },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    
                    // About
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Blazon v1.0.0",
                            fontSize = 12.sp,
                            color = BlazonMutedForeground
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Premium Men's Grooming",
                            fontSize = 12.sp,
                            color = BlazonMutedForeground
                        )
                    }
                }
            }
            
            is ProfileUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        color = BlazonDestructive
                    )
                }
            }
        }
    }
}

