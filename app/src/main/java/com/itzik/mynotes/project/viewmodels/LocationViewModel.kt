package com.itzik.mynotes.project.viewmodels

import androidx.lifecycle.ViewModel
import com.itzik.mynotes.project.repositories.IRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val repo: IRepo
) : ViewModel() {

    private val _locationName = MutableStateFlow("")
    val locationName: StateFlow<String> get() = _locationName

    fun setLocationName(name: String) {
        _locationName.value = name
    }

}