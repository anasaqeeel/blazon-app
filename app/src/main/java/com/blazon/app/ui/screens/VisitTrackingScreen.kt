package com.blazon.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
    val isVerifying by viewModel.isVerifying.collectAsState()
    val verifyResult by viewModel.verifyResult.collectAsState()
    
    LaunchedEffect(branchId) {
        viewModel.loadBranch(branchId)
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
                text = "Verify Visit",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BlazonForeground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Complete verification to log your visit",
                fontSize = 14.sp,
                color = BlazonMutedForeground
            )
        }
        
        // Content
        when (val state = uiState) {
            is VisitTrackingUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BlazonGold)
                }
            }
            
            is VisitTrackingUiState.Success -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .padding(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Branch Info
                    PremiumCard {
                        Text(
                            text = "Branch",
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
                    }
                    
                    // QR Code Verification
                    PremiumCard(isHighlighted = qrScanned) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "📱",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "QR Code Scan",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BlazonForeground
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Scan the QR code at the salon entrance",
                                fontSize = 14.sp,
                                color = BlazonMutedForeground
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            if (qrScanned) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            BlazonGold.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "✓ QR Code Verified",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = BlazonGold,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
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
                    
                    // Wi-Fi Verification
                    PremiumCard(isHighlighted = wifiConnected) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "📡",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Wi-Fi Connection",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = BlazonForeground
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Connect to ${state.branch.wifiNetwork}",
                                fontSize = 14.sp,
                                color = BlazonMutedForeground
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            if (wifiConnected) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            BlazonGold.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "✓ Wi-Fi Connected",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = BlazonGold,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
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
                    
                    // Verify Button
                    if (verifyResult == null) {
                        PremiumButton(
                            text = when {
                                isVerifying -> "Verifying..."
                                qrScanned && wifiConnected -> "Verify Visit"
                                else -> "Complete Both Verifications"
                            },
                            onClick = { viewModel.verifyVisit(userId, branchId) },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = qrScanned && wifiConnected && !isVerifying
                        )
                    }
                    
                    // Result
                    verifyResult?.let { result ->
                        PremiumCard(
                            isHighlighted = result.success
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (result.success) "✓" else "✗",
                                    fontSize = 32.sp,
                                    color = if (result.success) BlazonGold else BlazonDestructive
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = result.message,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (result.success) BlazonGold else BlazonDestructive
                                )
                                if (result.success) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Your visit has been logged to your loyalty account!",
                                        fontSize = 14.sp,
                                        color = BlazonMutedForeground
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            is VisitTrackingUiState.Error -> {
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

