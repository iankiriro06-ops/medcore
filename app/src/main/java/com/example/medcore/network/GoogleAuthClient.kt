package com.example.medcore.network



import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.example.medcore.R
class GoogleAuthClient(private val context: Context) {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    private val client = GoogleSignIn.getClient(context, gso)

    fun getSignInIntent(): Intent = client.signInIntent

    fun firebaseAuthWithGoogle(
        idToken: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { result ->
                val user = result.user ?: return@addOnSuccessListener
                val isNew = result.additionalUserInfo?.isNewUser == true

                // Only save to Firestore if it's a new user
                if (isNew) {
                    val userRecord = hashMapOf(
                        "uid" to user.uid,
                        "fullName" to (user.displayName ?: ""),
                        "email" to (user.email ?: ""),
                        "subscribed" to false,
                        "createdAt" to System.currentTimeMillis()
                    )
                    db.collection("users").document(user.uid)
                        .set(userRecord)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { onFailure(it.message ?: "Failed to save profile") }
                } else {
                    onSuccess()
                }
            }
            .addOnFailureListener { onFailure(it.message ?: "Google sign-in failed") }
    }
}