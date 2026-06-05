package com.denzo.wakil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denzo.wakil.Database.UserEntity
import com.denzo.wakil.domain.repository.UserRepository
import com.denzo.wakil.Util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = userRepository.login(username, password)
            if (user != null) {
                sessionManager.saveSession(username)
                _authState.value = AuthState.Authenticated(user)
            } else {
                _authState.value = AuthState.Error("Invalid credentials")
            }
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val existing = userRepository.getUserByUsername(username)
            if (existing != null) {
                _authState.value = AuthState.Error("User already exists")
            } else {
                val newUser = UserEntity(username, password)
                userRepository.register(newUser)
                _authState.value = AuthState.Authenticated(newUser)
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: UserEntity) : AuthState()
    data class Error(val message: String) : AuthState()
}
