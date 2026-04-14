package com.blazon.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blazon.app.data.model.TransactionType
import com.blazon.app.theme.*
import com.blazon.app.ui.components.GradientCard
import com.blazon.app.ui.components.PremiumCard
import com.blazon.app.viewmodel.PointsHistoryUiState
import com.blazon.app.viewmodel.PointsHistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PointsHistoryScreen(
    userId: String,
    viewModel: PointsHistoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadHistory(userId)
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
                    brush = Brush.verticalGradient(
                        colors = listOf(BlazonCard, BlazonBlack)
                    )
                )
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Points History",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = BlazonForeground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "All your points activity",
                fontSize = 14.sp,
                color = BlazonMutedForeground
            )
        }

        when (val state = uiState) {
            is PointsHistoryUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BlazonGold)
                }
            }

            is PointsHistoryUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Summary Card
                    item {
                        GradientCard {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${state.loyaltyInfo.totalPoints}",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BlazonGold
                                    )
                                    Text(
                                        text = "Available",
                                        fontSize = 12.sp,
                                        color = BlazonMutedForeground
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    val totalEarned = state.transactions
                                        .filter { it.type == TransactionType.Earned || it.type == TransactionType.Bonus }
                                        .sumOf { it.points }
                                    Text(
                                        text = "$totalEarned",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BlazonForeground
                                    )
                                    Text(
                                        text = "Total Earned",
                                        fontSize = 12.sp,
                                        color = BlazonMutedForeground
                                    )
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    val totalRedeemed = state.transactions
                                        .filter { it.type == TransactionType.Redeemed }
                                        .sumOf { kotlin.math.abs(it.points) }
                                    Text(
                                        text = "$totalRedeemed",
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BlazonDestructive.copy(alpha = 0.8f)
                                    )
                                    Text(
                                        text = "Redeemed",
                                        fontSize = 12.sp,
                                        color = BlazonMutedForeground
                                    )
                                }
                            }
                        }
                    }

                    // Transactions
                    item {
                        Text(
                            text = "All Transactions",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BlazonForeground,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    if (state.transactions.isEmpty()) {
                        item {
                            PremiumCard {
                                Text(
                                    text = "No transactions yet. Visit Blazon to start earning points!",
                                    fontSize = 14.sp,
                                    color = BlazonMutedForeground,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    } else {
                        items(state.transactions) { transaction ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(BlazonCard, RoundedCornerShape(10.dp))
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Type indicator
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(
                                            when (transaction.type) {
                                                TransactionType.Earned -> BlazonGold.copy(alpha = 0.15f)
                                                TransactionType.Bonus -> BlazonGold.copy(alpha = 0.25f)
                                                TransactionType.Redeemed -> BlazonDestructive.copy(alpha = 0.15f)
                                                TransactionType.Expired -> BlazonMuted
                                            },
                                            RoundedCornerShape(8.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when (transaction.type) {
                                            TransactionType.Earned -> "E"
                                            TransactionType.Bonus -> "B"
                                            TransactionType.Redeemed -> "R"
                                            TransactionType.Expired -> "X"
                                        },
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (transaction.type) {
                                            TransactionType.Earned -> BlazonGold
                                            TransactionType.Bonus -> BlazonGold
                                            TransactionType.Redeemed -> BlazonDestructive
                                            TransactionType.Expired -> BlazonMutedForeground
                                        }
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = transaction.description,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = BlazonForeground,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = formatDate(transaction.date),
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
                                    fontSize = 17.sp,
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
                    }
                }
            }

            is PointsHistoryUiState.Error -> {
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

private fun formatDate(date: Date): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return sdf.format(date)
}
