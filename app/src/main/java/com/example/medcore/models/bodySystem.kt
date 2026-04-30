package com.example.medcore.models

import androidx.compose.ui.graphics.Color

data class BodySystem(
    val id: String,
    val name: String,
    val subtitle: String,
    val emoji: String,
    val colorStart: Color,
    val colorEnd: Color,
    val progress: Float,
    val topicCount: Int,
    val isPremium: Boolean
)

val mockSystems = listOf(
    BodySystem("neuro",   "Nervous System",    "Neurons & pathways",       "🧠", Color(0xFF6C63FF), Color(0xFF9C63FF), 0.45f, 12, false),
    BodySystem("cardio",  "Cardiovascular",    "Heart & vessels",          "❤️", Color(0xFFE05252), Color(0xFFFF7070), 0.20f, 10, false),
    BodySystem("resp",    "Respiratory",       "Airways & gas exchange",   "🫁", Color(0xFF52B0E0), Color(0xFF52D0F0), 0.0f,  9,  false),
    BodySystem("gi",      "Gastrointestinal",  "Digestion & absorption",   "🫃", Color(0xFF52C97A), Color(0xFF52E08A), 0.0f,  11, true),
    BodySystem("endo",    "Endocrinology",     "Hormones & glands",        "⚗️", Color(0xFFF5A623), Color(0xFFFFB623), 0.0f,  8,  true),
    BodySystem("renal",   "Renal System",      "Kidneys & fluid balance",  "🫘", Color(0xFF9C6ADE), Color(0xFFBC6AFF), 0.0f,  9,  true),
    BodySystem("musculo", "Musculoskeletal",   "Bones, joints & movement", "🦴", Color(0xFF50BFA0), Color(0xFF50DFB0), 0.0f,  13, true),
    BodySystem("immuno",  "Immunology",        "Immune defence",           "🛡️", Color(0xFFE07B52), Color(0xFFFF9B72), 0.0f,  10, true),
)
