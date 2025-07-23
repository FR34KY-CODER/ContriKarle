package com.example.contrikaro.domain.auth

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient,
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun signIn(): Intent {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("70297833790-bmsrltu0f5c9l7imn3lqpiann492kmso.apps.googleusercontent.com") // ⛔ Replace this
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        val result = oneTapClient.beginSignIn(signInRequest).await()
        return result.pendingIntent.intentSender?.let { sender ->
            Intent().apply { putExtra("intent_sender", sender) }
        } ?: throw Exception("Sign-in Intent not created")
    }

    suspend fun signInWithIntent(data: Intent?): AuthCredential? {
        return try {
            val credential: SignInCredential = oneTapClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken ?: throw Exception("No ID token!")
            GoogleAuthProvider.getCredential(idToken, null)
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Sign-in failed", e)
            null
        }
    }

    suspend fun signInWithFirebase(credential: AuthCredential): Boolean {
        return try {
            firebaseAuth.signInWithCredential(credential).await()
            true
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Firebase sign-in failed", e)
            false
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        oneTapClient.signOut()
    }

    fun getSignedInUser() = firebaseAuth.currentUser
}