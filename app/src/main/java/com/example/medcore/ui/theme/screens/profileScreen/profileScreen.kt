package com.example.medcore.ui.theme.screens.profileScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.medcore.viewmodel.UserViewModel
import com.example.medcore.ui.theme.*
import com.google.firebase.auth.FirebaseAuth

private data class SettingsItem(
    val icon: ImageVector,
    val label: String,
    val subtitle: String? = null,
    val tint: Color = Color(0xFF6C63FF),
    val isDestructive: Boolean = false,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onSubscribeClick: () -> Unit,
    onSignOut: () -> Unit,
    userViewModel: UserViewModel = viewModel()
) {
    val userProfile by userViewModel.user.collectAsState()

    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled      by remember { mutableStateOf(true) }
    var showSignOutDialog    by remember { mutableStateOf(false) }
    var showDeleteDialog     by remember { mutableStateOf(false) }
    var showEditSheet        by remember { mutableStateOf(false) }

    // ── Edit profile state ────────────────────────────────────────────────────
    var editName         by remember { mutableStateOf("") }
    var editPhotoUrl     by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Gallery picker
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            editPhotoUrl     = it.toString()
        }
    }

    // Pre-fill edit fields when sheet opens
    LaunchedEffect(showEditSheet) {
        if (showEditSheet) {
            editName         = userProfile?.fullName ?: ""
            editPhotoUrl     = userProfile?.photoUrl ?: ""
            selectedImageUri = null
        }
    }

    // ── Sign out dialog ───────────────────────────────────────────────────────
    if (showSignOutDialog) {
        AlertDialog(
            onDismissRequest = { showSignOutDialog = false },
            containerColor   = BackgroundCard,
            title = { Text("Sign Out", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text  = { Text("Are you sure you want to sign out of MedCore?", color = TextSecondary) },
            confirmButton = {
                TextButton(onClick = { showSignOutDialog = false; onSignOut() }) {
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

    // ── Delete account dialog ─────────────────────────────────────────────────
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor   = BackgroundCard,
            title = {
                Text(
                    "Delete Account",
                    color      = Color(0xFFE05252),
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "This will permanently delete your account and all your progress. This cannot be undone.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    userViewModel.deleteAccount(
                        onSuccess = { onSignOut() },
                        onError   = { /* optionally show a snackbar */ }
                    )
                }) {
                    Text(
                        "Delete Forever",
                        color      = Color(0xFFE05252),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel", color = TextMuted)
                }
            }
        )
    }

    // ── Edit Profile bottom sheet ─────────────────────────────────────────────
    if (showEditSheet) {
        ModalBottomSheet(
            onDismissRequest = { showEditSheet = false },
            containerColor   = BackgroundCard,
            dragHandle       = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .size(width = 40.dp, height = 4.dp)
                        .background(BorderSubtle, RoundedCornerShape(2.dp))
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Edit Profile",
                    style      = MaterialTheme.typography.titleLarge,
                    color      = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(24.dp))

                // Avatar preview + change options
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(IndigoSubtle)
                            .border(2.dp, IndigoCore, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        val photoToShow = selectedImageUri?.toString() ?: editPhotoUrl
                        if (photoToShow.isNotEmpty()) {
                            AsyncImage(
                                model              = photoToShow,
                                contentDescription = "Profile photo",
                                contentScale       = ContentScale.Crop,
                                modifier           = Modifier.fillMaxSize()
                            )
                        } else {
                            Text(
                                text       = editName.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                                style      = MaterialTheme.typography.headlineMedium,
                                color      = IndigoCore,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(CyanCore, CircleShape)
                            .border(2.dp, BackgroundCard, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Filled.Edit, null,
                            tint     = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        shape   = RoundedCornerShape(12.dp),
                        border  = BorderStroke(1.dp, BorderCard),
                        colors  = ButtonDefaults.outlinedButtonColors(containerColor = BackgroundDeep)
                    ) {
                        Icon(
                            Icons.Filled.PhotoLibrary, null,
                            tint     = CyanCore,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Gallery", style = MaterialTheme.typography.labelMedium, color = TextPrimary)
                    }

                    val googlePhotoUrl = FirebaseAuth.getInstance().currentUser?.photoUrl?.toString() ?: ""
                    if (googlePhotoUrl.isNotEmpty()) {
                        OutlinedButton(
                            onClick = {
                                editPhotoUrl     = googlePhotoUrl
                                selectedImageUri = null
                            },
                            shape  = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, BorderCard),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = BackgroundDeep)
                        ) {
                            Text(
                                "G",
                                style      = MaterialTheme.typography.labelLarge,
                                color      = Color(0xFF4285F4),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("Google", style = MaterialTheme.typography.labelMedium, color = TextPrimary)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value         = editName,
                    onValueChange = { editName = it },
                    label         = { Text("Full name") },
                    leadingIcon   = {
                        Icon(Icons.Filled.Person, null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                    },
                    modifier   = Modifier.fillMaxWidth(),
                    shape      = RoundedCornerShape(14.dp),
                    singleLine = true,
                    colors     = medCoreFieldColors()
                )

                Spacer(Modifier.height(28.dp))

                Button(
                    onClick = {
                        userViewModel.updateProfile(
                            newName     = editName,
                            newPhotoUrl = editPhotoUrl
                        )
                        showEditSheet = false
                    },
                    enabled  = editName.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(containerColor = CyanCore)
                ) {
                    Text(
                        "Save Changes",
                        style      = MaterialTheme.typography.labelLarge,
                        color      = TextOnAccent,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    val settingsSections = listOf(
        "Account" to listOf(
            SettingsItem(Icons.Outlined.Person, "Edit Profile", tint = CyanCore) {
                showEditSheet = true
            },
            SettingsItem(Icons.Outlined.Notifications, "Notifications", tint = Color(0xFF52B0E0)) {
                notificationsEnabled = !notificationsEnabled
            },
            SettingsItem(Icons.Outlined.DarkMode, "Dark Mode", tint = IndigoCore) {
                darkModeEnabled = !darkModeEnabled
            },
        ),
        "Learning" to listOf(
            SettingsItem(Icons.Outlined.BarChart,  "My Progress",   tint = Color(0xFF52C97A)) { },
            SettingsItem(Icons.Outlined.Bookmark,  "Saved Topics",  tint = Color(0xFFF5A623)) { },
            SettingsItem(Icons.Outlined.History,   "Study History", tint = Color(0xFF9C6ADE)) { },
        ),
        "Support" to listOf(
            SettingsItem(Icons.Outlined.HelpOutline,  "Help & FAQ",       tint = CyanCore)  { },
            SettingsItem(Icons.Outlined.PrivacyTip,   "Privacy Policy",   tint = TextMuted) { },
            SettingsItem(Icons.Outlined.Description,  "Terms of Service", tint = TextMuted) { },
            SettingsItem(
                Icons.Outlined.ExitToApp, "Sign Out",
                isDestructive = true, tint = Color(0xFFE05252)
            ) { showSignOutDialog = true },
            SettingsItem(
                Icons.Outlined.DeleteForever,
                "Delete Account",
                subtitle      = "Permanently remove your data",
                isDestructive = true,
                tint          = Color(0xFFE05252)
            ) { showDeleteDialog = true },
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
            TopAppBar(
                title = { Text("Profile", fontWeight = FontWeight.Bold, color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Back", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            // ── Avatar + name ─────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(IndigoSubtle)
                        .border(2.dp, IndigoCore, CircleShape)
                        .clickable { showEditSheet = true },
                    contentAlignment = Alignment.Center
                ) {
                    val photo = userProfile?.photoUrl ?: ""
                    if (photo.isNotEmpty()) {
                        AsyncImage(
                            model              = photo,
                            contentDescription = "Profile photo",
                            contentScale       = ContentScale.Crop,
                            modifier           = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            text       = userProfile?.fullName?.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                            style      = MaterialTheme.typography.headlineMedium,
                            color      = IndigoCore,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    "Tap to edit",
                    style    = MaterialTheme.typography.labelSmall,
                    color    = TextMuted,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    userProfile?.fullName ?: "Loading...",
                    style      = MaterialTheme.typography.titleLarge,
                    color      = TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    userProfile?.email ?: "",
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
                    ProfileStat("${userProfile?.streakDays ?: 0}", "Days Streak", "🔥")
                    VerticalDivider(modifier = Modifier.height(40.dp), color = BorderSubtle)
                    ProfileStat("4", "Systems", "🧠")
                    VerticalDivider(modifier = Modifier.height(40.dp), color = BorderSubtle)
                    ProfileStat("87%", "Quiz Avg", "⭐")
                }

                Spacer(Modifier.height(16.dp))

                // Premium banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.linearGradient(listOf(Color(0xFF1A1040), Color(0xFF0D1B2A))))
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
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment     = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(Icons.Filled.Star, null, tint = GoldPremium, modifier = Modifier.size(22.dp))
                            Column {
                                Text("Upgrade to Pro", style = MaterialTheme.typography.titleSmall, color = GoldPremium, fontWeight = FontWeight.Bold)
                                Text("Unlock all systems & features", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                        }
                        Icon(Icons.Filled.ChevronRight, null, tint = GoldPremium)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // ── Settings sections ─────────────────────────────────────────────
            settingsSections.forEach { (sectionTitle, items) ->
                Text(
                    sectionTitle,
                    style      = MaterialTheme.typography.labelMedium,
                    color      = TextMuted,
                    fontWeight = FontWeight.SemiBold,
                    modifier   = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
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
                            item        = item,
                            showToggle  = item.label == "Notifications" || item.label == "Dark Mode",
                            toggleState = when (item.label) {
                                "Notifications" -> notificationsEnabled
                                "Dark Mode"     -> darkModeEnabled
                                else            -> false
                            }
                        )
                        if (index < items.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color    = BorderSubtle
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            Text(
                "MedCore v1.0.0",
                style    = MaterialTheme.typography.labelSmall,
                color    = TextMuted,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

@Composable
private fun ProfileStat(value: String, label: String, emoji: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(emoji, fontSize = 20.sp)
        Spacer(Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
    }
}

@Composable
private fun SettingsRow(item: SettingsItem, showToggle: Boolean, toggleState: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = item.onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
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
                    style      = MaterialTheme.typography.bodyMedium,
                    color      = if (item.isDestructive) Color(0xFFE05252) else TextPrimary,
                    fontWeight = FontWeight.Medium
                )
                item.subtitle?.let {
                    Text(it, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
            }
        }
        if (showToggle) {
            Switch(
                checked         = toggleState,
                onCheckedChange = { item.onClick() },
                colors          = SwitchDefaults.colors(
                    checkedThumbColor   = Color.White,
                    checkedTrackColor   = CyanCore,
                    uncheckedThumbColor = TextMuted,
                    uncheckedTrackColor = BackgroundDeep
                )
            )
        } else if (!item.isDestructive) {
            Icon(Icons.Filled.ChevronRight, null, tint = TextMuted, modifier = Modifier.size(18.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun medCoreFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor      = CyanCore,
    unfocusedBorderColor    = BorderSubtle,
    focusedLabelColor       = CyanCore,
    unfocusedLabelColor     = TextMuted,
    cursorColor             = CyanCore,
    focusedTextColor        = TextPrimary,
    unfocusedTextColor      = TextPrimary,
    focusedContainerColor   = BackgroundCard,
    unfocusedContainerColor = BackgroundCard,
)
