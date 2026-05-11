package com.example.medcore.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class UserProfile(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val subscribed: Boolean = false,
    val streakDays: Int = 0,
    val photoUrl: String = ""
)

class UserViewModel : ViewModel() {

    private val _user = MutableStateFlow<UserProfile?>(null)
    val user: StateFlow<UserProfile?> = _user

    private val ADMIN_EMAIL = "admin@medcore.app"

    val isAdmin: Boolean
        get() = FirebaseAuth.getInstance().currentUser?.email == ADMIN_EMAIL

    fun loadUser() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return
        val uid = firebaseUser.uid

        viewModelScope.launch {
            try {
                val db          = FirebaseFirestore.getInstance()
                val doc         = db.collection("users").document(uid).get().await()
                val googlePhoto = firebaseUser.photoUrl?.toString() ?: ""

                if (doc.exists()) {
                    _user.value = UserProfile(
                        uid        = doc.getString("uid")        ?: uid,
                        fullName   = doc.getString("fullName")   ?: "",
                        email      = doc.getString("email")      ?: firebaseUser.email ?: "",
                        subscribed = doc.getBoolean("subscribed") ?: false,
                        streakDays = doc.getLong("streakDays")?.toInt() ?: 0,
                        photoUrl   = doc.getString("photoUrl")   ?: googlePhoto
                    )
                } else {
                    _user.value = UserProfile(
                        uid      = uid,
                        fullName = firebaseUser.displayName ?: "",
                        email    = firebaseUser.email ?: "",
                        photoUrl = googlePhoto
                    )
                }

                updateStreak(uid)

            } catch (e: Exception) {
                println("UserViewModel error: ${e.message}")
            }
        }
    }

    fun updateProfile(newName: String, newPhotoUrl: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            try {
                FirebaseFirestore.getInstance()
                    .collection("users").document(uid)
                    .update(mapOf("fullName" to newName.trim(), "photoUrl" to newPhotoUrl))
                    .await()

                _user.value = _user.value?.copy(
                    fullName = newName.trim(),
                    photoUrl = newPhotoUrl
                )
            } catch (e: Exception) {
                println("updateProfile error: ${e.message}")
            }
        }
    }

    fun deleteAccount(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return
        val uid = firebaseUser.uid
        viewModelScope.launch {
            try {
                // Delete Firestore document (all user data)
                FirebaseFirestore.getInstance()
                    .collection("users").document(uid)
                    .delete().await()

                // Delete the Firebase Auth account (the login)
                firebaseUser.delete().await()

                // Wipe local state
                _user.value = null

                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Failed to delete account")
            }
        }
    }

    private suspend fun updateStreak(uid: String) {
        try {
            val db            = FirebaseFirestore.getInstance()
            val ref           = db.collection("users").document(uid)
            val doc           = ref.get().await()
            val lastSeen      = doc.getLong("lastSeen") ?: 0L
            val now           = System.currentTimeMillis()
            val oneDayMs      = 86_400_000L
            val daysSince     = (now - lastSeen) / oneDayMs
            val currentStreak = doc.getLong("streakDays")?.toInt() ?: 0

            val newStreak = when {
                daysSince == 1L -> currentStreak + 1
                daysSince == 0L -> currentStreak
                else            -> 1
            }

            ref.update(mapOf("streakDays" to newStreak, "lastSeen" to now)).await()
            _user.value = _user.value?.copy(streakDays = newStreak)

        } catch (e: Exception) {
            println("Streak update error: ${e.message}")
        }
    }

    fun clearUser() {
        _user.value = null
    }
}
