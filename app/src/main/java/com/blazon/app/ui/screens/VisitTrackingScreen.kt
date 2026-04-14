package com.blazon.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blazon.app.data.model.Service
import com.blazon.app.data.model.ServiceCategory
import com.blazon.app.theme.*
import com.blazon.app.ui.components.PremiumButton
import com.blazon.app.ui.components.PremiumCard
import com.blazon.app.viewmodel.VisitTrackingViewModel
import com.blazon.app.viewmodel.VisitTrackingUiState

@Composable
fun VisitTrackingScreen(
    branchId: String,
    userId: String,
    viewModel: VisitTrackingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val qrScanned by viewModel.qrScanned.collectAsState()
    val wifiConnected by viewModel.wifiConnected.collectAsState()
    val selectedService by viewModel.selectedService.collectAsState()
    val selectedProgress by viewModel.selectedServiceProgress.collectAsState()
    val isVerifying by viewModel.isVerifying.collectAsState()
    val verifyResult by viewModel.verifyResult.collectAsState()
    val earnResult by viewModel.earnResult.collectAsState()

    LaunchedEffect(branchId, userId) {
        viewModel.loadBranch(branchId, userId)
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
                    brush = Brush.verticalGradient(listOf(BlazonCard, BlazonBlack))
                )
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Verify Visit",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BlazonForeground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Earn 10% back on every service — every 11th is free",
                fontSize = 13.sp,
                color = BlazonMutedForeground
            )
        }

        when (val state = uiState) {
            is VisitTrackingUiState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BlazonGold)
                }
            }
            is VisitTrackingUiState.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.message, color = BlazonDestructive)
                }
            }
            is VisitTrackingUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                        .padding(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Branch
                    PremiumCard {
                        Text("Branch", fontSize = 12.sp, color = BlazonMutedForeground)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            state.branch.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BlazonForeground
                        )
                    }

                    // QR
                    PremiumCard(isHighlighted = qrScanned) {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("1. QR Code Scan", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = BlazonForeground)
                            Spacer(Modifier.height(6.dp))
                            Text("Scan the QR at the salon entrance", fontSize = 13.sp, color = BlazonMutedForeground)
                            Spacer(Modifier.height(12.dp))
                            if (qrScanned) {
                                StatusPill("QR Verified")
                            } else {
                                PremiumButton(
                                    text = "Simulate QR Scan",
                                    onClick = { viewModel.simulateQRScan() },
                                    modifier = Modifier.fillMaxWidth(),
                                    isPrimary = false
                                )
                            }
                        }
                    }

                    // Wi-Fi
                    PremiumCard(isHighlighted = wifiConnected) {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("2. Wi-Fi Connection", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = BlazonForeground)
                            Spacer(Modifier.height(6.dp))
                            Text("Connect to ${state.branch.wifiNetwork}", fontSize = 13.sp, color = BlazonMutedForeground)
                            Spacer(Modifier.height(12.dp))
                            if (wifiConnected) {
                                StatusPill("Wi-Fi Connected")
                            } else {
                                PremiumButton(
                                    text = "Simulate Wi-Fi Connection",
                                    onClick = { viewModel.simulateWiFiConnection() },
                                    modifier = Modifier.fillMaxWidth(),
                                    isPrimary = false
                                )
                            }
                        }
                    }

                    // Service picker
                    PremiumCard(isHighlighted = selectedService != null) {
                        Text(
                            "3. Select the Service You Received",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BlazonForeground
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Points & free-visit progress are tracked per service",
                            fontSize = 12.sp,
                            color = BlazonMutedForeground
                        )
                        Spacer(Modifier.height(12.dp))

                        // Category chips
                        val categories = state.services.map { it.category }.distinct()
                        val selectedCategory = selectedService?.category ?: categories.firstOrNull()
                        var currentCategory by remember {
                            mutableStateOf(selectedCategory ?: categories.first())
                        }
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(categories) { cat ->
                                CategoryPill(
                                    label = cat.name,
                                    selected = cat == currentCategory,
                                    onClick = { currentCategory = cat }
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))

                        // Services for chosen category
                        val filtered = state.services.filter { it.category == currentCategory }
                        filtered.forEach { service ->
                            ServicePickRow(
                                service = service,
                                isSelected = selectedService?.id == service.id,
                                onClick = { viewModel.selectService(service) }
                            )
                            Spacer(Modifier.height(6.dp))
                        }

                        // Selected preview
                        selectedService?.let { svc ->
                            Spacer(Modifier.height(8.dp))
                            val progress = selectedProgress
                            val isFreeReady = progress?.isFreeReady == true
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isFreeReady) BlazonGold.copy(alpha = 0.18f)
                                        else BlazonSecondary,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(12.dp)
                            ) {
                                Column {
                                    if (isFreeReady) {
                                        Text(
                                            "THIS ONE IS FREE — 11th visit reward!",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BlazonGold
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            "${svc.name} normally Rs. ${svc.price}. Counter will reset after.",
                                            fontSize = 12.sp,
                                            color = BlazonMutedForeground
                                        )
                                    } else {
                                        Text(
                                            "${svc.name} — Rs. ${svc.price}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = BlazonForeground
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            "You'll earn +${svc.price / 10} points (10% back)",
                                            fontSize = 12.sp,
                                            color = BlazonGold
                                        )
                                        val paid = progress?.paidVisits ?: 0
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            "Progress: $paid/10 paid visits → 11th is FREE",
                                            fontSize = 11.sp,
                                            color = BlazonMutedForeground
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Verify button
                    if (verifyResult == null) {
                        PremiumButton(
                            text = when {
                                isVerifying -> "Verifying..."
                                !qrScanned || !wifiConnected -> "Complete verifications above"
                                selectedService == null -> "Select a service first"
                                else -> "Verify Visit & Claim Reward"
                            },
                            onClick = { viewModel.verifyVisit(userId, branchId) },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = qrScanned && wifiConnected && selectedService != null && !isVerifying
                        )
                    }

                    // Verify result
                    verifyResult?.let { result ->
                        PremiumCard(isHighlighted = result.success) {
                            Text(
                                result.message,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (result.success) BlazonGold else BlazonDestructive
                            )
                        }
                    }

                    // Earn result
                    earnResult?.let { earned ->
                        PremiumCard(isHighlighted = true) {
                            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    if (earned.isFreeService) "Free ${earned.serviceName}!"
                                    else "Points Earned!",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BlazonGold
                                )
                                Spacer(Modifier.height(14.dp))

                                if (earned.isFreeService) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(BlazonGold.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                            .padding(14.dp)
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                                            Text(
                                                "Saved Rs. ${earned.servicePrice}",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = BlazonGold
                                            )
                                            Spacer(Modifier.height(4.dp))
                                            Text(
                                                "Counter reset — keep going for the next free one!",
                                                fontSize = 12.sp,
                                                color = BlazonMutedForeground,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                } else {
                                    SummaryRow("Service (10% back)", "+${earned.pointsEarned}", BlazonForeground)
                                    if (earned.streakBonus > 0) {
                                        Spacer(Modifier.height(6.dp))
                                        SummaryRow(
                                            "Streak Bonus (${earned.newStreak}-visit)",
                                            "+${earned.streakBonus}",
                                            BlazonGold
                                        )
                                    }
                                    Spacer(Modifier.height(10.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(BlazonSecondary, RoundedCornerShape(10.dp))
                                            .padding(12.dp)
                                    ) {
                                        SummaryRow("Total Earned", "+${earned.totalEarned} pts", BlazonGold, bold = true)
                                    }
                                    Spacer(Modifier.height(10.dp))
                                    Text(
                                        "${earned.serviceName}: ${earned.newPaidVisitCount}/10 toward next free",
                                        fontSize = 12.sp,
                                        color = BlazonMutedForeground
                                    )
                                }

                                Spacer(Modifier.height(10.dp))
                                Text(
                                    "New Balance: ${earned.newBalance} pts",
                                    fontSize = 13.sp,
                                    color = BlazonMutedForeground
                                )

                                if (earned.tierUp) {
                                    Spacer(Modifier.height(12.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(BlazonGold.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                            .padding(12.dp)
                                    ) {
                                        Text(
                                            "TIER UPGRADED! New rewards unlocked.",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BlazonGold,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusPill(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(BlazonGold.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = BlazonGold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CategoryPill(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(
                if (selected) BlazonGold else BlazonSecondary,
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) BlazonBlack else BlazonMutedForeground
        )
    }
}

@Composable
private fun ServicePickRow(service: Service, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(
                if (isSelected) BlazonGold.copy(alpha = 0.12f) else BlazonSecondary,
                RoundedCornerShape(10.dp)
            )
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) BlazonGold else androidx.compose.ui.graphics.Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                service.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = BlazonForeground
            )
            Text(
                "${service.duration} min",
                fontSize = 11.sp,
                color = BlazonMutedForeground
            )
        }
        Text(
            "Rs. ${service.price}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = BlazonGold
        )
    }
}

@Composable
private fun SummaryRow(label: String, value: String, valueColor: androidx.compose.ui.graphics.Color, bold: Boolean = false) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, fontSize = 14.sp, color = BlazonMutedForeground)
        Text(
            value,
            fontSize = if (bold) 18.sp else 15.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.SemiBold,
            color = valueColor
        )
    }
}
