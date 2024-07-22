package com.task.sliidechallenge.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.sliidechallenge.data.models.User
import com.task.sliidechallenge.domain.repos.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Author Abdullah Abo El~Makarem on 21/07/2024.
 */
@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState
    private var usersList: List<User> = emptyList()

    init {
        loadUsers()
    }

    fun loadUsers() {
        if (usersList.isNullOrEmpty().not()) {
            _uiState.value = UiState.Success(usersList)
            return
        }
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                usersList = repository.getUsers()
                _uiState.value = UiState.Success(usersList)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun createUser(name: String,  email: String) {
        viewModelScope.launch {
            try {
                val newUser = User(id = 0, name = name, email = email)
                val response = repository.createUser(newUser)
                if (response.isSuccessful && response.code() == 201) {
                    loadUsers()
                } else {
                    _uiState.value = UiState.Error("error while adding user")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message.orEmpty())
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            try {
                val response = repository.deleteUser(user.id)
                if (response.isSuccessful && response.code() == 204) {
                    loadUsers() // Reload the list after successful deletion
                } else {
                    _uiState.value = UiState.Error("error while deleting user")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message.orEmpty())
            }
        }
    }

}

sealed class UiState {

    object Loading : UiState()
    data class Success(val users: List<User>) : UiState()
    data class Error(val message: String) : UiState()
}