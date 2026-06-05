package com.denzo.wakil.viewmodel

import androidx.lifecycle.ViewModel
import com.denzo.wakil.Util.HotelView
import com.denzo.wakil.domain.repository.HotelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: HotelRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<HotelView>>(emptyList())
    val searchResults: StateFlow<List<HotelView>> = _searchResults

    fun search(query: String) {
        // Implementation for searching hotels
    }
}
