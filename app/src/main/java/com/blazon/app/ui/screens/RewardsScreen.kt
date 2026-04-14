package com.blazon.app.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import java.util.Date
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blazon.app.data.model.*
import com.blazon.app.theme.*
import com.blazon.app.ui.components.GradientCard
import com.blazon.app.ui.components.PremiumButton
import com.blazon.app.ui.components.PremiumCard
import com.blazon.app.viewmodel.RewardsUiState
import com.blazon.app.viewmodel.RewardsViewModel

@Composable
fun RewardsScreen(
    userId: String,
    onNavigateToHistory: () -> Unit,
    viewModel: RewardsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val redeemResult by viewModel.redeemResult.collectAsState()
    val isRedeeming by viewModel.isRedeeming.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadData(userId)
    }
//    claude wala

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
                    brush = Brush.verticalGradient(
                        colors = listOf(BlazonCard, BlazonBlack)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Blazon Rewards",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BlazonForeground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Earn points, unlock rewards",
                fontSize = 14.sp,
                color = BlazonMutedForeground
            )
        }

        when (val state = uiState) {
            is RewardsUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BlazonGold)
                }
            }

            is RewardsUiState.Success -> {
                // Redeem success/error dialog
                redeemResult?.let { result ->
                    AlertDialog(
                        onDismissRequest = { viewModel.clearRedeemResult() },
                        title = {
                            Text(
                                text = if (result.success) "Reward Redeemed!" else "Cannot Redeem",
                                color = BlazonForeground
                            )
                        },
                        text = {
                            Text(
                                text = result.message,
                                color = BlazonMutedForeground
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = { viewModel.clearRedeemResult() }) {
                                Text("OK", color = BlazonGold)
                            }
                        },
                        containerColor = BlazonCard
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Points Balance Card
                    item {
                        PointsBalanceCard(
                            loyaltyInfo = state.loyaltyInfo,
                            onHistoryClick = onNavigateToHistory
                        )
                    }

                    // Tier Progress Card
                    item {
                        TierProgressCard(loyaltyInfo = state.loyaltyInfo)
                    }

                    // Streak Card
                    item {
                        StreakCard(loyaltyInfo = state.loyaltyInfo)
                    }

                    // Free Visit Progress (every 11th of the same service is FREE)
                    if (state.serviceProgress.isNotEmpty()) {
                        item {
                            FreeVisitProgressCard(
                                progress = state.serviceProgress,
                                onClick = {}
                            )
                        }
                    }

                    // Tab Selector
                    item {
                        TabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = BlazonCard,
                            contentColor = BlazonGold
                        ) {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = { viewModel.selectTab(0) },
                                text = {
                                    Text(
                                        "Rewards",
                                        color = if (selectedTab == 0) BlazonGold else BlazonMutedForeground,
                                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { viewModel.selectTab(1) },
                                text = {
                                    Text(
                                        "Challenges",
                                        color = if (selectedTab == 1) BlazonGold else BlazonMutedForeground,
                                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                            Tab(
                                selected = selectedTab == 2,
                                onClick = { viewModel.selectTab(2) },
                                text = {
                                    Text(
                                        "Tier Info",
                                        color = if (selectedTab == 2) BlazonGold else BlazonMutedForeground,
                                        fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }

                    // Tab Content
                    when (selectedTab) {
                        0 -> {
                            // Rewards Catalog
                            val availableRewards = state.allRewards.filter { reward ->
                                when (state.loyaltyInfo.tier) {
                                    LoyaltyStatus.Gold -> true
                                    LoyaltyStatus.Silver -> reward.requiredTier != LoyaltyStatus.Gold
                                    LoyaltyStatus.Bronze -> reward.requiredTier == LoyaltyStatus.Bronze
                                }
                            }
                            val lockedRewards = state.allRewards - availableRewards.toSet()

                            if (availableRewards.isNotEmpty()) {
                                item {
                                    Text(
                                        text = "Available Rewards",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = BlazonForeground
                                    )
                                }
                                items(availableRewards) { reward ->
                                    RewardCard(
                                        reward = reward,
                                        userPoints = state.loyaltyInfo.totalPoints,
                                        isLocked = false,
                                        isRedeeming = isRedeeming,
                                        onRedeem = { viewModel.redeemReward(userId, reward.id) }
                                    )
                                }
                            }

                            if (lockedRewards.isNotEmpty()) {
                                item {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Locked Rewards (Upgrade Tier)",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = BlazonMutedForeground
                                    )
                                }
                                items(lockedRewards) { reward ->
                                    RewardCard(
                                        reward = reward,
                                        userPoints = state.loyaltyInfo.totalPoints,
                                        isLocked = true,
                                        isRedeeming = false,
                                        onRedeem = {}
                                    )
                                }
                            }
                        }

                        1 -> {
                            // Challenges
                            item {
                                Text(
                                    text = "Active Challenges",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BlazonForeground
                                )
                            }
                            items(state.challenges.filter { !it.isCompleted }) { challenge ->
                                ChallengeCard(challenge = challenge)
                            }

                            val completed = state.challenges.filter { it.isCompleted }
                            if (completed.isNotEmpty()) {
                                item {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Completed",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = BlazonMutedForeground
                                    )
                                }
                                items(completed) { challenge ->
                                    ChallengeCard(challenge = challenge)
                                }
                            }
                        }

                        2 -> {
                            // Tier Benefits
                            item {
                                TierBenefitsSection(currentTier = state.loyaltyInfo.tier)
                            }
                        }
                    }

                    // Recent Activity (always shown at bottom)
                    if (state.recentTransactions.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Recent Activity",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BlazonForeground
                                )
                                TextButton(onClick = onNavigateToHistory) {
                                    Text("See All", color = BlazonGold, fontSize = 13.sp)
                                }
                            }
                        }
                        items(state.recentTransactions) { transaction ->
                            TransactionRow(transaction = transaction)
                        }
                    }
                }
            }

            is RewardsUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = BlazonDestructive)
                }
            }
        }
    }
}

