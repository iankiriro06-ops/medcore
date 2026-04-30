package com.medcore.app.data.models

import androidx.compose.ui.graphics.Color
import com.medcore.app.ui.theme.*

// ── Subscription ───────────────────────────────────────────────────────────────

enum class SubscriptionTier { FREE, PREMIUM }

data class User(
    val id: String          = "usr_001",
    val name: String        = "Alex Carter",
    val email: String       = "alex.carter@email.com",
    val avatarInitials: String = "AC",
    val tier: SubscriptionTier = SubscriptionTier.FREE,
    val joinDate: String    = "March 2025",
    val streakDays: Int     = 14
)

// ── Body Systems ────────────────────────────────────────────────────────

data class BodySystem(
    val id: String,
    val name: String,
    val subtitle: String,
    val emoji: String,
    val colorStart: Color,
    val colorEnd: Color,
    val topicCount: Int,
    val progress: Float,        // 0f – 1f
    val isPremium: Boolean = false
)

val mockSystems = listOf(
    BodySystem("neuro",    "Nervous System",      "Brain, spinal cord & nerves",   "🧠", SystemNeuro,    IndigoCore,   42, 0.68f),
    BodySystem("cardio",   "Cardiovascular",      "Heart, arteries & veins",       "❤️", SystemCardio,   AmberCore,    38, 0.40f),
    BodySystem("respire",  "Respiratory",         "Lungs, trachea & airways",      "🫁", SystemRespire,  CyanCore,     29, 0.20f),
    BodySystem("musculo",  "Musculoskeletal",     "Muscles, bones & joints",       "💪", SystemMusculo,  TealCore,     55, 0.55f),
    BodySystem("digest",   "Digestive",           "GI tract, liver & pancreas",    "🫀", SystemDigest,   SystemCardio, 34, 0.10f, isPremium = true),
    BodySystem("endocrin", "Endocrine",           "Hormones & glands",             "⚗️", SystemEndocrin, SystemNeuro,  27, 0.00f, isPremium = true),
    BodySystem("renal",    "Renal / Urinary",     "Kidneys, ureters & bladder",    "🫘", SystemRenal,    CyanDim,      22, 0.00f, isPremium = true),
    BodySystem("immune",   "Immune / Lymphatic",  "Immunity & lymph nodes",        "🛡️", SystemImmune,   TealCore,     18, 0.00f, isPremium = true),
    BodySystem("repro",    "Reproductive",        "Male & female anatomy",         "🔬", SystemRepro,    SystemCardio, 31, 0.00f, isPremium = true),
    BodySystem("skeletal", "Skeletal",            "Bones, cartilage & ligaments",  "🦴", SystemSkeletal, IndigoCore,   40, 0.00f, isPremium = true),
    BodySystem("skin",     "Integumentary",       "Skin, hair & nails",            "🩹", SystemSkin,     AmberCore,    15, 0.00f, isPremium = true),
)

// ── Topics ─────────────────────────────────────────────────────────────────────

data class Topic(
    val id: String,
    val systemId: String,
    val title: String,
    val subtitle: String,
    val readTimeMin: Int,
    val isCompleted: Boolean = false,
    val isPremium: Boolean   = false,
    val hasQuiz: Boolean     = true
)

val mockTopics = listOf(
    Topic("n1",  "neuro", "Neurons & Synapses",          "Structure and signalling",        8,  isCompleted = true),
    Topic("n2",  "neuro", "The Cerebral Cortex",          "Lobes, areas & function",         12, isCompleted = true),
    Topic("n3",  "neuro", "Autonomic Nervous System",     "Sympathetic vs parasympathetic",  10, isCompleted = true),
    Topic("n4",  "neuro", "Blood–Brain Barrier",          "Structure & clinical relevance",  7),
    Topic("n5",  "neuro", "Cranial Nerves Overview",      "All 12 nerves explained",         15),
    Topic("n6",  "neuro", "Spinal Cord Tracts",           "Ascending & descending pathways", 14, isPremium = true),
    Topic("n7",  "neuro", "Basal Ganglia",                "Function & Parkinson's link",     9,  isPremium = true),
    Topic("n8",  "neuro", "Limbic System",                "Emotion, memory & behaviour",     11, isPremium = true),
    Topic("c1",  "cardio","Cardiac Anatomy",              "Chambers, valves & vessels",      10, isCompleted = true),
    Topic("c2",  "cardio","Cardiac Conduction",           "SA node to Purkinje fibres",      8,  isCompleted = true),
    Topic("c3",  "cardio","The Cardiac Cycle",            "Systole, diastole & pressures",   9),
    Topic("c4",  "cardio","Coronary Arteries",            "Supply & clinical importance",    7),
    Topic("c5",  "cardio","Arterial vs Venous Blood",     "Composition & flow dynamics",     6,  isPremium = true),
)

