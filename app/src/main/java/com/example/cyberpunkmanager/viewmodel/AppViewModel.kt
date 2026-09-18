package com.example.cyberpunkmanager.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyberpunkmanager.data.local.AppDatabase
import com.example.cyberpunkmanager.data.local.SavedItem
import com.example.cyberpunkmanager.data.models.*
import com.example.cyberpunkmanager.data.network.FirestoreClass
import com.example.cyberpunkmanager.data.network.FirestoreInterface
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val firestore: FirestoreInterface = FirestoreClass()

    private val db = AppDatabase.getDatabase(application)
    private val savedItemDao = db.savedItemDao()
    private val gson = Gson()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    private val _selectedItem = MutableStateFlow<Any?>(null)
    val selectedItem: StateFlow<Any?> = _selectedItem

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private val _selectedCategory = MutableStateFlow("")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _isCurrentItemSaved = MutableStateFlow(false)
    val isCurrentItemSaved: StateFlow<Boolean> = _isCurrentItemSaved

    val savedItems: StateFlow<List<Any>> = savedItemDao.getAllSavedItems()
        .map { list -> list.mapNotNull { convertToModel(it) } }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun selectItem(item: Any?, category: String = "") {
        _selectedItem.value = item
        _selectedCategory.value = category
        checkIfSaved(item)
    }

    private fun checkIfSaved(item: Any?) {
        val id = getObjectId(item)
        if (id != null) {
            viewModelScope.launch {
                _isCurrentItemSaved.value = savedItemDao.isItemSaved(id)
            }
        } else {
            _isCurrentItemSaved.value = false
        }
    }

    fun toggleSave(item: Any, category: String) {
        val id = getObjectId(item) ?: return
        viewModelScope.launch {
            if (savedItemDao.isItemSaved(id)) {
                savedItemDao.deleteSavedItemById(id)
                _isCurrentItemSaved.value = false
            } else {
                val name = getObjectName(item) ?: "Unknown"
                val json = gson.toJson(item)
                savedItemDao.insertSavedItem(SavedItem(id, name, category, json))
                _isCurrentItemSaved.value = true
            }
        }
    }

    private fun getObjectId(item: Any?): String? = when(item) {
        is cyberware -> item.id
        is drug -> item.id
        is gadget -> item.id
        is shard -> item.id
        is quickhack -> item.id
        is daemon -> item.id
        is weapon -> item.id
        else -> null
    }

    private fun getObjectName(item: Any?): String? = when(item) {
        is cyberware -> item.name
        is drug -> item.name
        is gadget -> item.name
        is shard -> item.name
        is quickhack -> item.name
        is daemon -> item.name
        is weapon -> item.name
        else -> null
    }

    private fun convertToModel(savedItem: SavedItem): Any? {
        return try {
            val clazz = when (savedItem.category) {
                "Cyberware" -> cyberware::class.java
                "Drugs" -> drug::class.java
                "Gadgets" -> gadget::class.java
                "Shards" -> shard::class.java
                "Quickhacks" -> quickhack::class.java
                "Daemons" -> daemon::class.java
                "Weapons" -> weapon::class.java
                else -> null
            }
            if (clazz != null) gson.fromJson(savedItem.jsonData, clazz) else null
        } catch (e: Exception) {
            null
        }
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
                "Weapons" -> firestore.getWeapons()
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
    fun addWeapon(item: weapon) = viewModelScope.launch { firestore.addWeapon(item) }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val items: List<Any>) : UiState()
        data class Error(val message: String) : UiState()
    }
}