@Composable
fun PointsBalanceCard(
    loyaltyInfo: LoyaltyInfo,
    onHistoryClick: () -> Unit
) {
    GradientCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = "Your Points",
                    fontSize = 14.sp,
                    color = BlazonMutedForeground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${loyaltyInfo.totalPoints}",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlazonGold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Lifetime: ${loyaltyInfo.lifetimePoints} pts",
                    fontSize = 12.sp,
                    color = BlazonMutedForeground
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Box(
                    modifier = Modifier
                        .background(
                            when (loyaltyInfo.tier) {
                                LoyaltyStatus.Gold -> BlazonGold
                                LoyaltyStatus.Silver -> BlazonMutedForeground
                                LoyaltyStatus.Bronze -> BlazonMuted
                            },
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = loyaltyInfo.tier.name.uppercase(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlazonBlack
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "${loyaltyInfo.tierMultiplier}x earning",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = BlazonGold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onHistoryClick() }
                        .background(BlazonSecondary.copy(alpha = 0.5f))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Outlined.History,
                        contentDescription = "History",
                        tint = BlazonMutedForeground,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "History",
                        fontSize = 12.sp,
                        color = BlazonMutedForeground
                    )
                }
            }
        }
    }
}

@Composable
fun TierProgressCard(loyaltyInfo: LoyaltyInfo) {
    val nextTier = loyaltyInfo.nextTier
    if (nextTier == null) {
        PremiumCard(isHighlighted = true) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    Icons.Outlined.EmojiEvents,
                    contentDescription = "Max tier",
                    tint = BlazonGold,
                    modifier = Modifier.size(32.dp)
                )
                Column {
                    Text(
                        text = "Maximum Tier Reached!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlazonGold
                    )
                    Text(
                        text = "You're a Gold member - enjoy all exclusive benefits",
                        fontSize = 13.sp,
                        color = BlazonMutedForeground
                    )
                }
            }
        }
    } else {
        val totalForNext = when (nextTier) {
            LoyaltyStatus.Silver -> 1000
            LoyaltyStatus.Gold -> 5000
            else -> 0
        }
        val totalForCurrent = when (loyaltyInfo.tier) {
            LoyaltyStatus.Bronze -> 0
            LoyaltyStatus.Silver -> 1000
            LoyaltyStatus.Gold -> 5000
        }
        val rangeSize = (totalForNext - totalForCurrent).toFloat()
        val progressInRange = (loyaltyInfo.lifetimePoints - totalForCurrent).toFloat()
        val progress = if (rangeSize > 0) (progressInRange / rangeSize).coerceIn(0f, 1f) else 0f
        val animatedProgress by animateFloatAsState(targetValue = progress, label = "tier_progress")

        PremiumCard {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progress to ${nextTier.name}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BlazonForeground
                )
                Icon(
                    Icons.Outlined.TrendingUp,
                    contentDescription = "Progress",
                    tint = BlazonGold,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = BlazonGold,
                trackColor = BlazonSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${loyaltyInfo.lifetimePoints} pts",
                    fontSize = 12.sp,
                    color = BlazonMutedForeground
                )
                Text(
                    text = "${loyaltyInfo.pointsToNextTier} pts to go",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = BlazonGold
                )
            }
        }
    }
}

