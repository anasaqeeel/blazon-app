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
import com.blazon.app.theme.*
import com.blazon.app.ui.components.PremiumButton
import com.blazon.app.ui.components.PremiumCard
import com.blazon.app.viewmodel.MembershipViewModel
import com.blazon.app.viewmodel.MembershipUiState

@Composable
fun MembershipScreen(
    branchId: String,
    userId: String,
    viewModel: MembershipViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedServices by viewModel.selectedServices.collectAsState()
    val isCreating by viewModel.isCreating.collectAsState()
    
    androidx.compose.runtime.LaunchedEffect(branchId) {
        viewModel.loadServices(branchId)
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlazonBlack)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
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
                    text = "Create Membership",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlazonForeground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Select services to include",
                    fontSize = 14.sp,
                    color = BlazonMutedForeground
                )
            }
            
            // Services List
            when (val state = uiState) {
                is MembershipUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = BlazonGold)
                    }
                }
                
                is MembershipUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(bottom = 100.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.services) { service ->
                            MembershipServiceCard(
                                service = service,
                                isSelected = selectedServices.contains(service),
                                onClick = { viewModel.toggleService(service) }
                            )
                        }
                    }
                }
                
                is MembershipUiState.Error -> {
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
        
        // Summary Bottom Sheet
        if (selectedServices.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        BlazonCard,
                        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${selectedServices.size}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlazonGold
                        )
                        Text(
                            text = "Services",
                            fontSize = 12.sp,
                            color = BlazonMutedForeground
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${selectedServices.sumOf { it.duration }}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlazonGold
                        )
                        Text(
                            text = "Minutes",
                            fontSize = 12.sp,
                            color = BlazonMutedForeground
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Rs. ${selectedServices.sumOf { it.price }}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlazonGold
                        )
                        Text(
                            text = "Total",
                            fontSize = 12.sp,
                            color = BlazonMutedForeground
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                PremiumButton(
                    text = if (isCreating) "Creating..." else "Create Membership",
                    onClick = { viewModel.createMembership(userId, branchId) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isCreating
                )
            }
        }
    }
}

@Composable
fun MembershipServiceCard(
    service: com.blazon.app.data.model.Service,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    PremiumCard(
        isHighlighted = isSelected,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BlazonForeground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = service.category.name,
                    fontSize = 12.sp,
                    color = BlazonMutedForeground
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Rs. ${service.price}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlazonGold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${service.duration} min",
                    fontSize = 12.sp,
                    color = BlazonMutedForeground
                )
            }
        }
        
        if (isSelected) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "✓ Selected",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = BlazonGold
            )
        }
    }
}

