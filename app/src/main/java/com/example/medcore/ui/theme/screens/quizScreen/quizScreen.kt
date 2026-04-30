package com.example.medcore.ui.theme.screens.quizScreen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

private fun mockQuestions(systemId: String): List<QuizQuestion> = listOf(
    QuizQuestion(
        question = "Which cell type is responsible for myelination in the central nervous system?",
        options = listOf("Schwann cells", "Astrocytes", "Oligodendrocytes", "Microglia"),
        correctIndex = 2,
        explanation = "Oligodendrocytes myelinate axons in the CNS. Schwann cells perform the same role in the peripheral nervous system."
    ),
    QuizQuestion(
        question = "The blood-brain barrier is primarily formed by which cell type?",
        options = listOf("Pericytes", "Astrocytes", "Brain endothelial cells", "Ependymal cells"),
        correctIndex = 2,
        explanation = "Tight junctions between brain capillary endothelial cells form the structural basis of the BBB."
    ),
    QuizQuestion(
        question = "Which part of the neuron receives incoming signals from other neurons?",
        options = listOf("Axon terminal", "Myelin sheath", "Soma", "Dendrites"),
        correctIndex = 3,
        explanation = "Dendrites are branched extensions of the neuron that receive synaptic input from other neurons."
    ),
    QuizQuestion(
        question = "The cerebellum is primarily responsible for:",
        options = listOf("Language processing", "Emotion regulation", "Coordination and balance", "Visual interpretation"),
        correctIndex = 2,
        explanation = "The cerebellum integrates sensory input to fine-tune motor commands, maintaining balance and coordinating movement."
    ),
    QuizQuestion(
        question = "Which ion channel opens during the depolarisation phase of an action potential?",
        options = listOf("Voltage-gated K⁺ channels", "Voltage-gated Na⁺ channels", "Ligand-gated Cl⁻ channels", "Ca²⁺ leak channels"),
        correctIndex = 1,
        explanation = "Depolarisation is caused by rapid opening of voltage-gated Na⁺ channels allowing Na⁺ to rush into the cell."
    ),
)

private val correctColor = Color(0xFF4CAF50)
private val wrongColor   = Color(0xFFE05252)
private val neutralColor = Color(0xFF6C63FF)

