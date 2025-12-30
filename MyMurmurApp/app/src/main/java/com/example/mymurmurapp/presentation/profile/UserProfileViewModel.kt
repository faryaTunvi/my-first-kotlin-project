package com.example.mymurmurapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymurmurapp.di.AppContainer
import com.example.mymurmurapp.domain.model.Murmur
import com.example.mymurmurapp.domain.model.User
import com.example.mymurmurapp.domain.repository.AuthRepository
import com.example.mymurmurapp.domain.usecase.DeleteMurmurUseCase
import com.example.mymurmurapp.domain.usecase.FollowUserUseCase
import com.example.mymurmurapp.domain.usecase.GetUserByIdUseCase
import com.example.mymurmurapp.domain.usecase.GetUserMurmursUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UserProfileUiState(
    val user: User? = null,
    val feeds: List<Murmur> = emptyList(),
    val isLoading: Boolean = false,
    val isFollowing: Boolean = false,
    val error: String? = null,
    val currentUserId: String? = null,
    val isOwnProfile: Boolean = false,
    val currentPage: Int = 0,
    val hasMorePages: Boolean = true
)

class UserProfileViewModel(
    private val getUserByIdUseCase: GetUserByIdUseCase = AppContainer.provideGetUserByIdUseCase(),
    private val getUserMurmursUseCase: GetUserMurmursUseCase = AppContainer.provideGetUserMurmursUseCase(),
    private val followUserUseCase: FollowUserUseCase = AppContainer.provideFollowUserUseCase(),
    private val deleteMurmurUseCase: DeleteMurmurUseCase = AppContainer.provideDeleteMurmurUseCase(),
    private val authRepository: AuthRepository = AppContainer.provideAuthRepository(),
    private val userRepository: com.example.mymurmurapp.domain.repository.UserRepository = AppContainer.provideUserRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserProfileUiState())
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val currentUserId = authRepository.getCurrentUserId()
            val isOwnProfile = currentUserId == userId

            getUserByIdUseCase(userId)
                .onSuccess { user ->
                    // Check if following
                    val isFollowing = if (!isOwnProfile && currentUserId != null) {
                        userRepository.isFollowing(userId).getOrDefault(false)
                    } else {
                        false
                    }

                    _uiState.value = _uiState.value.copy(
                        user = user,
                        currentUserId = currentUserId,
                        isOwnProfile = isOwnProfile,
                        isFollowing = isFollowing
                    )

                    loadUserMurmurs(userId)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load user profile"
                    )
                }
        }
    }

    private fun loadUserMurmurs(userId: String) {
        viewModelScope.launch {
            getUserMurmursUseCase(userId, 0)
                .onSuccess { murmurs ->
                    _uiState.value = _uiState.value.copy(
                        feeds = murmurs,
                        isLoading = false,
                        hasMorePages = murmurs.size >= 10,
                        currentPage = 0
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to load murmurs"
                    )
                }
        }
    }

    fun loadNextPage() {
        val user = _uiState.value.user ?: return
        if (!_uiState.value.isLoading && _uiState.value.hasMorePages) {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val nextPage = _uiState.value.currentPage + 1

                getUserMurmursUseCase(user.id, nextPage)
                    .onSuccess { murmurs ->
                        _uiState.value = _uiState.value.copy(
                            feeds = _uiState.value.feeds + murmurs,
                            isLoading = false,
                            hasMorePages = murmurs.size >= 10,
                            currentPage = nextPage
                        )
                    }
                    .onFailure { error ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = error.message ?: "Failed to load more murmurs"
                        )
                    }
            }
        }
    }

    fun toggleFollow() {
        val user = _uiState.value.user ?: return

        viewModelScope.launch {
            followUserUseCase(user.id, _uiState.value.isFollowing)
                .onSuccess {
                    val newFollowState = !_uiState.value.isFollowing
                    val updatedUser = user.copy(
                        followersCount = if (newFollowState) user.followersCount + 1 else user.followersCount - 1
                    )
                    _uiState.value = _uiState.value.copy(
                        isFollowing = newFollowState,
                        user = updatedUser
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Failed to follow/unfollow user"
                    )
                }
        }
    }

    fun deleteMurmur(murmurId: String) {
        viewModelScope.launch {
            deleteMurmurUseCase(murmurId)
                .onSuccess {
                    val updatedMurmurs = _uiState.value.feeds.filter { it.id != murmurId }
                    _uiState.value = _uiState.value.copy(feeds = updatedMurmurs)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Failed to delete murmur"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            authRepository.signOut()
                .onSuccess {
                    onLogoutSuccess()
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Failed to logout"
                    )
                }
        }
    }
}

