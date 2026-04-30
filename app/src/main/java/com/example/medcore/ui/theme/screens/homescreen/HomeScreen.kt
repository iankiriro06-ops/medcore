package com.medcore.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
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
import com.example.medcore.ui.theme.MedCoreBottomBar
import com.example.medcore.ui.theme.PremiumBadge
import com.example.medcore.ui.theme.SectionHeader
import com.medcore.app.data.models.*
import com.example.medcore.ui.theme.*
import com.medcore.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSystemClick: (String) -> Unit,
    onProgressClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSubscribeClick: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedRoute by remember { mutableStateOf("home") }
    val user = remember { User() }

    val filteredSystems = remember(searchQuery) {
        if (searchQuery.isBlank()) mockSystems
        else mockSystems.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        containerColor = BackgroundDeep,
        bottomBar = {
            MedCoreBottomBar(
                currentRoute = selectedRoute,
                onNavigate = { route ->
                    selectedRoute = route
                    when (route) {
                        "progress" -> onProgressClick()
                        "profile"  -> onProfileClick()
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // ── Header ──────────────────────────────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(BackgroundSurface, BackgroundDeep)
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    // Subtle background glow
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.TopEnd)
                            .background(Brush.radialGradient(listOf(CyanCore.copy(0.06f), Color.Transparent)))
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "Good morning,",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                            Text(
                                user.name.split(" ").first(),
                                style = MaterialTheme.typography.headlineLarge,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Streak badge
                            Row(
                                modifier = Modifier
                                    .background(AmberSubtle, RoundedCornerShape(20.dp))
                                    .border(1.dp, AmberCore.copy(0.4f), RoundedCornerShape(20.dp))
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("🔥", fontSize = 14.sp)
                                Text(
                                    "${user.streakDays}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = AmberCore,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            // Avatar
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(IndigoSubtle, CircleShape)
                                    .border(1.5.dp, IndigoCore, CircleShape)
                                    .clickable { onProfileClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(user.avatarInitials, style = MaterialTheme.typography.labelMedium, color = IndigoCore)
                            }
                        }
                    }
                }
            }

            // ── Search bar ──────────────────────────────────────────────
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search body systems…", color = TextMuted) },
                    leadingIcon = { Icon(Icons.Filled.Search, null, tint = TextMuted, modifier = Modifier.size(18.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Filled.Close, null, tint = TextMuted, modifier = Modifier.size(16.dp))
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    colors = medCoreFieldColors()
                )
            }

            // ── Continue Learning card ───────────────────────────────────
            if (searchQuery.isBlank()) {
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        SectionHeader("Continue Learning")
                        Spacer(Modifier.height(12.dp))
                        ContinueLearningCard(
                            system = mockSystems.first(),
                            onClick = { onSystemClick(mockSystems.first().id) }
                        )
                        Spacer(Modifier.height(28.dp))
                    }
                }

                // ── Free systems ─────────────────────────────────────────
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        SectionHeader("Your Systems", actionLabel = "See all")
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }

            // ── Systems grid ─────────────────────────────────────────────
            item {
                val systems = filteredSystems
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 2000.dp)
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement   = Arrangement.spacedBy(12.dp),
                    userScrollEnabled = false
                ) {
                    items(systems) { system ->
                        SystemCard(
                            system  = system,
                            onClick = {
                                if (system.isPremium) onSubscribeClick()
                                else onSystemClick(system.id)
                            }
                        )
                    }
                }
            }

            // ── Premium banner ───────────────────────────────────────────
            if (searchQuery.isBlank()) {
                item {
                    Spacer(Modifier.height(28.dp))
                    PremiumBannerCard(onClick = onSubscribeClick, modifier = Modifier.padding(horizontal = 20.dp))
                }
            }
        }
    }
}

// ── Continue Learning Card ──────────────────────────────────────────────────────

@Composable
fun ContinueLearningCard(system: BodySystem, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(system.colorStart.copy(0.3f), system.colorEnd.copy(0.15f), BackgroundCard)
                )
            )
            .border(1.dp, system.colorStart.copy(0.3f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Continue", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    Text(system.name, style = MaterialTheme.typography.headlineSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                    Text(system.subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
                Text(system.emoji, fontSize = 44.sp)
            }
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${(system.progress * 100).toInt()}% complete",
                    style = MaterialTheme.typography.labelMedium,
                    color = system.colorStart
                )
                Text(
                    "${system.topicCount} topics",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = system.progress,
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)).height(6.dp),
                color = system.colorStart,
                trackColor = system.colorStart.copy(0.2f)
            )
        }
    }
}

// ── System Card ─────────────────────────────────────────────────────────────────

@Composable
fun SystemCard(system: BodySystem, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(18.dp))
            .background(BackgroundCard)
            .border(1.dp, if (system.isPremium) GoldPremium.copy(0.25f) else BorderCard, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
    ) {
        // Top gradient accent
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        listOf(system.colorStart.copy(0.2f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(system.emoji, fontSize = 30.sp)
                    if (system.isPremium) {
                        PremiumBadge()
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(system.name, style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                Text(system.subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary, maxLines = 2)
            }

            Column {
                if (!system.isPremium) {
                    LinearProgressIndicator(
                        progress = system.progress,
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(3.dp)).height(4.dp),
                        color    = system.colorStart,
                        trackColor = system.colorStart.copy(0.15f)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "${(system.progress * 100).toInt()}%  ·  ${system.topicCount} topics",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                } else {
                    Text(
                        "${system.topicCount} topics",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted
                    )
                }
            }
        }

        // Lock overlay for premium
        if (system.isPremium) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp, bottom = 10.dp)
            ) {
                Icon(Icons.Filled.Lock, null, tint = GoldPremium, modifier = Modifier.size(16.dp).align(Alignment.Center))
            }
        }
    }
}

// ── Premium Banner ───────────────────────────────────────────────────────────────

@Composable
fun PremiumBannerCard(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF1A1040), Color(0xFF0D1B2A), Color(0xFF0B1E30))
                )
            )
            .border(
                1.dp,
                Brush.linearGradient(listOf(GoldPremium.copy(0.6f), IndigoCore.copy(0.4f))),
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
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Filled.Star, null, tint = GoldPremium, modifier = Modifier.size(16.dp))
                    Text("Unlock MedCore Pro", style = MaterialTheme.typography.titleLarge, color = GoldPremium, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    "Access all 11 body systems, 500+ topics\nand offline learning.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    "From \$4.99/mo",
                    style = MaterialTheme.typography.labelLarge,
                    color = GoldPremium
                )
            }
            Icon(Icons.Filled.ChevronRight, null, tint = GoldPremium, modifier = Modifier.size(28.dp))
        }
    }
}