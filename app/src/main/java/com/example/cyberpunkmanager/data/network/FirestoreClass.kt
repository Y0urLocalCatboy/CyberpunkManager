package com.example.cyberpunkmanager.data.network

import com.example.cyberpunkmanager.data.Constants
import com.example.cyberpunkmanager.data.models.cyberware
import com.example.cyberpunkmanager.data.models.daemon
import com.example.cyberpunkmanager.data.models.drug
import com.example.cyberpunkmanager.data.models.gadget
import com.example.cyberpunkmanager.data.models.quickhack
import com.example.cyberpunkmanager.data.models.shard
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreClass : FirestoreInterface {

    private val mFirestore = FirebaseFirestore.getInstance()

    private suspend fun addAsset(collection: String, item: Any): Result<Unit> = try {
        mFirestore.collection(collection)
            .add(item)
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun editAsset(collection: String, id: String, item: Any): Result<Unit> = try {
        mFirestore.collection(collection)
            .document(id)
            .set(item)
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun deleteAsset(collection: String, id: String): Result<Unit> = try {
        mFirestore.collection(collection)
            .document(id)
            .delete()
            .await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Cyberware
    override suspend fun addCyberware(item: cyberware): Result<Unit> = addAsset(Constants.CYBERWARES, item)
    override suspend fun editCyberware(item: cyberware): Result<Unit> = editAsset(Constants.CYBERWARES, item.id, item)
    override suspend fun deleteCyberware(id: String): Result<Unit> = deleteAsset(Constants.CYBERWARES, id)

    // Drug
    override suspend fun addDrug(item: drug): Result<Unit> = addAsset(Constants.DRUGS, item)
    override suspend fun editDrug(item: drug): Result<Unit> = editAsset(Constants.DRUGS, item.id, item)
    override suspend fun deleteDrug(id: String): Result<Unit> = deleteAsset(Constants.DRUGS, id)

    // Gadget
    override suspend fun addGadget(item: gadget): Result<Unit> = addAsset(Constants.GADGETS, item)
    override suspend fun editGadget(item: gadget): Result<Unit> = editAsset(Constants.GADGETS, item.id, item)
    override suspend fun deleteGadget(id: String): Result<Unit> = deleteAsset(Constants.GADGETS, id)

    // Shard
    override suspend fun addShard(item: shard): Result<Unit> = addAsset(Constants.SHARDS, item)
    override suspend fun editShard(item: shard): Result<Unit> = editAsset(Constants.SHARDS, item.id, item)
    override suspend fun deleteShard(id: String): Result<Unit> = deleteAsset(Constants.SHARDS, id)

    // Quickhack
    override suspend fun addQuickhack(item: quickhack): Result<Unit> = addAsset(Constants.QUICKHACK, item)
    override suspend fun editQuickhack(item: quickhack): Result<Unit> = editAsset(Constants.QUICKHACK, item.id, item)
    override suspend fun deleteQuickhack(id: quickhack): Result<Unit> = deleteAsset(Constants.QUICKHACK, id)

    // Daemon
    override suspend fun addDaemon(item: daemon): Result<Unit> = addAsset(Constants.DAEMON, item)
    override suspend fun editDaemon(item: daemon): Result<Unit> = editAsset(Constants.DAEMON, item.id, item)
    override suspend fun deleteDaemon(id: daemon): Result<Unit> = deleteAsset(Constants.DAEMON, id)
}
