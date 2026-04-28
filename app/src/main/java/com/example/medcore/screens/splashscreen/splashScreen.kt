package com.medcore.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.material3.*
import com.medcore.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigate: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2200)
        onNavigate()
    }

    val infiniteTransition = rememberInfiniteTransition(label = "splash")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0.8f,
        animationSpec = infiniteRepeatable(tween(1200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glow"
    )
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDeep),
        contentAlignment = Alignment.Center
    ) {
        // Background radial glow
        Box(
            modifier = Modifier
                .size(320.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(CyanCore.copy(glowAlpha * 0.15f), Color.Transparent)
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo mark
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .scale(scale)
                    .background(
                        Brush.radialGradient(listOf(CyanSubtle, Color.Transparent)),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
                    .border(
                        2.dp,
                        Brush.linearGradient(listOf(CyanCore, IndigoCore)),
                        androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text  = "M+",
                    style = MaterialTheme.typography.displaySmall,
                    color = CyanCore,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text  = "MedCore",
                style = MaterialTheme.typography.displaySmall,
                color = TextPrimary,
                fontWeight = FontWeight.Black
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text  = "Master Human Anatomy",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(Modifier.height(60.dp))

            // Loading indicator
            CircularProgressIndicator(
                color = CyanCore,
                strokeWidth = 2.dp,
                modifier = Modifier.size(24.dp)
            )
        }

        // Version tag
        Text(
            text     = "v1.0.0",
            style    = MaterialTheme.typography.labelSmall,
            color    = TextMuted,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

