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
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirm  by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var agreedToTerms   by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDeep)
    ) {
        Box(
            modifier = Modifier
                .size(280.dp)
                .align(Alignment.TopEnd)
                .offset(60.dp, (-60).dp)
                .background(Brush.radialGradient(listOf(IndigoCore.copy(0.08f), Color.Transparent)))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(56.dp))

            // Back button
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Filled.ArrowBack, "Back", tint = TextPrimary)
                }
            }

            Spacer(Modifier.height(8.dp))

            Text("Create Account", style = MaterialTheme.typography.displaySmall, color = TextPrimary, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Join MedCore and start mastering\nhuman anatomy today",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(36.dp))

            // Full name
            OutlinedTextField(
                value = fullName, onValueChange = { fullName = it },
                label = { Text("Full name") },
                leadingIcon = { Icon(Icons.Filled.Person, null, tint = TextSecondary, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                colors = medCoreFieldColors()
            )

            Spacer(Modifier.height(14.dp))

            // Email
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Email address") },
                leadingIcon = { Icon(Icons.Filled.Email, null, tint = TextSecondary, modifier = Modifier.size(18.dp)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                colors = medCoreFieldColors()
            )

            Spacer(Modifier.height(14.dp))

            // Password
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text("Password") },
                leadingIcon = { Icon(Icons.Filled.Lock, null, tint = TextSecondary, modifier = Modifier.size(18.dp)) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            null, tint = TextSecondary, modifier = Modifier.size(18.dp))
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                colors = medCoreFieldColors()
            )

            Spacer(Modifier.height(14.dp))

            // Confirm password
            OutlinedTextField(
                value = confirm, onValueChange = { confirm = it },
                label = { Text("Confirm password") },
                leadingIcon = { Icon(Icons.Filled.Lock, null, tint = TextSecondary, modifier = Modifier.size(18.dp)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                isError = confirm.isNotEmpty() && confirm != password,
                colors = medCoreFieldColors()
            )

            Spacer(Modifier.height(20.dp))

            // Terms
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = agreedToTerms,
                    onCheckedChange = { agreedToTerms = it },
                    colors = CheckboxDefaults.colors(checkedColor = CyanCore, uncheckedColor = TextMuted)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    "I agree to the ",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Text("Terms of Service", style = MaterialTheme.typography.bodySmall, color = CyanCore)
                Text(" & ", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                Text("Privacy Policy", style = MaterialTheme.typography.bodySmall, color = CyanCore)
            }

            Spacer(Modifier.height(28.dp))

            // CTA
            Button(
                onClick = onRegisterSuccess,
                enabled = agreedToTerms && fullName.isNotBlank() && email.isNotBlank() && password.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CyanCore,
                    disabledContainerColor = BackgroundElevated
                )
            ) {
                Text("Create Account", style = MaterialTheme.typography.labelLarge, color = TextOnAccent, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Already have an account? ", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                TextButton(onClick = onNavigateBack, contentPadding = PaddingValues(0.dp)) {
                    Text("Sign in", style = MaterialTheme.typography.labelLarge, color = CyanCore, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

