package com.example.medcore.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.medcore.app.ui.theme.*

// ── Bottom Nav ────────────────────────────────────────────────────────────────

private data class NavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

private val navItems = listOf(
    NavItem("home",     "Home",     Icons.Filled.Home,     Icons.Outlined.Home),
    NavItem("progress", "Progress", Icons.Filled.BarChart, Icons.Outlined.BarChart),
    NavItem("profile",  "Profile",  Icons.Filled.Person,   Icons.Outlined.Person),
)

@Composable
fun MedCoreBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = BackgroundSurface,
        tonalElevation = 0.dp,
        modifier = Modifier.border(
            width = 1.dp,
            color = BorderCard,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        )
    ) {
        navItems.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick  = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        item.label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor   = CyanCore,
                    selectedTextColor   = CyanCore,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted,
                    indicatorColor      = CyanCore.copy(alpha = 0.12f)
                )
            )
        }
    }
}

// ── Section Header ────────────────────────────────────────────────────────────

@Composable
fun SectionHeader(
    title: String,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Bold
        )
        if (actionLabel != null) {
            Text(
                actionLabel,
                style = MaterialTheme.typography.labelMedium,
                color = CyanCore,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(onClick = onActionClick)
            )
        }
    }
}

// ── Premium Badge ─────────────────────────────────────────────────────────────

@Composable
fun PremiumBadge() {
    Row(
        modifier = Modifier
            .background(GoldPremium.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
            .border(1.dp, GoldPremium.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(Icons.Filled.Star, null, tint = GoldPremium, modifier = Modifier.size(10.dp))
        Text("Pro", fontSize = 10.sp, color = GoldPremium, fontWeight = FontWeight.Bold)
    }
}

// ── TextField Colors ──────────────────────────────────────────────────────────

@Composable
fun medCoreFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = CyanCore,
    unfocusedBorderColor = BorderCard,
    cursorColor          = CyanCore,
    focusedTextColor     = TextPrimary,
    unfocusedTextColor   = TextPrimary,
    focusedContainerColor   = BackgroundCard,
    unfocusedContainerColor = BackgroundCard,
)