@Composable
fun StreakCard(loyaltyInfo: LoyaltyInfo) {
    PremiumCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Outlined.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = if (loyaltyInfo.currentStreak >= 3) BlazonGold else BlazonMutedForeground,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${loyaltyInfo.currentStreak}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlazonForeground
                )
                Text(
                    text = "Current Streak",
                    fontSize = 11.sp,
                    color = BlazonMutedForeground
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Outlined.Star,
                    contentDescription = "Best",
                    tint = BlazonMutedForeground,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${loyaltyInfo.longestStreak}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlazonForeground
                )
                Text(
                    text = "Best Streak",
                    fontSize = 11.sp,
                    color = BlazonMutedForeground
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Outlined.EmojiEvents,
                    contentDescription = "Redeemed",
                    tint = BlazonMutedForeground,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${loyaltyInfo.redeemedRewardsCount}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = BlazonForeground
                )
                Text(
                    text = "Redeemed",
                    fontSize = 11.sp,
                    color = BlazonMutedForeground
                )
            }
        }

        if (loyaltyInfo.currentStreak > 0) {
            val nextStreakBonus = when {
                loyaltyInfo.currentStreak % 5 >= 3 -> 5 - (loyaltyInfo.currentStreak % 5)
                loyaltyInfo.currentStreak % 3 >= 1 -> 3 - (loyaltyInfo.currentStreak % 3)
                else -> 3 - loyaltyInfo.currentStreak
            }
            if (nextStreakBonus > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            BlazonGold.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(10.dp)
                ) {
                    Text(
                        text = "$nextStreakBonus more visit(s) for your next streak bonus!",
                        fontSize = 12.sp,
                        color = BlazonGold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun RewardCard(
    reward: Reward,
    userPoints: Int,
    isLocked: Boolean,
    isRedeeming: Boolean,
    onRedeem: () -> Unit
) {
    val canAfford = userPoints >= reward.pointsCost

    PremiumCard(
        isHighlighted = !isLocked && canAfford
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = reward.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isLocked) BlazonMutedForeground else BlazonForeground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (reward.isLimitedTime) {
                        Box(
                            modifier = Modifier
                                .background(BlazonDestructive.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "LIMITED",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = BlazonDestructive
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = reward.description,
                    fontSize = 13.sp,
                    color = BlazonMutedForeground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (reward.originalPrice > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Worth Rs. ${"%,d".format(reward.originalPrice)}",
                        fontSize = 12.sp,
                        color = BlazonGold.copy(alpha = 0.7f)
                    )
                }

                // Tier requirement badge
                if (reward.requiredTier != LoyaltyStatus.Bronze) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                when (reward.requiredTier) {
                                    LoyaltyStatus.Gold -> BlazonGold.copy(alpha = 0.15f)
                                    LoyaltyStatus.Silver -> BlazonMutedForeground.copy(alpha = 0.15f)
                                    else -> BlazonSecondary
                                },
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${reward.requiredTier.name}+",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isLocked) BlazonMutedForeground else BlazonForeground
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${reward.pointsCost}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isLocked) BlazonMutedForeground
                    else if (canAfford) BlazonGold
                    else BlazonDestructive.copy(alpha = 0.7f)
                )
                Text(
                    text = "pts",
                    fontSize = 11.sp,
                    color = BlazonMutedForeground
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (isLocked) {
                    Icon(
                        Icons.Outlined.Lock,
                        contentDescription = "Locked",
                        tint = BlazonMutedForeground,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Button(
                        onClick = onRedeem,
                        enabled = canAfford && !isRedeeming,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BlazonGold,
                            contentColor = BlazonBlack,
                            disabledContainerColor = BlazonSecondary,
                            disabledContentColor = BlazonMutedForeground
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = if (isRedeeming) "..." else "Redeem",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChallengeCard(challenge: Challenge) {
    val progress = challenge.progress.toFloat() / challenge.target.toFloat()
    val animatedProgress by animateFloatAsState(targetValue = progress.coerceIn(0f, 1f), label = "challenge_progress")

    PremiumCard(isHighlighted = challenge.isCompleted) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = challenge.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (challenge.isCompleted) BlazonGold else BlazonForeground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = challenge.description,
                    fontSize = 13.sp,
                    color = BlazonMutedForeground
                )
                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (challenge.isCompleted) BlazonGold else BlazonGold.copy(alpha = 0.7f),
                    trackColor = BlazonSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "${challenge.progress}/${challenge.target}",
                    fontSize = 12.sp,
                    color = BlazonMutedForeground
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "+${challenge.bonusPoints}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (challenge.isCompleted) BlazonGold else BlazonForeground
                )
                Text(
                    text = "pts",
                    fontSize = 11.sp,
                    color = BlazonMutedForeground
                )
                if (challenge.isCompleted) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "DONE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlazonGold
                    )
                }
            }
        }
    }
}

@Composable
fun TierBenefitsSection(currentTier: LoyaltyStatus) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        LoyaltyStatus.values().forEach { tier ->
            val isCurrent = tier == currentTier
            val isUnlocked = when (currentTier) {
                LoyaltyStatus.Gold -> true
                LoyaltyStatus.Silver -> tier != LoyaltyStatus.Gold
                LoyaltyStatus.Bronze -> tier == LoyaltyStatus.Bronze
            }

            PremiumCard(isHighlighted = isCurrent) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                when (tier) {
                                    LoyaltyStatus.Gold -> BlazonGold
                                    LoyaltyStatus.Silver -> BlazonMutedForeground
                                    LoyaltyStatus.Bronze -> BlazonMuted
                                },
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (isUnlocked) Icons.Outlined.Star else Icons.Outlined.Lock,
                            contentDescription = tier.name,
                            tint = BlazonBlack,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = tier.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isUnlocked) BlazonForeground else BlazonMutedForeground
                            )
                            if (isCurrent) {
                                Box(
                                    modifier = Modifier
                                        .background(BlazonGold, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "CURRENT",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BlazonBlack
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = when (tier) {
                                LoyaltyStatus.Bronze -> "0 - 999 lifetime pts"
                                LoyaltyStatus.Silver -> "1,000 - 4,999 lifetime pts"
                                LoyaltyStatus.Gold -> "5,000+ lifetime pts"
                            },
                            fontSize = 12.sp,
                            color = BlazonMutedForeground
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LoyaltyInfo.getTierBenefits(tier).forEach { benefit ->
                    Row(
                        modifier = Modifier.padding(vertical = 3.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isUnlocked) "+" else "-",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isUnlocked) BlazonGold else BlazonMutedForeground
                        )
                        Text(
                            text = benefit,
                            fontSize = 13.sp,
                            color = if (isUnlocked) BlazonForeground else BlazonMutedForeground
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionRow(transaction: PointsTransaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BlazonCard, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.description,
                fontSize = 13.sp,
                color = BlazonForeground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = formatTransactionDate(transaction.date),
                fontSize = 11.sp,
                color = BlazonMutedForeground
            )
        }
        Text(
            text = when (transaction.type) {
                TransactionType.Earned, TransactionType.Bonus -> "+${transaction.points}"
                TransactionType.Redeemed -> "${transaction.points}"
                TransactionType.Expired -> "${transaction.points}"
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = when (transaction.type) {
                TransactionType.Earned -> BlazonForeground
                TransactionType.Bonus -> BlazonGold
                TransactionType.Redeemed -> BlazonDestructive.copy(alpha = 0.8f)
                TransactionType.Expired -> BlazonMutedForeground
            }
        )
    }
}

private fun formatTransactionDate(date: Date): String {
    val diff = System.currentTimeMillis() - date.time
    val days = diff / (1000 * 60 * 60 * 24)
    return when {
        days == 0L -> "Today"
        days == 1L -> "Yesterday"
        days < 7 -> "$days days ago"
        days < 30 -> "${days / 7} week(s) ago"
        else -> "${days / 30} month(s) ago"
    }
}
