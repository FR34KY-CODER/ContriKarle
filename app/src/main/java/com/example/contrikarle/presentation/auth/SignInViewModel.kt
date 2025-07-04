package com.example.contrikarle.presentation.auth

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user = _user.asStateFlow()

    fun handleSignInResult(client: GoogleAuthUiClient, intent: Intent) {
        viewModelScope.launch {
            try {
                val result = client.signInWithIntent(intent)
                _user.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}