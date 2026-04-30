package com.example.medcore.ui.theme.screens.topicScreen
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------------------------------------------------------------------------
// Data helpers (swap with ViewModel)
// ---------------------------------------------------------------------------

data class TopicContent(
    val id: String,
    val title: String,
    val systemName: String,
    val accentColor: Color,
    val readingTimeMin: Int,
    val sections: List<ContentSection>
)

data class ContentSection(
    val heading: String,
    val body: String,
    val keyPoint: String? = null
)

private fun mockTopic(topicId: String) = TopicContent(
    id = topicId,
    title = "Anatomy & Structure",
    systemName = "Nervous System",
    accentColor = Color(0xFF6C63FF),
    readingTimeMin = 12,
    sections = listOf(
        ContentSection(
            heading = "Overview",
            body = "The nervous system is the body's primary communication network, responsible for processing and transmitting signals between different parts of the body. It is broadly divided into the central nervous system (CNS) and the peripheral nervous system (PNS).",
            keyPoint = "The CNS consists of the brain and spinal cord, while the PNS encompasses all nerves outside these structures."
        ),
        ContentSection(
            heading = "Cellular Composition",
            body = "Neurons are the fundamental structural and functional units of the nervous system. Each neuron consists of a cell body (soma), dendrites that receive incoming signals, and an axon that transmits signals away from the cell body. Supporting cells called glial cells — including astrocytes, oligodendrocytes, and microglia — provide structural support, myelination, and immune defence.",
            keyPoint = "Myelination by oligodendrocytes (CNS) and Schwann cells (PNS) dramatically increases signal conduction velocity."
        ),
        ContentSection(
            heading = "Structural Divisions",
            body = "The brain is divided into the cerebrum, cerebellum, and brainstem. The cerebrum handles higher cognitive functions and voluntary movement. The cerebellum coordinates balance and fine motor control. The brainstem regulates autonomic functions including breathing, heart rate, and digestion.",
            keyPoint = null
        ),
        ContentSection(
            heading = "Blood-Brain Barrier",
            body = "The blood-brain barrier (BBB) is a highly selective semipermeable border formed by endothelial cells of the brain's capillaries. It restricts the passage of solutes between the circulating blood and the brain extracellular fluid, protecting neural tissue from pathogens and toxins while allowing essential nutrients through.",
            keyPoint = "Disruption of the BBB is implicated in conditions such as multiple sclerosis, Alzheimer's disease, and stroke."
        ),
    )
)

// ---------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicScreen(
    topicId: String,
    onBack: () -> Unit,
    onQuizClick: () -> Unit
) {
    val topic = remember { mockTopic(topicId) }
    var isBookmarked by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val readProgress by remember {
        derivedStateOf {
            if (scrollState.maxValue == 0) 0f
            else scrollState.value.toFloat() / scrollState.maxValue
        }
    }

    val animatedProgress by animateFloatAsState(
        targetValue = readProgress,
        animationSpec = tween(200),
        label = "readProgress"
    )

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            topic.systemName,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isBookmarked = !isBookmarked }) {
                            Icon(
                                if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = topic.accentColor
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                )
                // Reading progress bar
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxWidth().height(3.dp),
                    color = topic.accentColor,
                    trackColor = topic.accentColor.copy(alpha = 0.12f)
                )
            }
        },
        bottomBar = {
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = onQuizClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = topic.accentColor),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Test Your Knowledge", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .padding(bottom = 24.dp)
        ) {
            // Title block
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(topic.accentColor.copy(alpha = 0.15f), Color.Transparent)
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Column {
                    Surface(
                        color = topic.accentColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "${topic.readingTimeMin} min read",
                            color = topic.accentColor,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        topic.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Content sections
            topic.sections.forEachIndexed { index, section ->
                ContentSectionBlock(
                    section = section,
                    accentColor = topic.accentColor,
                    sectionNumber = index + 1
                )
                if (index < topic.sections.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ContentSectionBlock(
    section: ContentSection,
    accentColor: Color,
    sectionNumber: Int
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f))
            ) {
                Text(
                    "$sectionNumber",
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
            Spacer(Modifier.width(10.dp))
            Text(
                section.heading,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            section.body,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        section.keyPoint?.let { kp ->
            Spacer(Modifier.height(14.dp))
            Surface(
                color = accentColor.copy(alpha = 0.08f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, accentColor.copy(alpha = 0.25f))
            ) {
                Row(modifier = Modifier.padding(14.dp)) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .height(40.dp)
                            .clip(CircleShape)
                            .background(accentColor)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        kp,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = accentColor,
                        lineHeight = 20.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}