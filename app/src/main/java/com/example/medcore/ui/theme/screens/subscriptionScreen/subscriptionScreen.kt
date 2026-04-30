package com.example.medcore.ui.theme.screens.subscriptionScreen
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.medcore.app.ui.theme.*

// ── Data ─────────────────────────────────────────────────────────────────────

private data class PlanOption(
    val id: String,
    val label: String,
    val price: String,
    val period: String,
    val savingsBadge: String?,
    val isPopular: Boolean
)

private val plans = listOf(
    PlanOption("monthly",  "Monthly",  "\$7.99",  "/ month", null,        false),
    PlanOption("annual",   "Annual",   "\$4.99",  "/ month", "Save 38%",  true),
    PlanOption("lifetime", "Lifetime", "\$59.99", "one-time", "Best value", false),
)

private val features = listOf(
    "All 11 body systems unlocked",
    "500+ in-depth topics",
    "Unlimited quizzes & practice tests",
    "Offline access",
    "Progress tracking & analytics",
    "Ad-free experience",
    "Early access to new content",
)

// ── Screen ────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    var selectedPlan by remember { mutableStateOf("annual") }

    val glowAlpha by rememberInfiniteTransition(label = "glow").animateFloat(
        initialValue = 0.15f, targetValue = 0.35f,
        animationSpec = infiniteRepeatable(tween(2000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "glowAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDeep)
    ) {
        // Background glow
        Box(
            modifier = Modifier
                .size(360.dp)
                .align(Alignment.TopCenter)
                .offset(y = (-60).dp)
                .background(
                    Brush.radialGradient(
                        listOf(GoldPremium.copy(glowAlpha), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top bar
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(top = 48.dp, start = 12.dp)
            ) {
                Icon(Icons.Filled.Close, contentDescription = "Close", tint = TextPrimary)
            }

            Spacer(Modifier.height(8.dp))

            // Hero
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            Brush.radialGradient(listOf(GoldPremium.copy(0.2f), Color.Transparent)),
                            CircleShape
                        )
                        .border(1.5.dp, GoldPremium.copy(0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Star, null, tint = GoldPremium, modifier = Modifier.size(32.dp))
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    "MedCore Pro",
                    style = MaterialTheme.typography.displaySmall,
                    color = GoldPremium,
                    fontWeight = FontWeight.Black
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Master every body system with\nunlimited access to all content",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(32.dp))

            // Plan selector
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                plans.forEach { plan ->
                    PlanCard(
                        plan = plan,
                        isSelected = selectedPlan == plan.id,
                        onClick = { selectedPlan = plan.id }
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            // Features list
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Everything included",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                features.forEach { feature ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(CyanCore.copy(0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Check, null,
                                tint = CyanCore,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Text(
                            feature,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(Modifier.height(36.dp))

            // CTA
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onSuccess,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPremium)
                ) {
                    Text(
                        "Start Free Trial",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF1A1200),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    "7-day free trial · Cancel anytime",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(8.dp))

                TextButton(onClick = onBack) {
                    Text(
                        "Maybe later",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextMuted
                    )
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}

// ── Plan Card ─────────────────────────────────────────────────────────────────

@Composable
private fun PlanCard(
    plan: PlanOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isSelected && plan.isPopular -> GoldPremium
        isSelected                  -> CyanCore
        else                        -> BorderCard
    }
    val bgColor = when {
        isSelected -> BackgroundCard
        else       -> BackgroundDeep
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Radio indicator
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .border(1.5.dp, if (isSelected) borderColor else BorderSubtle, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(borderColor, CircleShape)
                        )
                    }
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            plan.label,
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (plan.isPopular) {
                            Surface(
                                color = GoldPremium.copy(0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    "Most Popular",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GoldPremium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    plan.savingsBadge?.let {
                        Text(it, style = MaterialTheme.typography.bodySmall, color = CyanCore)
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    plan.price,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) TextPrimary else TextSecondary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    plan.period,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }
    }
}