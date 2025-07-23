package com.example.contrikaro.presentation.group

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.contrikaro.components.AddGroupDialog
import com.example.contrikaro.presentation.main.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun AllGroupsScreen(
    navController: NavController,
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val groups by viewModel.groups.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Group")
            }
        },
        containerColor = Color(0xFF1E1E2F),
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Your Groups",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(groups.size) { index ->
                    val group = groups[index]

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                // Navigate to group details screen with group ID
                                navController.navigate("group/${group.id}")
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                group.emoji ?: "🎉",
                                fontSize = MaterialTheme.typography.headlineMedium.fontSize
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(group.name, color = Color.White)
                                group.coverImageUrl?.let { url ->
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Image(
                                        painter = rememberAsyncImagePainter(url),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .height(120.dp)
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AddGroupDialog(
                onDismiss = { showDialog = false },
                onGroupAdded = { showDialog = false },
                viewModel = viewModel
            )
        }
    }
}
