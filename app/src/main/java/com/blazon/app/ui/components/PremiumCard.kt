package com.blazon.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.blazon.app.theme.BlazonCard
import com.blazon.app.theme.BlazonGold
import com.blazon.app.theme.BlazonSecondary

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isHighlighted) {
                BlazonCard.copy(alpha = 0.8f)
            } else {
                BlazonCard
            }
        ),
        border = if (isHighlighted) {
            androidx.compose.foundation.BorderStroke(1.dp, BlazonGold.copy(alpha = 0.3f))
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, BlazonSecondary)
        },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            content = content
        )
    }
}

@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = BlazonCard
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, BlazonGold.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            BlazonGold.copy(alpha = 0.1f),
                            BlazonGold.copy(alpha = 0.05f)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Column(content = content)
        }
    }
}

