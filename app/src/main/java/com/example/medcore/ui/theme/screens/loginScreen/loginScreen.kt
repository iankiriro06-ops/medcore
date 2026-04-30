package com.medcore.app.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.medcore.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDeep)
    ) {
        // Background glow top-left
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset((-80).dp, (-60).dp)
                .background(
                    Brush.radialGradient(listOf(CyanCore.copy(0.08f), Color.Transparent))
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(72.dp))

            // Logo
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(CyanSubtle, CircleShape)
                        .border(1.5.dp, CyanCore, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("M+", style = MaterialTheme.typography.titleMedium, color = CyanCore, fontWeight = FontWeight.Black)
                }
                Text("MedCore", style = MaterialTheme.typography.headlineLarge, color = TextPrimary, fontWeight = FontWeight.Black)
            }

            Spacer(Modifier.height(48.dp))

            Text("Welcome back", style = MaterialTheme.typography.displaySmall, color = TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Sign in to continue your anatomy journey", style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center)

            Spacer(Modifier.height(40.dp))

            // Google sign-in button
            OutlinedButton(
                onClick = onLoginSuccess,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape  = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, BorderCard),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = BackgroundCard)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("G", style = MaterialTheme.typography.titleLarge, color = Color(0xFF4285F4), fontWeight = FontWeight.Bold)
                    Text("Continue with Google", style = MaterialTheme.typography.labelLarge, color = TextPrimary)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Divider
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Divider(modifier = Modifier.weight(1f), color = BorderSubtle)
                Text("or", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                Divider(modifier = Modifier.weight(1f), color = BorderSubtle)
            }

            Spacer(Modifier.height(24.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email address") },
                leadingIcon = { Icon(Icons.Filled.Email, null, tint = TextSecondary, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = medCoreFieldColors()
            )

            Spacer(Modifier.height(16.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Filled.Lock, null, tint = TextSecondary, modifier = Modifier.size(18.dp)) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            null, tint = TextSecondary, modifier = Modifier.size(18.dp)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                colors = medCoreFieldColors()
            )

            Spacer(Modifier.height(12.dp))

            // Remember me + Forgot password
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor   = CyanCore,
                            uncheckedColor = TextMuted
                        )
                    )
                    Text("Remember me", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
                TextButton(onClick = {}) {
                    Text("Forgot password?", style = MaterialTheme.typography.labelMedium, color = CyanCore)
                }
            }

            Spacer(Modifier.height(28.dp))

            // Sign In CTA
            Button(
                onClick = onLoginSuccess,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyanCore)
            ) {
                Text("Sign In", style = MaterialTheme.typography.labelLarge, color = TextOnAccent, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))

            // Register link
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("New to MedCore? ", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                TextButton(onClick = onNavigateToRegister, contentPadding = PaddingValues(0.dp)) {
                    Text("Create account", style = MaterialTheme.typography.labelLarge, color = CyanCore, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun medCoreFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = CyanCore,
    unfocusedBorderColor = BorderSubtle,
    focusedLabelColor    = CyanCore,
    unfocusedLabelColor  = TextMuted,
    cursorColor          = CyanCore,
    focusedTextColor     = TextPrimary,
    unfocusedTextColor   = TextPrimary,
    focusedContainerColor   = BackgroundCard,
    unfocusedContainerColor = BackgroundCard,
)

