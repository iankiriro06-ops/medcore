package com.example.medcore.ui.theme.screens.systemdetail

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// ---------------------------------------------------------------------------
// Data helpers (replace with real ViewModel / repository calls)
// ---------------------------------------------------------------------------

data class TopicItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val durationMin: Int,
    val isPremium: Boolean,
    val isCompleted: Boolean
)

private fun mockTopics(systemId: String): List<TopicItem> = listOf(
    TopicItem("t1", "Introduction & Overview",      "Core concepts and terminology",         8,  false, true),
    TopicItem("t2", "Anatomy & Structure",           "Detailed structural breakdown",         12, false, false),
    TopicItem("t3", "Physiology & Function",         "How it works at a cellular level",      15, false, false),
    TopicItem("t4", "Clinical Correlations",         "Common disorders and presentations",    20, true,  false),
    TopicItem("t5", "Pharmacology",                  "Key drugs and mechanisms",              18, true,  false),
    TopicItem("t6", "Case-Based Learning",           "Real-world clinical scenarios",         25, true,  false),
)

private val systemMeta = mapOf(
    "neuro"   to Triple("Nervous System",      "Explore neurons, pathways, and clinical neurology", Color(0xFF6C63FF)),
    "cardio"  to Triple("Cardiovascular",      "Heart, vessels, and haemodynamics",                 Color(0xFFE05252)),
    "resp"    to Triple("Respiratory",         "Airways, gas exchange, and lung mechanics",          Color(0xFF52B0E0)),
    "gi"      to Triple("Gastrointestinal",    "Digestion, absorption, and the GI tract",            Color(0xFF52C97A)),
    "endo"    to Triple("Endocrinology",       "Hormones, glands, and metabolic regulation",         Color(0xFFF5A623)),
    "renal"   to Triple("Renal System",        "Kidneys, fluid balance, and electrolytes",           Color(0xFF9C6ADE)),
    "musculo" to Triple("Musculoskeletal",     "Bones, joints, and movement",                        Color(0xFF50BFA0)),
    "immuno"  to Triple("Immunology",          "Immune responses, antibodies, and defence",          Color(0xFFE07B52)),
)

// ---------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemDetailScreen(
    systemId: String,
    onBack: () -> Unit,
    onTopicClick: (String) -> Unit,
    onQuizClick: () -> Unit,
    onSubscribeClick: () -> Unit
) {
    val (title, subtitle, accentColor) = systemMeta[systemId]
        ?: Triple("Body System", "Learn this system in depth", Color(0xFF6C63FF))

    val topics = remember { mockTopics(systemId) }
    val completedCount = topics.count { it.isCompleted }
    val progress = if (topics.isNotEmpty()) completedCount.toFloat() / topics.size else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Header Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(accentColor.copy(alpha = 0.85f), accentColor.copy(alpha = 0.4f))
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(24.dp)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Progress Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Your Progress",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "$completedCount / ${topics.size} topics",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(CircleShape),
                            color = accentColor,
                            trackColor = accentColor.copy(alpha = 0.15f)
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = onQuizClick,
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Take $title Quiz", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Topics header
            item {
                Text(
                    "Topics",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }

            // Topic list
            itemsIndexed(topics) { index, topic ->
                TopicRow(
                    topic = topic,
                    accentColor = accentColor,
                    index = index,
                    onClick = {
                        if (topic.isPremium) onSubscribeClick()
                        else onTopicClick(topic.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun TopicRow(
    topic: TopicItem,
    accentColor: Color,
    index: Int,
    onClick: () -> Unit
) {
    val enterAnim = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(index * 60L)
        enterAnim.animateTo(1f, tween(300))
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Step circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (topic.isCompleted) accentColor
                        else accentColor.copy(alpha = 0.12f)
                    )
            ) {
                Text(
                    text = "${index + 1}",
                    color = if (topic.isCompleted) Color.White else accentColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = topic.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${topic.durationMin} min · ${topic.subtitle}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.width(8.dp))

            if (topic.isPremium) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "Premium",
                    tint = Color(0xFFF5A623),
                    modifier = Modifier.size(18.dp)
                )
            } else {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

