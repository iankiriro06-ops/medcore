package com.example.medcore.ui.theme.screens.homescreen



import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.medcore.models.BodySystem
import com.example.medcore.ui.theme.*


// ── medCoreFieldColors ─────────────────────────────────────────────────────────

@Composable
fun medCoreFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = CyanCore,
    unfocusedBorderColor = BorderCard,
    focusedLabelColor    = CyanCore,
    unfocusedLabelColor  = TextMuted,
    cursorColor          = CyanCore,
    focusedTextColor     = TextPrimary,
    unfocusedTextColor   = TextPrimary,
    focusedContainerColor   = BackgroundCard,
    unfocusedContainerColor = BackgroundCard,
)

// ── ContinueLearningCard ───────────────────────────────────────────────────────

@Composable
fun ContinueLearningCard(
    system: BodySystem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(system.colorStart.copy(alpha = 0.25f), BackgroundCard)
                )
            )
            .border(1.dp, system.colorStart.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Emoji badge
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            system.colorStart.copy(alpha = 0.15f),
                            RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(system.emoji, fontSize = 26.sp)
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        system.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${(system.progress * 100).toInt()}% complete · ${system.topicCount} topics",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(Modifier.height(6.dp))
                    // Progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(system.colorStart.copy(alpha = 0.15f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(system.progress.coerceIn(0f, 1f))
                                .fillMaxHeight()
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(system.colorStart, system.colorEnd)
                                    ),
                                    RoundedCornerShape(3.dp)
                                )
                        )
                    }
                }
            }

            Icon(
                Icons.Filled.PlayCircleOutline,
                contentDescription = "Continue",
                tint = system.colorStart,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

// ── SystemCard ─────────────────────────────────────────────────────────────────

@Composable
fun SystemCard(
    system: BodySystem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(18.dp))
            .background(BackgroundCard)
            .border(1.dp, BorderCard, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
    ) {
        // Gradient accent strip at top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(
                    Brush.horizontalGradient(listOf(system.colorStart, system.colorEnd))
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                // Emoji
                Text(system.emoji, fontSize = 28.sp)
                Text(
                    system.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )
                Text(
                    system.subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    maxLines = 2
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                if (system.isPremium) {
                    // Premium lock badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .background(GoldPremium.copy(0.12f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = null,
                            tint = GoldPremium,
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            "PRO",
                            style = MaterialTheme.typography.labelSmall,
                            color = GoldPremium,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                } else {
                    // Progress bar + topic count
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${(system.progress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = system.colorStart,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "${system.topicCount} topics",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(system.colorStart.copy(0.12f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(system.progress.coerceIn(0f, 1f))
                                .fillMaxHeight()
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(system.colorStart, system.colorEnd)
                                    ),
                                    RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }
            }
        }
    }
}

// ── PremiumBannerCard ──────────────────────────────────────────────────────────

@Composable
fun PremiumBannerCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF1A1040), Color(0xFF0D1B2A))
                )
            )
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(GoldPremium.copy(0.6f), IndigoCore.copy(0.3f))
                ),
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Gold star icon
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(GoldPremium.copy(0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = GoldPremium,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        "Unlock All 11 Systems",
                        style = MaterialTheme.typography.titleSmall,
                        color = GoldPremium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Get full access · From \$3.99/month",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFB0A0C0)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(GoldPremium, RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    "Upgrade",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF1A1040),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}