package com.example.medcore.ui.theme.screens.homescreen

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medcore.ui.theme.MedCoreBottomBar
import com.example.medcore.ui.theme.PremiumBadge
import com.example.medcore.ui.theme.SectionHeader
import com.example.medcore.models.*
import com.example.medcore.ui.theme.*
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.medcore.viewmodel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSystemClick: (String) -> Unit,
    onProgressClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSubscribeClick: () -> Unit,
    userViewModel: UserViewModel = viewModel()
) {
    val userProfile by userViewModel.user.collectAsState()
    val isAdmin = userViewModel.isAdmin

    // ── FIX: declare missing state variables ──
    var selectedRoute by remember { mutableStateOf("home") }
    var searchQuery   by remember { mutableStateOf("") }

    val displaySystems = remember(isAdmin) {
        if (isAdmin) mockSystems.map { it.copy(isPremium = false) }
        else mockSystems
    }

    val filteredSystems = remember(searchQuery, displaySystems) {
        if (searchQuery.isBlank()) displaySystems
        else displaySystems.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
                    it.subtitle.contains(searchQuery, ignoreCase = true)
        }
    }

    LaunchedEffect(Unit) {
        userViewModel.loadUser()
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
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.TopEnd)
                            .background(
                                Brush.radialGradient(
                                    listOf(CyanCore.copy(0.06f), Color.Transparent)
                                )
                            )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // ── FIX: single source of truth for username ──
                        Column {
                            Text(
                                "Good morning,",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                            Text(
                                userProfile?.fullName?.split(" ")?.first() ?: "there",
                                style = MaterialTheme.typography.headlineLarge,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Streak badge — real data from Firebase
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
                                    "${userProfile?.streakDays ?: 0}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = AmberCore,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // ── FIX: avatar always shows "P" ──
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(IndigoSubtle, CircleShape)
                                    .border(1.5.dp, IndigoCore, CircleShape)
                                    .clickable { onProfileClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                val photo = userProfile?.photoUrl ?: ""
                                if (photo.isNotEmpty()) {
                                    AsyncImage(
                                        model = photo,
                                        contentDescription = "Avatar",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize().clip(CircleShape)
                                    )
                                } else {
                                    Text(
                                        userProfile?.fullName?.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = IndigoCore
                                    )
                                }
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
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search, null,
                            tint = TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    Icons.Filled.Close, null,
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
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
                    PremiumBannerCard(
                        onClick = onSubscribeClick,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
        }
    }
}

