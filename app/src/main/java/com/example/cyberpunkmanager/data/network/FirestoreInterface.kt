package com.example.cyberpunkmanager.data.network

import com.example.cyberpunkmanager.data.models.cyberware
import com.example.cyberpunkmanager.data.models.daemon
import com.example.cyberpunkmanager.data.models.drug
import com.example.cyberpunkmanager.data.models.gadget
import com.example.cyberpunkmanager.data.models.quickhack
import com.example.cyberpunkmanager.data.models.shard

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
    suspend fun deleteQuickhack(id: quickhack): Result<Unit>

    // Daemon
    suspend fun addDaemon(item: daemon): Result<Unit>
    suspend fun editDaemon(item: daemon): Result<Unit>
    suspend fun deleteDaemon(id: daemon): Result<Unit>
}
