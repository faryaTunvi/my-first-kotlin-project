package com.fattyleo.defanceshotokankarate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fattyleo.defanceshotokankarate.Destination
import com.fattyleo.defanceshotokankarate.Navigator
import kotlinx.coroutines.launch

class HomeViewModel(
    private val navigator: Navigator
): ViewModel()  {

    fun navigateToDetail(id: String) {
        viewModelScope.launch {
            navigator.navigate(
                destination = Destination.DetailsScreen(id),
            )
        }
    }
}