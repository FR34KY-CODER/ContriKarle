package com.example.contrikarle.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleAuthUiClient(
    private val context: Context,
){
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("152296767175-hqrljgqvg6lmm4u8foctaunr6b93e9qo.apps.googleusercontent.com")
        .requestEmail()
        .build()
    private val googleSignInClient: GoogleSignInClient =
        GoogleSignIn.getClient(context, gso)

    fun signInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun getSignInResultFromIntent(data: Intent?): Task<GoogleSignInAccount> {
        return GoogleSignIn.getSignedInAccountFromIntent(data)
    }

    fun firebaseAuthWithGoogle(
        account: GoogleSignInAccount,
        auth: FirebaseAuth,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(task.exception ?: Exception("Unknown error"))
                }
            }
    }

    fun signOut() {
        googleSignInClient.signOut()
    }
}