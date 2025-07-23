package com.example.contrikaro.presentation.auth

import android.content.Intent
import android.content.IntentSender
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val signInIntent by viewModel.signInIntentSender.collectAsState()
    val signInSuccess by viewModel.signInSuccess.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        viewModel.handleGoogleSignInResult(result.data)
    }

    LaunchedEffect(signInIntent) {
        signInIntent?.let {
            val intentSender = it.getParcelableExtra<IntentSender>("intent_sender")
            intentSender?.let { sender ->
                val request = IntentSenderRequest.Builder(sender).build()
                launcher.launch(request)
            }
        }
    }

    LaunchedEffect(signInSuccess) {
        if (signInSuccess) {
            navController.navigate("main_screen") {
                popUpTo("login_screen") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { viewModel.launchGoogleSignIn() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in with Google")
        }
    }
}
