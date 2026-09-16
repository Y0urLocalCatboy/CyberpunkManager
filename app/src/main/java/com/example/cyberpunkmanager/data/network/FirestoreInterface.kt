package com.example.cyberpunkmanager.data.network

import com.example.cyberpunkmanager.data.models.cyberware
import com.example.cyberpunkmanager.data.models.daemon
import com.example.cyberpunkmanager.data.models.drug
import com.example.cyberpunkmanager.data.models.gadget
import com.example.cyberpunkmanager.data.models.quickhack
import com.example.cyberpunkmanager.data.models.shard
import com.example.cyberpunkmanager.data.models.weapon

interface FirestoreInterface {
    // Cyberware
    suspend fun addCyberware(item: cyberware): Result<Unit>
    suspend fun editCyberware(item: cyberware): Result<Unit>
    suspend fun deleteCyberware(id: String): Result<Unit>

    // Drug
    suspend fun addDrug(item: drug): Result<Unit>
    suspend fun editDrug(item: drug): Result<Unit>
    suspend fun deleteDrug(id: String): Result<Unit>

    // Gadget
    suspend fun addGadget(item: gadget): Result<Unit>
    suspend fun editGadget(item: gadget): Result<Unit>
    suspend fun deleteGadget(id: String): Result<Unit>

    // Shard
    suspend fun addShard(item: shard): Result<Unit>
    suspend fun editShard(item: shard): Result<Unit>
    suspend fun deleteShard(id: String): Result<Unit>

    // Quickhack
    suspend fun addQuickhack(item: quickhack): Result<Unit>
    suspend fun editQuickhack(item: quickhack): Result<Unit>
    suspend fun deleteQuickhack(id: String): Result<Unit>

    // Daemon
    suspend fun addDaemon(item: daemon): Result<Unit>
    suspend fun editDaemon(item: daemon): Result<Unit>
    suspend fun deleteDaemon(id: String): Result<Unit>

    // Weapon
    suspend fun addWeapon(item: weapon): Result<Unit>
    suspend fun editWeapon(item: weapon): Result<Unit>
    suspend fun deleteWeapon(id: String): Result<Unit>

    // Get All
    suspend fun getCyberwares(): Result<List<cyberware>>
    suspend fun getDrugs(): Result<List<drug>>
    suspend fun getGadgets(): Result<List<gadget>>
    suspend fun getShards(): Result<List<shard>>
    suspend fun getQuickhacks(): Result<List<quickhack>>
    suspend fun getDaemons(): Result<List<daemon>>
    suspend fun getWeapons(): Result<List<weapon>>
}
