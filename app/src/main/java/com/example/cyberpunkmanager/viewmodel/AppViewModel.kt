package com.example.cyberpunkmanager.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyberpunkmanager.data.models.cyberware
import com.example.cyberpunkmanager.data.models.daemon
import com.example.cyberpunkmanager.data.models.drug
import com.example.cyberpunkmanager.data.models.gadget
import com.example.cyberpunkmanager.data.models.quickhack
import com.example.cyberpunkmanager.data.models.shard
import com.example.cyberpunkmanager.data.network.FirestoreClass
import com.example.cyberpunkmanager.data.network.FirestoreInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val firestore: FirestoreInterface = FirestoreClass()
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    private val _selectedItem = MutableStateFlow<Any?>(null)
    val selectedItem: StateFlow<Any?> = _selectedItem

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun selectItem(item: Any?) {
        _selectedItem.value = item
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun loadCategory(category: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = when (category) {
                "Cyberware" -> firestore.getCyberwares()
                "Drugs" -> firestore.getDrugs()
                "Gadgets" -> firestore.getGadgets()
                "Shards" -> firestore.getShards()
                "Quickhacks" -> firestore.getQuickhacks()
                "Daemons" -> firestore.getDaemons()
                else -> Result.failure(Exception("Unknown category"))
            }
            
            result.onSuccess { items ->
                _uiState.value = UiState.Success(items)
            }.onFailure { error ->
                _uiState.value = UiState.Error(error.message ?: "Unknown error")
            }
        }
    }

    fun addCyberware(item: cyberware) = viewModelScope.launch { firestore.addCyberware(item) }
    fun addDrug(item: drug) = viewModelScope.launch { firestore.addDrug(item) }
    fun addGadget(item: gadget) = viewModelScope.launch { firestore.addGadget(item) }
    fun addShard(item: shard) = viewModelScope.launch { firestore.addShard(item) }
    fun addQuickhack(item: quickhack) = viewModelScope.launch { firestore.addQuickhack(item) }
    fun addDaemon(item: daemon) = viewModelScope.launch { firestore.addDaemon(item) }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val items: List<Any>) : UiState()
        data class Error(val message: String) : UiState()
    }
}
