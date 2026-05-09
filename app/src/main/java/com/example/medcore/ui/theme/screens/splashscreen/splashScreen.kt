package com.example.medcore.ui.theme.screens.splashscreen

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.medcore.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigate: () -> Unit) {

    // ── Animation states ──────────────────────────────────────────────────────
    var dropped       by remember { mutableStateOf(false) }
    var landed        by remember { mutableStateOf(false) }
    var showSpotlight by remember { mutableStateOf(false) }
    var showText      by remember { mutableStateOf(false) }

    // Drop + bounce
    val offsetY by animateFloatAsState(
        targetValue = if (dropped) 0f else -700f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness    = Spring.StiffnessMedium
        ),
        finishedListener = {
            landed        = true
            showSpotlight = true
            showText      = true
        },
        label = "dropY"
    )

    // Squash on landing
    val scaleX by animateFloatAsState(
        targetValue = if (landed) 1f else 1f,
        animationSpec = keyframes {
            durationMillis = 400
            1.0f  at 0
            1.25f at 100
            0.9f  at 200
            1.0f  at 400
        },
        label = "scaleX"
    )
    val scaleY by animateFloatAsState(
        targetValue = if (landed) 1f else 1f,
        animationSpec = keyframes {
            durationMillis = 400
            1.0f  at 0
            0.75f at 100
            1.1f  at 200
            1.0f  at 400
        },
        label = "scaleY"
    )

    // Spotlight fade in
    val spotlightAlpha by animateFloatAsState(
        targetValue   = if (showSpotlight) 1f else 0f,
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label         = "spotlight"
    )

    // Text fade in
    val textAlpha by animateFloatAsState(
        targetValue   = if (showText) 1f else 0f,
        animationSpec = tween(600, delayMillis = 300),
        label         = "textAlpha"
    )

    // Continuous cyan glow pulse
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue  = 0.4f,
        targetValue   = 1.0f,
        animationSpec = infiniteRepeatable(
            tween(1400, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    // Heartbeat line draw progress
    val heartbeatProgress by infiniteTransition.animateFloat(
        initialValue  = 0f,
        targetValue   = 1f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = LinearEasing),
            RepeatMode.Restart
        ),
        label = "heartbeat"
    )

    // ── Orchestration ─────────────────────────────────────────────────────────
    LaunchedEffect(Unit) {
        delay(300)
        dropped = true
        delay(2800)
        onNavigate()
    }

    // ── UI ────────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDeep),
        contentAlignment = Alignment.Center
    ) {
        // ── Spotlight beams ───────────────────────────────────────────────────
        if (showSpotlight) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = (-80).dp, y = 100.dp)
                    .size(width = 140.dp, height = 600.dp)
                    .alpha(spotlightAlpha * 0.3f)
                    .rotate(-20f)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, CyanCore.copy(0.7f))
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 100.dp)
                    .size(width = 100.dp, height = 600.dp)
                    .alpha(spotlightAlpha * 0.4f)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, CyanCore.copy(0.5f))
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 80.dp, y = 100.dp)
                    .size(width = 140.dp, height = 600.dp)
                    .alpha(spotlightAlpha * 0.3f)
                    .rotate(20f)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, CyanCore.copy(0.7f))
                        )
                    )
            )

            // Ground glow ellipse
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 80.dp)
                    .size(width = 200.dp, height = 20.dp)
                    .alpha(spotlightAlpha * 0.7f)
                    .background(
                        Brush.radialGradient(
                            listOf(CyanCore.copy(0.6f), Color.Transparent)
                        ),
                        RoundedCornerShape(50)
                    )
            )
        }

        // ── Logo drop ─────────────────────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.offset(y = offsetY.dp)
        ) {
            // Outer glow halo
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(CyanCore.copy(glowAlpha * 0.25f), Color.Transparent)
                        ),
                        RoundedCornerShape(36.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Icon card matching app icon shape
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .graphicsLayer(scaleX = scaleX, scaleY = scaleY)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF0D1B2A), Color(0xFF0A1628))
                            ),
                            RoundedCornerShape(28.dp)
                        )
                        .border(
                            width = 1.5.dp,
                            brush = Brush.linearGradient(
                                listOf(CyanCore.copy(glowAlpha), CyanCore.copy(0.2f))
                            ),
                            shape = RoundedCornerShape(28.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        // Glass M — subtle white
                        Text(
                            text       = "M",
                            fontSize   = 72.sp,
                            fontWeight = FontWeight.Black,
                            color      = Color.White.copy(0.12f)
                        )
                        // Cyan glowing M on top
                        Text(
                            text       = "M",
                            fontSize   = 72.sp,
                            fontWeight = FontWeight.Black,
                            color      = CyanCore.copy(glowAlpha * 0.55f)
                        )
                        // Animated heartbeat line
                        Canvas(
                            modifier = Modifier
                                .size(width = 110.dp, height = 40.dp)
                                .offset(y = 4.dp)
                        ) {
                            drawHeartbeatLine(
                                progress = heartbeatProgress,
                                color    = CyanCore,
                                alpha    = glowAlpha
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            Text(
                text       = "MedCore",
                style      = MaterialTheme.typography.displaySmall,
                color      = TextPrimary,
                fontWeight = FontWeight.Black,
                modifier   = Modifier.alpha(textAlpha)
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text     = "Master Human Anatomy",
                style    = MaterialTheme.typography.bodyMedium,
                color    = TextSecondary,
                modifier = Modifier.alpha(textAlpha)
            )
        }

        // Version
        Text(
            text     = "v1.0.0",
            style    = MaterialTheme.typography.labelSmall,
            color    = TextMuted,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .alpha(textAlpha)
        )
    }
}

// ── Heartbeat line ────────────────────────────────────────────────────────────
private fun DrawScope.drawHeartbeatLine(
    progress: Float,
    color: Color,
    alpha: Float
) {
    val w   = size.width
    val h   = size.height
    val mid = h / 2f

    val points = listOf(
        0.00f to mid,
        0.20f to mid,
        0.28f to mid - h * 0.8f,
        0.33f to mid + h * 0.6f,
        0.38f to mid - h * 0.4f,
        0.43f to mid,
        0.55f to mid,
        0.62f to mid - h * 0.5f,
        0.67f to mid + h * 0.3f,
        0.72f to mid,
        1.00f to mid,
    )

    val path   = Path()
    val cutoff = progress * w

    points.forEachIndexed { i, (nx, ny) ->
        val x = nx * w
        val y = ny
        if (x > cutoff) return@forEachIndexed
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }

    // Main line
    drawPath(
        path  = path,
        color = color.copy(alpha = alpha),
        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
    // Glow blur duplicate
    drawPath(
        path  = path,
        color = color.copy(alpha = alpha * 0.3f),
        style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}