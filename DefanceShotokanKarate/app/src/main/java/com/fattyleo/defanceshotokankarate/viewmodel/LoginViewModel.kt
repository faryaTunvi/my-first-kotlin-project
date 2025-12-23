package com.fattyleo.defanceshotokankarate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fattyleo.defanceshotokankarate.Destination
import com.fattyleo.defanceshotokankarate.Navigator
import kotlinx.coroutines.launch

class LoginViewModel(
    private val navigator: Navigator
): ViewModel()  {

    fun login() {
        viewModelScope.launch {
            navigator.navigate(
                destination = Destination.HomeGraph,
                navOptions = {
                    popUpTo(Destination.AuthGraph) {
                        inclusive = true
                    }
                }
            )
        }
    }
}