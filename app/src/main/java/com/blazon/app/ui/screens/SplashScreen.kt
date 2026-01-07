package com.blazon.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.blazon.app.theme.BlazonBlack
import com.blazon.app.theme.BlazonForeground
import com.blazon.app.theme.BlazonGold
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToNext: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000) // Show splash for 2 seconds
        onNavigateToNext()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BlazonBlack),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Blazon",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = BlazonGold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Premium Men's Grooming",
                fontSize = 16.sp,
                color = BlazonForeground.copy(alpha = 0.7f)
            )
        }
    }
}

