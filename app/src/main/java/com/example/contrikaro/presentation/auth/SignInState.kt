package com.example.contrikaro.presentation.auth

data class SignInState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)