// ── Progress ────────────────────────────────────────────────────────────────────

data class WeeklyActivity(val day: String, val minutes: Int)

val mockWeeklyActivity = listOf(
    WeeklyActivity("Mon", 25),
    WeeklyActivity("Tue", 45),
    WeeklyActivity("Wed", 15),
    WeeklyActivity("Thu", 60),
    WeeklyActivity("Fri", 30),
    WeeklyActivity("Sat", 0),
    WeeklyActivity("Sun", 20),
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val isUnlocked: Boolean
)

val mockAchievements = listOf(
    Achievement("a1", "First Step",       "Complete your first topic",           "🎯", true),
    Achievement("a2", "On a Roll",        "7-day study streak",                  "🔥", true),
    Achievement("a3", "Neuroscientist",   "Complete the Nervous System",         "🧠", false),
    Achievement("a4", "Heart of Gold",    "Complete Cardiovascular",             "❤️", false),
    Achievement("a5", "Quiz Master",      "Ace 10 quizzes in a row",             "🏆", false),
    Achievement("a6", "Full Anatomy",     "Complete all body systems",           "💎", false),
)

// ── Subscription Plans ──────────────────────────────────────────────────────────

data class PlanFeature(val text: String, val included: Boolean)

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val price: String,
    val period: String,
    val features: List<PlanFeature>,
    val isBestValue: Boolean = false
)

val mockPlans = listOf(
    SubscriptionPlan(
        id = "monthly",
        name = "Monthly",
        price = "$3.99",
        period = "/ month",
        features = listOf(
            PlanFeature("All 11 body systems", true),
            PlanFeature("500+ topics & articles", true),
            PlanFeature("Unlimited quizzes", true),
            PlanFeature("Progress tracking", true),
            PlanFeature("Offline access", false),
            PlanFeature("AI study assistant", false),
        )
    ),
    SubscriptionPlan(
        id = "yearly",
        name = "Annual",
        price = "$20.00",
        period = "/ year",
        isBestValue = true,
        features = listOf(
            PlanFeature("All 11 body systems", true),
            PlanFeature("500+ topics & articles", true),
            PlanFeature("Unlimited quizzes", true),
            PlanFeature("Progress tracking", true),
            PlanFeature("Offline access", true),
            PlanFeature("AI study assistant", true),
        )
    )
)

// ── Quiz ────────────────────────────────────────────────────────────────────────

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

val mockQuizQuestions = listOf(
    QuizQuestion(
        id = "q1",
        question = "Which part of the neuron receives incoming signals from other neurons?",
        options = listOf("Axon", "Dendrites", "Myelin sheath", "Node of Ranvier"),
        correctIndex = 1,
        explanation = "Dendrites are the branched extensions of a neuron that receive signals from other neurons and transmit them to the cell body."
    ),
    QuizQuestion(
        id = "q2",
        question = "What is the resting membrane potential of a typical neuron?",
        options = listOf("+70 mV", "-70 mV", "0 mV", "-40 mV"),
        correctIndex = 1,
        explanation = "The resting membrane potential of a neuron is approximately -70 mV, maintained by the Na⁺/K⁺ ATPase pump."
    ),
    QuizQuestion(
        id = "q3",
        question = "The blood–brain barrier is primarily formed by which cells?",
        options = listOf("Neurons", "Microglia", "Tight junctions of endothelial cells", "Schwann cells"),
        correctIndex = 2,
        explanation = "The BBB is formed by tight junctions between capillary endothelial cells, supported by astrocyte end-feet and pericytes."
    ),
)