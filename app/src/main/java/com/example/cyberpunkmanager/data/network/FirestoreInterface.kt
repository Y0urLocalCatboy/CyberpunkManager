package com.example.cyberpunkmanager.data.network

import com.example.cyberpunkmanager.data.models.Cyberware
import com.example.cyberpunkmanager.data.models.Daemon
import com.example.cyberpunkmanager.data.models.Drug
import com.example.cyberpunkmanager.data.models.Gadget
import com.example.cyberpunkmanager.data.models.Quickhack
import com.example.cyberpunkmanager.data.models.Shard
import com.example.cyberpunkmanager.data.models.Weapon
import com.example.cyberpunkmanager.data.models.Agent

interface FirestoreInterface {
    // Cyberware
    suspend fun addCyberware(item: Cyberware): Result<Unit>
    suspend fun editCyberware(item: Cyberware): Result<Unit>
    suspend fun deleteCyberware(id: String): Result<Unit>

    // Drug
    suspend fun addDrug(item: Drug): Result<Unit>
    suspend fun editDrug(item: Drug): Result<Unit>
    suspend fun deleteDrug(id: String): Result<Unit>

    // Gadget
    suspend fun addGadget(item: Gadget): Result<Unit>
    suspend fun editGadget(item: Gadget): Result<Unit>
    suspend fun deleteGadget(id: String): Result<Unit>

    // Shard
    suspend fun addShard(item: Shard): Result<Unit>
    suspend fun editShard(item: Shard): Result<Unit>
    suspend fun deleteShard(id: String): Result<Unit>

    // Quickhack
    suspend fun addQuickhack(item: Quickhack): Result<Unit>
    suspend fun editQuickhack(item: Quickhack): Result<Unit>
    suspend fun deleteQuickhack(id: String): Result<Unit>

    // Daemon
    suspend fun addDaemon(item: Daemon): Result<Unit>
    suspend fun editDaemon(item: Daemon): Result<Unit>
    suspend fun deleteDaemon(id: String): Result<Unit>

    // Weapon
    suspend fun addWeapon(item: Weapon): Result<Unit>
    suspend fun editWeapon(item: Weapon): Result<Unit>
    suspend fun deleteWeapon(id: String): Result<Unit>

    // Agent
    suspend fun addAgent(item: Agent): Result<Unit>
    suspend fun editAgent(item: Agent): Result<Unit>
    suspend fun deleteAgent(id: String): Result<Unit>

    // Get All
    suspend fun getCyberwares(): Result<List<Cyberware>>
    suspend fun getDrugs(): Result<List<Drug>>
    suspend fun getGadgets(): Result<List<Gadget>>
    suspend fun getShards(): Result<List<Shard>>
    suspend fun getQuickhacks(): Result<List<Quickhack>>
    suspend fun getDaemons(): Result<List<Daemon>>
    suspend fun getWeapons(): Result<List<Weapon>>
    suspend fun getAgents(): Result<List<Agent>>
}