// ---------------------------------------------------------------------------
// Screen
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    systemId: String,
    onBack: () -> Unit
) {
    val questions = remember { mockQuestions(systemId) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var showExplanation by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }

    val progressAnim by animateFloatAsState(
        targetValue = if (questions.isNotEmpty()) (currentIndex + 1).toFloat() / questions.size else 0f,
        animationSpec = tween(400),
        label = "quizProgress"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { padding ->
        AnimatedContent(
            targetState = isFinished,
            transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(300)) },
            label = "quizContent",
            modifier = Modifier.padding(padding)
        ) { finished ->
            if (finished) {
                ResultsView(
                    score = score,
                    total = questions.size,
                    accentColor = neutralColor,
                    onBack = onBack,
                    onRetry = {
                        currentIndex = 0; selectedOption = null
                        showExplanation = false; score = 0; isFinished = false
                    }
                )
            } else {
                val question = questions[currentIndex]
                QuestionView(
                    questionNumber = currentIndex + 1,
                    total = questions.size,
                    question = question,
                    selectedOption = selectedOption,
                    showExplanation = showExplanation,
                    progress = progressAnim,
                    accentColor = neutralColor,
                    onOptionSelected = { idx ->
                        if (selectedOption == null) {
                            selectedOption = idx
                            showExplanation = true
                            if (idx == question.correctIndex) score++
                        }
                    },
                    onNext = {
                        if (currentIndex < questions.lastIndex) {
                            currentIndex++
                            selectedOption = null
                            showExplanation = false
                        } else {
                            isFinished = true
                        }
                    }
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Question View
// ---------------------------------------------------------------------------

@Composable
private fun QuestionView(
    questionNumber: Int,
    total: Int,
    question: QuizQuestion,
    selectedOption: Int?,
    showExplanation: Boolean,
    progress: Float,
    accentColor: Color,
    onOptionSelected: (Int) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // Progress bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(CircleShape),
                color = accentColor,
                trackColor = accentColor.copy(alpha = 0.15f)
            )
            Spacer(Modifier.width(12.dp))
            Text(
                "$questionNumber / $total",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(28.dp))

        // Question card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = accentColor.copy(alpha = 0.08f)),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Text(
                question.question,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 26.sp,
                modifier = Modifier.padding(20.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        // Options
        question.options.forEachIndexed { idx, option ->
            OptionButton(
                label = ('A' + idx).toString(),
                text = option,
                state = when {
                    selectedOption == null -> OptionState.Idle
                    idx == question.correctIndex -> OptionState.Correct
                    idx == selectedOption -> OptionState.Wrong
                    else -> OptionState.Idle
                },
                accentColor = accentColor,
                onClick = { onOptionSelected(idx) }
            )
            Spacer(Modifier.height(10.dp))
        }

        // Explanation
        AnimatedVisibility(visible = showExplanation) {
            Column {
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedOption == question.correctIndex)
                            correctColor.copy(alpha = 0.08f) else wrongColor.copy(alpha = 0.08f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (selectedOption == question.correctIndex)
                            correctColor.copy(alpha = 0.3f) else wrongColor.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            if (selectedOption == question.correctIndex) "✓ Correct!" else "✗ Incorrect",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedOption == question.correctIndex) correctColor else wrongColor,
                            fontSize = 15.sp
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            question.explanation,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 20.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onNext,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text(
                        if (selectedOption != null && selectedOption.let { true } && currentIsLast(questionNumber, 5))
                            "See Results" else "Next Question",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

private fun currentIsLast(current: Int, total: Int) = current >= total

// ---------------------------------------------------------------------------
// Option Button
// ---------------------------------------------------------------------------

enum class OptionState { Idle, Correct, Wrong }

@Composable
private fun OptionButton(
    label: String,
    text: String,
    state: OptionState,
    accentColor: Color,
    onClick: () -> Unit
) {
    val bgColor = when (state) {
        OptionState.Correct -> correctColor.copy(alpha = 0.12f)
        OptionState.Wrong   -> wrongColor.copy(alpha = 0.12f)
        OptionState.Idle    -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    }
    val borderColor = when (state) {
        OptionState.Correct -> correctColor
        OptionState.Wrong   -> wrongColor
        OptionState.Idle    -> Color.Transparent
    }
    val labelBg = when (state) {
        OptionState.Correct -> correctColor
        OptionState.Wrong   -> wrongColor
        OptionState.Idle    -> accentColor
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.5.dp, borderColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(labelBg)
            ) {
                when (state) {
                    OptionState.Correct -> Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    OptionState.Wrong   -> Icon(Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    OptionState.Idle    -> Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
            Spacer(Modifier.width(12.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// ---------------------------------------------------------------------------
// Results View
// ---------------------------------------------------------------------------

@Composable
private fun ResultsView(
    score: Int,
    total: Int,
    accentColor: Color,
    onBack: () -> Unit,
    onRetry: () -> Unit
) {
    val percentage = if (total > 0) (score.toFloat() / total * 100).toInt() else 0
    val emoji = when {
        percentage >= 80 -> "🏆"
        percentage >= 60 -> "👍"
        else             -> "📚"
    }
    val message = when {
        percentage >= 80 -> "Excellent work!"
        percentage >= 60 -> "Good effort!"
        else             -> "Keep studying!"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(emoji, fontSize = 64.sp)
        Spacer(Modifier.height(16.dp))
        Text(message, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            "You scored $score out of $total",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))

        // Score ring placeholder
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { score.toFloat() / total },
                modifier = Modifier.size(120.dp),
                color = accentColor,
                strokeWidth = 10.dp,
                trackColor = accentColor.copy(alpha = 0.15f)
            )
            Text(
                "$percentage%",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
        ) {
            Text("Retry Quiz", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Back to Topics", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}