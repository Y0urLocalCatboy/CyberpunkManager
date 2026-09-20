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
        is Cyberware -> item.id
        is Drug -> item.id
        is Gadget -> item.id
        is Shard -> item.id
        is Quickhack -> item.id
        is Daemon -> item.id
        is Weapon -> item.id
        is Agent -> item.id
        else -> null
    }

    private fun getObjectName(item: Any?): String? = when(item) {
        is Cyberware -> item.uniqueName ?: item.name
        is Drug -> item.name
        is Gadget -> item.name
        is Shard -> item.name
        is Quickhack -> item.name
        is Daemon -> item.name
        is Weapon -> item.uniqueName ?: item.name
        is Agent -> item.uniqueName ?: item.name
        else -> null
    }

    private fun convertToModel(savedItem: SavedItem): Any? {
        return try {
            val clazz = when (savedItem.category) {
                "Cyberware" -> Cyberware::class.java
                "Drugs" -> Drug::class.java
                "Gadgets" -> Gadget::class.java
                "Shards" -> Shard::class.java
                "Quickhacks" -> Quickhack::class.java
                "Daemons" -> Daemon::class.java
                "Weapons" -> Weapon::class.java
                "Agents" -> Agent::class.java
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
                "Agents" -> firestore.getAgents()
                else -> Result.failure(Exception("Unknown category"))
            }
            
            result.onSuccess { items ->
                _uiState.value = UiState.Success(items)
            }.onFailure { error ->
                _uiState.value = UiState.Error(error.message ?: "Unknown error")
            }
        }
    }

    fun addCyberware(item: Cyberware) = viewModelScope.launch { firestore.addCyberware(item) }
    fun addDrug(item: Drug) = viewModelScope.launch { firestore.addDrug(item) }
    fun addGadget(item: Gadget) = viewModelScope.launch { firestore.addGadget(item) }
    fun addShard(item: Shard) = viewModelScope.launch { firestore.addShard(item) }
    fun addQuickhack(item: Quickhack) = viewModelScope.launch { firestore.addQuickhack(item) }
    fun addDaemon(item: Daemon) = viewModelScope.launch { firestore.addDaemon(item) }
    fun addWeapon(item: Weapon) = viewModelScope.launch { firestore.addWeapon(item) }
    fun addAgent(item: Agent) = viewModelScope.launch { firestore.addAgent(item) }

    fun editCyberware(item: Cyberware) = viewModelScope.launch { firestore.editCyberware(item) }
    fun editDrug(item: Drug) = viewModelScope.launch { firestore.editDrug(item) }
    fun editGadget(item: Gadget) = viewModelScope.launch { firestore.editGadget(item) }
    fun editShard(item: Shard) = viewModelScope.launch { firestore.editShard(item) }
    fun editQuickhack(item: Quickhack) = viewModelScope.launch { firestore.editQuickhack(item) }
    fun editDaemon(item: Daemon) = viewModelScope.launch { firestore.editDaemon(item) }
    fun editWeapon(item: Weapon) = viewModelScope.launch { firestore.editWeapon(item) }
    fun editAgent(item: Agent) = viewModelScope.launch { firestore.editAgent(item) }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        data class Success(val items: List<Any>) : UiState()
        data class Error(val message: String) : UiState()
    }
}
