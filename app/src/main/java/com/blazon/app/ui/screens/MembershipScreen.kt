package com.blazon.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    
    var showPackages by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(true) }
    var showCustomMembership by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    
    androidx.compose.runtime.LaunchedEffect(branchId) {
        viewModel.loadServices(branchId)
    }
    
    // Calculate prices with 15% discount
    val totalPrice = selectedServices.sumOf { it.price }
    val discountPercent = 15
    val discountAmount = (totalPrice * discountPercent) / 100
    val finalPrice = totalPrice - discountAmount
    
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
                    text = "Membership & Packages",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlazonForeground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Choose a package or create your own",
                    fontSize = 14.sp,
                    color = BlazonMutedForeground
                )
            }
            
            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                androidx.compose.material3.Surface(
                    onClick = { 
                        showPackages = true
                        showCustomMembership = false
                    },
                    shape = RoundedCornerShape(20.dp),
                    color = if (showPackages) BlazonGold else BlazonCard,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Packages",
                        fontSize = 14.sp,
                        fontWeight = if (showPackages) FontWeight.Bold else FontWeight.Normal,
                        color = if (showPackages) BlazonBlack else BlazonMutedForeground,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
                
                androidx.compose.material3.Surface(
                    onClick = { 
                        showPackages = false
                        showCustomMembership = true
                    },
                    shape = RoundedCornerShape(20.dp),
                    color = if (showCustomMembership) BlazonGold else BlazonCard,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Custom",
                        fontSize = 14.sp,
                        fontWeight = if (showCustomMembership) FontWeight.Bold else FontWeight.Normal,
                        color = if (showCustomMembership) BlazonBlack else BlazonMutedForeground,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
            
            // Content based on selected tab
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
                    if (showPackages) {
                        // Show Packages
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(bottom = 80.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                Text(
                                    text = "Premium Grooming Packages",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BlazonForeground,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            
                            items(com.blazon.app.data.repository.MockDataRepository.groomingPackages.filter { it.branchId == branchId }) { pkg ->
                                PackageCard(packageItem = pkg)
                            }
                        }
                    } else {
                        // Show Custom Membership
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(bottom = if (selectedServices.isNotEmpty()) 180.dp else 80.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            BlazonGold.copy(alpha = 0.15f),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // 15% OFF Badge at top
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                BlazonGold,
                                                shape = RoundedCornerShape(20.dp)
                                            )
                                            .padding(horizontal = 20.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = "15% OFF",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BlazonBlack
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    Text(
                                        text = "Create Custom Membership",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BlazonForeground,
                                        textAlign = TextAlign.Center
                                    )
                                    
                                    Spacer(modifier = Modifier.height(6.dp))
                                    
                                    Text(
                                        text = "Select services below and get 15% discount",
                                        fontSize = 14.sp,
                                        color = BlazonMutedForeground,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            
                            items(state.services) { service ->
                                MembershipServiceCard(
                                    service = service,
                                    isSelected = selectedServices.contains(service),
                                    onClick = { viewModel.toggleService(service) }
                                )
                            }
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
        
        // Summary Bottom Sheet for Custom Membership
        if (selectedServices.isNotEmpty() && showCustomMembership) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        BlazonCard,
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .padding(20.dp)
            ) {
                // Discount Badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            BlazonGold.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🎉",
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "15% Membership Discount",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = BlazonGold
                            )
                            Text(
                                text = "Save Rs. $discountAmount",
                                fontSize = 12.sp,
                                color = BlazonMutedForeground
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Price Breakdown
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            BlazonSecondary.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Subtotal",
                            fontSize = 14.sp,
                            color = BlazonMutedForeground
                        )
                        Text(
                            text = "Rs. $totalPrice",
                            fontSize = 14.sp,
                            color = BlazonForeground
                        )
                    }
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Discount (15%)",
                                fontSize = 14.sp,
                                color = BlazonMutedForeground
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .background(
                                        BlazonGold.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "-15%",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BlazonGold
                                )
                            }
                        }
                        Text(
                            text = "-Rs. $discountAmount",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlazonGold
                        )
                    }
                    
                    Divider(
                        color = BlazonSecondary,
                        thickness = 1.dp
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlazonForeground
                        )
                        Text(
                            text = "Rs. $finalPrice",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlazonGold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${selectedServices.size}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlazonGold
                        )
                        Text(
                            text = "Services",
                            fontSize = 11.sp,
                            color = BlazonMutedForeground
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${selectedServices.sumOf { it.duration }}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlazonGold
                        )
                        Text(
                            text = "Minutes",
                            fontSize = 11.sp,
                            color = BlazonMutedForeground
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                PremiumButton(
                    text = if (isCreating) "Creating Membership..." else "Create Membership",
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

@Composable
fun PackageCard(packageItem: com.blazon.app.data.model.GroomingPackage) {
    PremiumCard(
        isHighlighted = false,
        modifier = Modifier.clickable { /* TODO: Handle package selection */ }
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = packageItem.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlazonGold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = packageItem.description,
                        fontSize = 13.sp,
                        color = BlazonMutedForeground
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Rs. ${packageItem.price}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlazonGold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Pre Wedding Services
            if (packageItem.preWeddingServices.isNotEmpty()) {
                Text(
                    text = "Pre Wedding Services",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BlazonForeground
                )
                Spacer(modifier = Modifier.height(8.dp))
                packageItem.preWeddingServices.take(5).forEach { service ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = "• ",
                            fontSize = 12.sp,
                            color = BlazonGold
                        )
                        Text(
                            text = service,
                            fontSize = 12.sp,
                            color = BlazonMutedForeground
                        )
                    }
                }
                if (packageItem.preWeddingServices.size > 5) {
                    Text(
                        text = "+ ${packageItem.preWeddingServices.size - 5} more",
                        fontSize = 11.sp,
                        color = BlazonGold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            // Mehndi/Barat/Walima Services
            if (packageItem.mehndiBaratWalimaServices.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Mehndi/Barat/Walima Services",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BlazonForeground
                )
                Spacer(modifier = Modifier.height(8.dp))
                packageItem.mehndiBaratWalimaServices.take(4).forEach { service ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = "• ",
                            fontSize = 12.sp,
                            color = BlazonGold
                        )
                        Text(
                            text = service,
                            fontSize = 12.sp,
                            color = BlazonMutedForeground
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            PremiumButton(
                text = "Select Package",
                onClick = { /* TODO: Handle package selection */ },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
