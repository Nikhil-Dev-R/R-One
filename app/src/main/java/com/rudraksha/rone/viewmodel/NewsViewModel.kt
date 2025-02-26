package com.rudraksha.rone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudraksha.rone.model.Article
import com.rudraksha.rone.repo.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val _newsState = MutableStateFlow(NewsUiState())
    val newsState: StateFlow<NewsUiState> = _newsState

    fun fetchNews(category: String) {
        _newsState.value = _newsState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val response = NewsRepository.api.getNewsByCategory(category = category)
                if (response.status == "ok") {
                    _newsState.value = _newsState.value.copy(
                        news = mapOf(category to response.articles).toMutableMap()
                    )
                } else {
                    _newsState.value = _newsState.value.copy(errorMessage = "Response Status: ${response.status}")
                }
            } catch (e: Exception) {
                _newsState.value = _newsState.value.copy(errorMessage = e.message)
            }
        }
        _newsState.value = _newsState.value.copy(isLoading = false)
    }
}

data class NewsUiState(
    var news: MutableMap<String, List<Article>> = mutableMapOf(),
    var isLoading: Boolean = false,
    var errorMessage: String? = null
)
