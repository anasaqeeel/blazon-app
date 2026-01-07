package com.blazon.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blazon.app.theme.*
import com.blazon.app.ui.components.*
import com.blazon.app.viewmodel.HomeViewModel
import com.blazon.app.viewmodel.HomeUiState

@Composable
fun HomeScreen(
    branchId: String,
    onNavigateToServices: () -> Unit,
    onNavigateToMembership: () -> Unit,
    onNavigateToAvailability: () -> Unit,
    onNavigateToVisitTracking: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(branchId) {
        viewModel.loadData(branchId)
    }
    
    when (val state = uiState) {
        is HomeUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlazonBlack)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(color = BlazonGold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading...",
                        color = BlazonMutedForeground
                    )
                }
            }
        }
        
        is HomeUiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlazonBlack)
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 80.dp) // Space for bottom navigation
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
                        text = "Blazon",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlazonGold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = state.branch.name,
                        fontSize = 14.sp,
                        color = BlazonMutedForeground
                    )
                }
                
                // Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Welcome Section
                    Column {
                        Text(
                            text = "Premium Grooming",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlazonForeground
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Experience luxury at every touch",
                            fontSize = 16.sp,
                            color = BlazonMutedForeground
                        )
                    }
                    
                    // Quick Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            IconButton(
                                icon = "📋",
                                text = "Services",
                                onClick = onNavigateToServices,
                                isPrimary = true
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            IconButton(
                                icon = "💳",
                                text = "Membership",
                                onClick = onNavigateToMembership,
                                isPrimary = true
                            )
                        }
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            IconButton(
                                icon = "⏱️",
                                text = "Availability",
                                onClick = onNavigateToAvailability,
                                isPrimary = false
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            IconButton(
                                icon = "📸",
                                text = "Scan QR",
                                onClick = onNavigateToVisitTracking,
                                isPrimary = false
                            )
                        }
                    }
                    
                    // Barber Availability Card
                    state.availability?.let { availability ->
                        BarberCard(availability = availability)
                    }
                    
                    // Loyalty Status
                    state.user?.let { user ->
                        PremiumCard {
                            Text(
                                text = "Your Loyalty",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BlazonForeground
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Visits Completed",
                                    fontSize = 14.sp,
                                    color = BlazonMutedForeground
                                )
                                Text(
                                    text = "${user.totalVisits}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BlazonGold
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Remaining for Discount",
                                    fontSize = 14.sp,
                                    color = BlazonMutedForeground
                                )
                                Text(
                                    text = "${maxOf(0, 10 - user.totalVisits)}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BlazonGold
                                )
                            }
                            
                            if (user.discountEligible) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            BlazonGold.copy(alpha = 0.1f),
                                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                                        )
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "🎉 Discount Unlocked!",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = BlazonGold,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                    
                    // Promotions
                    if (state.promotions.isNotEmpty()) {
                        Column {
                            Text(
                                text = "Promotions",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BlazonForeground,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                            
                            LazyColumn(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(state.promotions) { promo ->
                                    PromotionCard(promotion = promo)
                                }
                            }
                        }
                    }
                }
            }
        }
        
        is HomeUiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlazonBlack)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
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
fun BarberCard(availability: com.blazon.app.data.model.BarberAvailability) {
    GradientCard {
        Text(
            text = "Live Status",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = BlazonForeground
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column {
                Text(
                    text = "${availability.availableBarbers}",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlazonGold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Barbers Available",
                    fontSize = 12.sp,
                    color = BlazonMutedForeground
                )
            }
            
            Column {
                Text(
                    text = "${availability.freeSlots}",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlazonGold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Free Slots",
                    fontSize = 12.sp,
                    color = BlazonMutedForeground
                )
            }
        }
    }
}

@Composable
fun PromotionCard(promotion: com.blazon.app.data.model.Promotion) {
    GradientCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = promotion.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BlazonForeground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = promotion.description,
                    fontSize = 14.sp,
                    color = BlazonMutedForeground
                )
            }
            Text(
                text = "${promotion.discount}%",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BlazonGold
            )
        }
    }
}

