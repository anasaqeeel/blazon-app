package com.blazon.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.blazon.app.data.model.ServiceCategory
import com.blazon.app.theme.*
import com.blazon.app.ui.components.PremiumButton
import com.blazon.app.ui.components.PremiumCard
import com.blazon.app.viewmodel.ServicesViewModel
import com.blazon.app.viewmodel.ServicesUiState

@Composable
fun ServicesScreen(
    branchId: String,
    viewModel: ServicesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    
    androidx.compose.runtime.LaunchedEffect(branchId) {
        viewModel.loadServices(branchId)
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
                text = "Our Services",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BlazonForeground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Premium grooming options",
                fontSize = 14.sp,
                color = BlazonMutedForeground
            )
        }
        
        // Category Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ServiceCategory.values().forEach { category ->
                CategoryChip(
                    category = category,
                    isSelected = selectedCategory == category,
                    onClick = { viewModel.selectCategory(category) }
                )
            }
        }
        
        // Services List
        when (val state = uiState) {
            is ServicesUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    CircularProgressIndicator(color = BlazonGold)
                }
            }
            
            is ServicesUiState.Success -> {
                val filteredServices = state.services.filter { it.category == selectedCategory }
                
                if (filteredServices.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = androidx.compose.ui.Alignment.Center
                    ) {
                        Text(
                            text = "No services available in this category",
                            color = BlazonMutedForeground
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 80.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filteredServices) { service ->
                            ServiceCard(service = service)
                        }
                    }
                }
            }
            
            is ServicesUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = androidx.compose.ui.Alignment.Center
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

@Composable
fun CategoryChip(
    category: ServiceCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    androidx.compose.material3.Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) BlazonGold else BlazonCard,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = category.name,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) BlazonBlack else BlazonMutedForeground,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun ServiceCard(service: com.blazon.app.data.model.Service) {
    PremiumCard {
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
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "${service.duration} min",
                        fontSize = 14.sp,
                        color = BlazonMutedForeground
                    )
                }
                service.description?.let { description ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = BlazonMutedForeground
                    )
                }
            }
            Column(
                horizontalAlignment = androidx.compose.ui.Alignment.End
            ) {
                Text(
                    text = "Rs. ${service.price}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlazonGold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        PremiumButton(
            text = "Add to Membership",
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth(),
            isPrimary = true
        )
    }
}

