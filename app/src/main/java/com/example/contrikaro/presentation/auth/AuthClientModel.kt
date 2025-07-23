package com.example.contrikaro.presentation.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contrikaro.domain.auth.GoogleAuthUiClient
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val googleAuthUiClient: GoogleAuthUiClient
) : ViewModel() {

    private val _signInIntentSender = MutableStateFlow<Intent?>(null)
    val signInIntentSender = _signInIntentSender.asStateFlow()

    private val _signInSuccess = MutableStateFlow(false)
    val signInSuccess = _signInSuccess.asStateFlow()

    fun launchGoogleSignIn() {
        viewModelScope.launch {
            try {
                val intent = googleAuthUiClient.signIn()
                _signInIntentSender.value = intent
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun handleGoogleSignInResult(data: Intent?) {
        viewModelScope.launch {
            val credential: AuthCredential? = googleAuthUiClient.signInWithIntent(data)
            if (credential != null) {
                val isSignedIn = googleAuthUiClient.signInWithFirebase(credential)
                _signInSuccess.value = isSignedIn
            }
        }
    }

    fun signOut() {
        googleAuthUiClient.signOut()
        _signInSuccess.value = false
    }
}