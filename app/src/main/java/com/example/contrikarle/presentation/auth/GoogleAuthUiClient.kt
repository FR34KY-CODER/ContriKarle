package com.example.contrikarle.presentation.auth

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient,
    private val webClientId: String
) {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getSignedInUser(): FirebaseUser? = auth.currentUser

    suspend fun signIn(): IntentSender {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(webClientId)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()

        val result = oneTapClient.beginSignIn(signInRequest).await()
        return result.pendingIntent.intentSender
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun signInWithIntent(intent: Intent): FirebaseUser? {
        val credential = Identity.getSignInClient(context).getSignInCredentialFromIntent(intent)
        val idToken = credential.googleIdToken
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = auth.signInWithCredential(firebaseCredential).await()
        return authResult.user
    }
}