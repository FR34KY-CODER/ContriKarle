package com.example.contrikarle.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contrikarle.data.model.User
import com.example.contrikarle.data.repository.AuthRepository
import com.example.contrikarle.presentation.auth.GoogleAuthUiClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel() {
    private val firebaseAuth = Firebase.auth
    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: StateFlow<FirebaseUser?> = _user

    fun signInWithGoogle(intent: Intent, authClient: GoogleAuthUiClient) {
        viewModelScope.launch {
            val userResult = authClient.signInWithIntent(intent)
            _user.value = userResult
        }
    }

}