package com.example.contrikaro.presentation.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.contrikaro.components.AddGroupDialog
import com.example.contrikaro.components.GlassButton
import com.example.contrikaro.components.GlassCard
import com.example.contrikaro.components.GroupItem
import com.example.contrikaro.navigation.Routes

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    // NOTE: We now get user and groups from the ViewModel's StateFlow
    val user by viewModel.currentUser.collectAsState()
    val groups by viewModel.groups.collectAsState()

    var showAddGroupDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Header Row - Now uses user data from ViewModel
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Profile picture
                if (user?.photoUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(user?.photoUrl),
                        contentDescription = "Profile Photo",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape)
                    )
                } else {
                    Spacer(modifier = Modifier.size(48.dp)) // Placeholder if no photo
                }

                Text(
                    text = "Dashboard",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )

                // We will add a profile/settings icon here later
                Spacer(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Total Balance Card - This is still a placeholder
            GlassCard {
                Text("TOTAL BALANCE", color = Color.White.copy(alpha = 0.7f))
                Text(
                    "₹0.00",
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    "Calculation coming soon...",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.6f))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Groups section - This is now connected to live data
            GlassCard {
                Text("GROUPS", color = Color.White.copy(alpha = 0.7f))
                Spacer(modifier = Modifier.height(8.dp))

                if (groups.isEmpty()) {
                    Text("No groups yet!", color = Color.White.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))
                } else {
                    // Display the list of groups
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        groups.forEach { group ->
                            Box(modifier = Modifier.clickable {
                                navController.navigate(Routes.GroupDetails.createRoute(group.id))
                            }) {
                                GroupItem("${group.emoji} ${group.name}")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                GlassButton("Create or Join Group") { showAddGroupDialog = true }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Sign out button - Now calls the ViewModel
            OutlinedButton(
                onClick = {
                    viewModel.signOut()
                    // After signing out, we should navigate back to the login screen.
                    // We'll add this logic properly when we finalize the navigation graph.
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
            ) {
                Text("Sign Out")
            }
        }
    }

    if (showAddGroupDialog) {
        // This uses the updated AddGroupDialog you already have
        AddGroupDialog(
            onDismiss = { showAddGroupDialog = false },
            onGroupAdded = { showAddGroupDialog = false },
            viewModel = viewModel
        )
    }
}