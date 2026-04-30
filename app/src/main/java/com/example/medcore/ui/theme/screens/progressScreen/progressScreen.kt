package com.example.medcore.ui.theme.screens.progressScreen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------------------------------------------------------------------------
// Data helpers
// ---------------------------------------------------------------------------

data class SystemProgress(
    val id: String,
    val name: String,
    val accentColor: Color,
    val completedTopics: Int,
    val totalTopics: Int,
    val quizBestScore: Int?,   // null = not taken
    val isPremium: Boolean
)

private val mockSystems = listOf(
    SystemProgress("neuro",   "Nervous System",    Color(0xFF6C63FF), 3, 6,  85,  false),
    SystemProgress("cardio",  "Cardiovascular",    Color(0xFFE05252), 1, 6,  60,  false),
    SystemProgress("resp",    "Respiratory",       Color(0xFF52B0E0), 0, 6,  null, false),
    SystemProgress("gi",      "Gastrointestinal",  Color(0xFF52C97A), 0, 6,  null, true),
    SystemProgress("endo",    "Endocrinology",     Color(0xFFF5A623), 0, 6,  null, true),
    SystemProgress("renal",   "Renal System",      Color(0xFF9C6ADE), 0, 6,  null, true),
    SystemProgress("musculo", "Musculoskeletal",   Color(0xFF50BFA0), 0, 6,  null, true),
    SystemProgress("immuno",  "Immunology",        Color(0xFFE07B52), 0, 6,  null, true),
)

// ---------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
    onBack: () -> Unit,
    onSubscribeClick: () -> Unit
) {
    val totalTopics    = mockSystems.sumOf { it.totalTopics }
    val completedTopics = mockSystems.sumOf { it.completedTopics }
    val overallProgress = if (totalTopics > 0) completedTopics.toFloat() / totalTopics else 0f

    val overallAnim by animateFloatAsState(
        targetValue = overallProgress,
        animationSpec = tween(1200, easing = FastOutSlowInEasing),
        label = "overall"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Progress", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Overall summary card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Overall Completion",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(Modifier.height(20.dp))
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                progress = { overallAnim },
                                modifier = Modifier.size(110.dp),
                                color = Color(0xFF6C63FF),
                                strokeWidth = 10.dp,
                                trackColor = Color(0xFF6C63FF).copy(alpha = 0.12f)
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "${(overallProgress * 100).toInt()}%",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
                                Text(
                                    "done",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatCell("Topics Read", "$completedTopics / $totalTopics")
                            StatCell("Systems Started", "${mockSystems.count { it.completedTopics > 0 }}")
                            StatCell("Quizzes Taken", "${mockSystems.count { it.quizBestScore != null }}")
                        }
                    }
                }
            }

            // Section header
            item {
                Text(
                    "By System",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
            }

            // Per-system rows
            items(mockSystems) { sys ->
                SystemProgressRow(
                    system = sys,
                    onSubscribeClick = onSubscribeClick
                )
            }
        }
    }
}

@Composable
private fun StatCell(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SystemProgressRow(
    system: SystemProgress,
    onSubscribeClick: () -> Unit
) {
    val prog = system.completedTopics.toFloat() / system.totalTopics
    val animProg by animateFloatAsState(
        targetValue = prog,
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label = "sys_${system.id}"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = if (system.isPremium) onSubscribeClick else ({})
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Colour dot
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(system.accentColor)
            )
            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        system.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    if (system.isPremium) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Premium",
                            tint = Color(0xFFF5A623),
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        system.quizBestScore?.let { score ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFF5A623),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    "$score%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFFF5A623),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { animProg },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = system.accentColor,
                    trackColor = system.accentColor.copy(alpha = 0.15f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${system.completedTopics} / ${system.totalTopics} topics",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
