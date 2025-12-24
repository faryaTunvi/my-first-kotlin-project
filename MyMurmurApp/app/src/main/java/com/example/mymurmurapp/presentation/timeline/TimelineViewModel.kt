package com.example.mymurmurapp.presentation.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymurmurapp.di.AppContainer
import com.example.mymurmurapp.domain.model.Murmur
import com.example.mymurmurapp.domain.usecase.GetTimelineUseCase
import com.example.mymurmurapp.domain.usecase.LikeMurmurUseCase
import com.example.mymurmurapp.domain.usecase.PostMurmurUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TimelineUiState(
    val murmurs: List<Murmur> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 0,
    val hasMorePages: Boolean = true
)

class TimelineViewModel(
    private val getTimelineUseCase: GetTimelineUseCase = AppContainer.provideGetTimelineUseCase(),
    private val likeMurmurUseCase: LikeMurmurUseCase = AppContainer.provideLikeMurmurUseCase(),
    private val postMurmurUseCase: PostMurmurUseCase = AppContainer.providePostMurmurUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimelineUiState())
    val uiState: StateFlow<TimelineUiState> = _uiState.asStateFlow()

    init {
        loadTimeline()
    }

    fun loadTimeline(refresh: Boolean = false) {
        viewModelScope.launch {
            if (refresh) {
                _uiState.value = _uiState.value.copy(
                    isRefreshing = true,
                    currentPage = 0,
                    error = null
                )
            } else {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            }

            val page = if (refresh) 0 else _uiState.value.currentPage

            getTimelineUseCase(page)
                .onSuccess { murmurs ->
                    val currentMurmurs = if (refresh) emptyList() else _uiState.value.murmurs
                    _uiState.value = _uiState.value.copy(
                        murmurs = currentMurmurs + murmurs,
                        isLoading = false,
                        isRefreshing = false,
                        hasMorePages = murmurs.size >= 10,
                        currentPage = page
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = error.message ?: "Failed to load timeline"
                    )
                }
        }
    }

    fun loadNextPage() {
        if (!_uiState.value.isLoading && _uiState.value.hasMorePages) {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val nextPage = _uiState.value.currentPage + 1

                getTimelineUseCase(nextPage)
                    .onSuccess { murmurs ->
                        _uiState.value = _uiState.value.copy(
                            murmurs = _uiState.value.murmurs + murmurs,
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

    fun toggleLike(murmur: Murmur) {
        viewModelScope.launch {
            likeMurmurUseCase(murmur.id, murmur.isLikedByCurrentUser)
                .onSuccess {
                    // Update the murmur in the list
                    val updatedMurmurs = _uiState.value.murmurs.map {
                        if (it.id == murmur.id) {
                            it.copy(
                                isLikedByCurrentUser = !it.isLikedByCurrentUser,
                                likesCount = if (it.isLikedByCurrentUser) it.likesCount - 1 else it.likesCount + 1
                            )
                        } else {
                            it
                        }
                    }
                    _uiState.value = _uiState.value.copy(murmurs = updatedMurmurs)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Failed to like murmur"
                    )
                }
        }
    }

    fun postMurmur(content: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            postMurmurUseCase(content)
                .onSuccess { newMurmur ->
                    _uiState.value = _uiState.value.copy(
                        murmurs = listOf(newMurmur) + _uiState.value.murmurs,
                        isLoading = false
                    )
                    onSuccess()
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Failed to post murmur"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

