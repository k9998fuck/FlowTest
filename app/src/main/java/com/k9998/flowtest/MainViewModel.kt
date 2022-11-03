package com.k9998.flowtest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userDao: UserDao) : ViewModel() {

    val etQuery = MutableStateFlow(User(0, ""))
    val etInsert = MutableStateFlow(User(0, ""))

    private val query = MutableStateFlow(User(0, ""))
    private val onQuery: (user: User) -> Unit = { user ->
        query.value = User(0, user.name)
    }
    private val onInsert: (user: User) -> Unit = { user ->
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userDao.insert(user)
            }
        }
    }
    private val onUpdate: (user: User) -> Unit = { user ->
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userDao.update(user)
            }
        }
    }
    private val onDelete: (user: User) -> Unit = { user ->
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                userDao.delete(user)
            }
        }
    }

    val result: StateFlow<UsersUiState> = query.flatMapLatest {
        userDao.queryAll(it.name).map { users ->
            UsersUiState(
                users.map { user -> UsersItemUiState(user, onUpdate, { onDelete(user) }) },
                onQuery,
                onInsert, onUpdate, onDelete
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Or Lazily because it's a one-shot
        initialValue = UsersUiState(emptyList(), onQuery, onInsert, onUpdate, onDelete)
    )

}

data class UsersUiState(
    val users: List<UsersItemUiState>,
    val onQuery: (user: User) -> Unit,
    val onInsert: (user: User) -> Unit,
    val onUpdate: (user: User) -> Unit,
    val onDelete: (user: User) -> Unit
)

data class UsersItemUiState(
    val user: User,
    val onUpdate: (user: User) -> Unit,
    val onDelete: () -> Unit
)
