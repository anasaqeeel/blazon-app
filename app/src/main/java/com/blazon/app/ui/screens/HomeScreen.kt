package com.blazon.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CardMembership
import androidx.compose.material.icons.outlined.ContentCut
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blazon.app.data.model.LoyaltyInfo
import com.blazon.app.theme.*
import com.blazon.app.ui.components.*
import com.blazon.app.viewmodel.HomeUiState
import com.blazon.app.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    branchId: String,
    userId: String,
    onNavigateToServices: () -> Unit,
    onNavigateToMembership: () -> Unit,
    onNavigateToAvailability: () -> Unit,
    onNavigateToVisitTracking: () -> Unit,
    onNavigateToRewards: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(branchId) {
        viewModel.loadData(branchId, userId)
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
                    horizontalAlignment = Alignment.CenterHorizontally,
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
                            brush = Brush.verticalGradient(
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

                    // Points Quick Card (NEW - tappable to go to Rewards)
                    state.loyaltyInfo?.let { info ->
                        GradientCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onNavigateToRewards() }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            Icons.Outlined.Stars,
                                            contentDescription = "Points",
                                            tint = BlazonGold,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "Blazon Rewards",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = BlazonForeground
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        verticalAlignment = Alignment.Bottom,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = "${info.totalPoints}",
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BlazonGold
                                        )
                                        Text(
                                            text = "points",
                                            fontSize = 14.sp,
                                            color = BlazonMutedForeground,
                                            modifier = Modifier.padding(bottom = 5.dp)
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                when (info.tier) {
                                                    com.blazon.app.data.model.LoyaltyStatus.Gold -> BlazonGold
                                                    com.blazon.app.data.model.LoyaltyStatus.Silver -> BlazonMutedForeground
                                                    com.blazon.app.data.model.LoyaltyStatus.Bronze -> BlazonMuted
                                                },
                                                shape = RoundedCornerShape(16.dp)
                                            )
                                            .padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = info.tier.name.uppercase(),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BlazonBlack
                                        )
                                    }

                                    if (info.currentStreak > 0) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                Icons.Outlined.LocalFireDepartment,
                                                contentDescription = "Streak",
                                                tint = BlazonGold,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = "${info.currentStreak} streak",
                                                fontSize = 12.sp,
                                                color = BlazonGold,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Free Visit Progress (per-service "every 11th free" tracker)
                    if (state.serviceProgress.isNotEmpty()) {
                        FreeVisitProgressCard(
                            progress = state.serviceProgress.take(4),
                            onClick = onNavigateToRewards
                        )
                    }

                    // Quick Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            IconButton(
                                icon = Icons.Outlined.ContentCut,
                                iconDescription = "Services",
                                text = "Services",
                                onClick = onNavigateToServices,
                                isPrimary = true
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            IconButton(
                                icon = Icons.Outlined.CardMembership,
                                iconDescription = "Membership",
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
                                icon = Icons.Outlined.Schedule,
                                iconDescription = "Availability",
                                text = "Availability",
                                onClick = onNavigateToAvailability,
                                isPrimary = false
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            IconButton(
                                icon = Icons.Outlined.QrCodeScanner,
                                iconDescription = "Scan QR",
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
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = "Discount Unlocked!",
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

                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 4.dp)
                            ) {
                                items(state.promotions) { promo ->
                                    PromotionCard(
                                        promotion = promo,
                                        modifier = Modifier.width(280.dp)
                                    )
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
fun FreeVisitProgressCard(
    progress: List<com.blazon.app.data.model.ServiceProgress>,
    onClick: () -> Unit
) {
    PremiumCard(
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Free Visit Progress",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = BlazonForeground
            )
            Text(
                text = "Every 11th = FREE",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = BlazonGold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        progress.forEach { p ->
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = p.serviceName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = BlazonForeground
                    )
                    Text(
                        text = if (p.isFreeReady) "READY!" else "${p.paidVisits}/10",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (p.isFreeReady) BlazonGold else BlazonMutedForeground
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                androidx.compose.material3.LinearProgressIndicator(
                    progress = if (p.isFreeReady) 1f else p.progressFraction,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = BlazonGold,
                    trackColor = BlazonSecondary
                )
                if (!p.isFreeReady) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "${p.visitsUntilFree} more to unlock (saves Rs. ${p.price})",
                        fontSize = 10.sp,
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
fun PromotionCard(
    promotion: com.blazon.app.data.model.Promotion,
    modifier: Modifier = Modifier
) {
    GradientCard(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = promotion.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BlazonForeground,
                    modifier = Modifier.weight(1f)
                )
                if (promotion.discount > 0) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${promotion.discount}%",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlazonGold
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = promotion.description,
                fontSize = 13.sp,
                color = BlazonMutedForeground,
                lineHeight = 18.sp
            )
        }
    }
}
