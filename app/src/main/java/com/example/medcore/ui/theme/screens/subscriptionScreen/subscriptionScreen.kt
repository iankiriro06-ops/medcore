package com.example.medcore.ui.theme.screens.subscriptionScreen

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.medcore.network.MpesaClient
import com.example.medcore.network.MpesaResult
import com.example.medcore.ui.theme.*
import kotlinx.coroutines.launch

// ── Data ─────────────────────────────────────────────────────────────────────

private data class PlanOption(
    val id: String,
    val label: String,
    val displayPrice: String,   // shown in UI e.g. "KES 200"
    val period: String,
    val amountKes: Int,         // actual amount sent to M-Pesa
    val savingsBadge: String?,
    val isPopular: Boolean
)

private val plans = listOf(
    PlanOption("monthly",  "Monthly",  "KES 200",   "/ month",   200,  null,         false),
    PlanOption("annual",   "Annual",   "KES 2,000", "/ year",    2000, "Save 17%",   true),
    PlanOption("lifetime", "Lifetime", "KES 5,000", "one-time",  5000, "Best value", false),
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
    var selectedPlan  by remember { mutableStateOf("annual") }
    var phone         by remember { mutableStateOf("") }
    var isLoading     by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isError       by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val selectedPlanObj = plans.first { it.id == selectedPlan }

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
                        plan       = plan,
                        isSelected = selectedPlan == plan.id,
                        onClick    = { selectedPlan = plan.id }
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

            Spacer(Modifier.height(32.dp))

            // M-Pesa payment section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(BackgroundCard)
                    .border(1.dp, BorderCard, RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("🟢", fontSize = 18.sp)
                    Text(
                        "Pay via M-Pesa",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(4.dp))

                // Show selected plan amount
                Text(
                    "You will be charged ${selectedPlanObj.displayPrice} ${selectedPlanObj.period}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("M-Pesa phone number") },
                    placeholder = { Text("e.g. 0712345678", color = TextMuted) },
                    leadingIcon = {
                        Text(
                            "🇰🇪",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = medCoreFieldColors()
                )

                // Status message
                statusMessage?.let { msg ->
                    Spacer(Modifier.height(10.dp))
                    Text(
                        msg,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isError) Color(0xFFFF5252) else CyanCore,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // CTA button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        if (phone.isBlank()) {
                            statusMessage = "Please enter your M-Pesa phone number"
                            isError = true
                            return@Button
                        }
                        if (phone.length < 10) {
                            statusMessage = "Please enter a valid phone number"
                            isError = true
                            return@Button
                        }

                        isLoading = true
                        statusMessage = "Sending payment request..."
                        isError = false

                        scope.launch {
                            val result = MpesaClient.initiateStkPush(
                                phone  = phone,
                                amount = selectedPlanObj.amountKes
                            )
                            isLoading = false
                            when (result) {
                                is MpesaResult.Success -> {
                                    isError = false
                                    statusMessage = "✓ ${result.message}"
                                    // Give user time to read the message then navigate
                                    kotlinx.coroutines.delay(2000)
                                    onSuccess()
                                }
                                is MpesaResult.Error -> {
                                    isError = true
                                    statusMessage = result.message
                                }
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor         = GoldPremium,
                        disabledContainerColor = GoldPremium.copy(0.5f)
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color(0xFF1A1200),
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            "Pay ${selectedPlanObj.displayPrice} via M-Pesa",
                            style = MaterialTheme.typography.labelLarge,
                            color = Color(0xFF1A1200),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    "A payment prompt will appear on your phone",
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
    val bgColor = if (isSelected) BackgroundCard else BackgroundDeep

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
                    plan.displayPrice,
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