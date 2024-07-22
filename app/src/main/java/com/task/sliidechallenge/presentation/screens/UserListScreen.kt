package com.task.sliidechallenge.presentation.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.task.sliidechallenge.data.models.User
import com.task.sliidechallenge.presentation.viewmodels.UiState
import com.task.sliidechallenge.presentation.viewmodels.UserViewModel

/**
 * @Author Abdullah Abo El~Makarem on 21/07/2024.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(viewModel: UserViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsState()

    var showAddUserDialog by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    var userToDelete by rememberSaveable { mutableStateOf<User?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Users") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddUserDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add User")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        when (val state = uiState) {
            is UiState.Loading -> LoadingIndicator()
            is UiState.Error -> {
                LaunchedEffect(state.message) {
                    state.message.let {
                        snackbarHostState.showSnackbar(it)
                    }
                    viewModel.loadUsers()
                }
            }

            is UiState.Success -> {
                UserList(
                    innerPadding = innerPadding,
                    users = state.users,
                    onUserLongPress = { user ->
                        userToDelete = user
                        showDeleteDialog = true
                    }
                )
            }

        }

        AddUserDialog(
            showDialog = showAddUserDialog,
            onDismiss = {
                showAddUserDialog = false
                viewModel.loadUsers()
            },
            onAddUser = { name, email ->
                viewModel.createUser(name, email)
            }
        )

        DeleteConfirmationDialog(
            showDialog = showDeleteDialog,
            user = userToDelete,
            onConfirm = {
                userToDelete?.let { viewModel.deleteUser(it) }
                showDeleteDialog = false
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }
}

@Composable
fun UserList(innerPadding: PaddingValues, users: List<User>, onUserLongPress: (User) -> Unit) {
    LazyColumn(modifier = Modifier.padding(innerPadding)) {
        items(users) { user ->
            UserListItem(user, onUserLongPress)
        }
    }
}

@Composable
fun UserListItem(user: User, onLongPress: (User) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { onLongPress(user) }
                )
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${user.name}", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Email: ${user.email}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun userListPreview() {
    UserListScreen()
}