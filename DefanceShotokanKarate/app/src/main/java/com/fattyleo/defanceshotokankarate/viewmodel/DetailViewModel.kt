package com.fattyleo.defanceshotokankarate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fattyleo.defanceshotokankarate.Destination
import com.fattyleo.defanceshotokankarate.Navigator
import kotlinx.coroutines.launch

class DetailViewModel(
    private val navigator: Navigator
): ViewModel()  {

    fun goBack() {
        viewModelScope.launch {
            navigator.navigateUp()
        }
    }
}