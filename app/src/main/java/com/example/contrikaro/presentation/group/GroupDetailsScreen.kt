package com.example.contrikaro.presentation.group

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.contrikaro.components.ExpenseItem
import com.example.contrikaro.components.GlassCard
import com.example.contrikaro.presentation.group.AddExpenseDialog // Ensure this import is present
import com.example.contrikaro.presentation.group.GroupDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailsScreen(
    navController: NavController,
    viewModel: GroupDetailsViewModel = hiltViewModel()
) {
    val group by viewModel.group.collectAsState()
    val expenses by viewModel.expenses.collectAsState()

    // NOTE 1: This state variable controls when the dialog is shown.
    var showAddExpenseDialog by remember { mutableStateOf(false) }

    // NOTE 2: This is the block that calls the dialog. It now safely
    // unwraps the groupId and passes it.
    if (showAddExpenseDialog) {
        group?.id?.let { groupId ->
            AddExpenseDialog(
                onDismiss = { showAddExpenseDialog = false },
                viewModel = viewModel,
                groupId = groupId
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(group?.name ?: "Loading...") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            // NOTE 3: The Floating Action Button now correctly sets the state to true.
            FloatingActionButton(onClick = { showAddExpenseDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        },
        containerColor = Color.Transparent
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0)))
                )
                .padding(padding)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Cover Image
                val imageUrl = group?.coverImageUrl
                if (!imageUrl.isNullOrBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = "Group Cover",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Group Header
                Text(
                    text = "${group?.emoji ?: "🎉"} ${group?.name ?: "..."}",
                    style = MaterialTheme.typography.headlineMedium.copy(color = Color.White)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Balance Summary (still using placeholder values for now)
                GlassCard {
                    Text("Total Spent: ₹3,500", color = Color.White)
                    Text("You Lent: ₹1,250", color = Color(0xFF00FFAB))
                    Text("You Owe: ₹850", color = Color(0xFFFF6F61))
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text("Expenses", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                if (expenses.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center) {
                        Text("No expenses yet. Tap '+' to add one!", color = Color.White.copy(alpha = 0.8f))
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(expenses) { expense ->
                            ExpenseItem(
                                title = expense.title,
                                amount = "₹${"%.2f".format(expense.amount)}"
                            )
                        }
                    }
                }
            }
        }
    }
}