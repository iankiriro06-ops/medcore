package com.example.medcore.ui.theme.screens.profileScreen




import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.medcore.app.ui.theme.*

// ── Data ─────────────────────────────────────────────────────────────────────

private data class SettingsItem(
    val icon: ImageVector,
    val label: String,
    val subtitle: String? = null,
    val tint: Color = Color(0xFF6C63FF),
    val isDestructive: Boolean = false,
    val onClick: () -> Unit
)

// ── Screen ────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onSubscribeClick: () -> Unit,
    onSignOut: () -> Unit
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled      by remember { mutableStateOf(true) }
    var showSignOutDialog    by remember { mutableStateOf(false) }

    // Sign-out confirmation dialog
    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            containerColor   = BackgroundCard,
            title = {
                Text("Sign Out", color = TextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Are you sure you want to sign out of MedCore?",
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showSignOutDialog = false
                    onSignOut()
                }) {
                    Text("Sign Out", color = Color(0xFFE05252), fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSignOutDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }

    val settingsSections = listOf(
        "Account" to listOf(
            SettingsItem(Icons.Outlined.Person,           "Edit Profile",         tint = CyanCore)          { },
            SettingsItem(Icons.Outlined.Notifications,    "Notifications",        tint = Color(0xFF52B0E0)) { notificationsEnabled = !notificationsEnabled },
            SettingsItem(Icons.Outlined.DarkMode,         "Dark Mode",            tint = IndigoCore)        { darkModeEnabled = !darkModeEnabled },
        ),
        "Learning" to listOf(
            SettingsItem(Icons.Outlined.BarChart,         "My Progress",          tint = Color(0xFF52C97A)) { },
            SettingsItem(Icons.Outlined.Bookmark,         "Saved Topics",         tint = Color(0xFFF5A623)) { },
            SettingsItem(Icons.Outlined.History,          "Study History",        tint = Color(0xFF9C6ADE)) { },
        ),
        "Support" to listOf(
            SettingsItem(Icons.Outlined.HelpOutline,      "Help & FAQ",           tint = CyanCore)          { },
            SettingsItem(Icons.Outlined.PrivacyTip,       "Privacy Policy",       tint = TextMuted)         { },
            SettingsItem(Icons.Outlined.Description,      "Terms of Service",     tint = TextMuted)         { },
            SettingsItem(Icons.Outlined.ExitToApp,        "Sign Out",
                isDestructive = true, tint = Color(0xFFE05252))                                             { showSignOutDialog = true },
        ),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDeep)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Top bar ───────────────────────────────────────────────
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold, color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            // ── Avatar + name ─────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .background(IndigoSubtle, CircleShape)
                        .border(2.dp, IndigoCore, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "JD",
                        style = MaterialTheme.typography.headlineMedium,
                        color = IndigoCore,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(14.dp))
                Text(
                    "John Doe",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "john.doe@example.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(Modifier.height(20.dp))

                // Stats row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(BackgroundCard)
                        .border(1.dp, BorderCard, RoundedCornerShape(16.dp))
                        .padding(vertical = 18.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileStat("12", "Days Streak", "🔥")
                    VerticalDivider(modifier = Modifier.height(40.dp), color = BorderSubtle)
                    ProfileStat("4", "Systems", "🧠")
                    VerticalDivider(modifier = Modifier.height(40.dp), color = BorderSubtle)
                    ProfileStat("87%", "Quiz Avg", "⭐")
                }

                Spacer(Modifier.height(16.dp))

                // Premium upgrade banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF1A1040), Color(0xFF0D1B2A))
                            )
                        )
                        .border(
                            1.dp,
                            Brush.linearGradient(listOf(GoldPremium.copy(0.6f), IndigoCore.copy(0.3f))),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable(onClick = onSubscribeClick)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Filled.Star, null, tint = GoldPremium, modifier = Modifier.size(22.dp))
                            Column {
                                Text(
                                    "Upgrade to Pro",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = GoldPremium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Unlock all systems & features",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                        Icon(Icons.Filled.ChevronRight, null, tint = GoldPremium)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // ── Settings sections ─────────────────────────────────────
            settingsSections.forEach { (sectionTitle, items) ->
                Text(
                    sectionTitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = TextMuted,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(BackgroundCard)
                        .border(1.dp, BorderCard, RoundedCornerShape(16.dp))
                ) {
                    items.forEachIndexed { index, item ->
                        SettingsRow(
                            item = item,
                            showToggle = item.label == "Notifications" || item.label == "Dark Mode",
                            toggleState = when (item.label) {
                                "Notifications" -> notificationsEnabled
                                "Dark Mode"     -> darkModeEnabled
                                else            -> false
                            }
                        )
                        if (index < items.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = BorderSubtle
                            )
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
            }

            // App version
            Text(
                "MedCore v1.0.0",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── Profile Stat ──────────────────────────────────────────────────────────────

@Composable
private fun ProfileStat(value: String, label: String, emoji: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(emoji, fontSize = 20.sp)
        Spacer(Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
    }
}

// ── Settings Row ──────────────────────────────────────────────────────────────

@Composable
private fun SettingsRow(
    item: SettingsItem,
    showToggle: Boolean,
    toggleState: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = item.onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .background(item.tint.copy(0.12f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(item.icon, null, tint = item.tint, modifier = Modifier.size(18.dp))
            }
            Column {
                Text(
                    item.label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (item.isDestructive) Color(0xFFE05252) else TextPrimary,
                    fontWeight = FontWeight.Medium
                )
                item.subtitle?.let {
                    Text(it, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
            }
        }

        if (showToggle) {
            Switch(
                checked = toggleState,
                onCheckedChange = { item.onClick() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor  = Color.White,
                    checkedTrackColor  = CyanCore,
                    uncheckedThumbColor = TextMuted,
                    uncheckedTrackColor = BackgroundDeep
                )
            )
        } else if (!item.isDestructive) {
            Icon(Icons.Filled.ChevronRight, null, tint = TextMuted, modifier = Modifier.size(18.dp))
        }
    }
}