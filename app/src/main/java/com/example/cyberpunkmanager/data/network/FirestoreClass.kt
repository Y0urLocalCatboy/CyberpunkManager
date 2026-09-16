package com.example.cyberpunkmanager.data.network

import com.example.cyberpunkmanager.data.Constants
import com.example.cyberpunkmanager.data.models.cyberware
import com.example.cyberpunkmanager.data.models.daemon
import com.example.cyberpunkmanager.data.models.drug
import com.example.cyberpunkmanager.data.models.gadget
import com.example.cyberpunkmanager.data.models.quickhack
import com.example.cyberpunkmanager.data.models.shard
import com.example.cyberpunkmanager.data.models.weapon
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

    private suspend inline fun <reified T> getAssets(collection: String): Result<List<T>> = try {
        val snapshot = mFirestore.collection(collection).get().await()
        val items = snapshot.documents.mapNotNull { doc ->
            try {
                doc.toObject(T::class.java)
            } catch (e: Exception) {
                android.util.Log.e("FirestoreClass", "Error parsing document ${doc.id} to ${T::class.java.simpleName}", e)
                null
            }
        }
        Result.success(items)
    } catch (e: Exception) {
        android.util.Log.e("FirestoreClass", "Error fetching collection $collection", e)
        Result.failure(e)
    }

    // Cyberware
    override suspend fun addCyberware(item: cyberware): Result<Unit> = addAsset(Constants.CYBERWARES, item)
    override suspend fun editCyberware(item: cyberware): Result<Unit> = editAsset(Constants.CYBERWARES, item.id, item)
    override suspend fun deleteCyberware(id: String): Result<Unit> = deleteAsset(Constants.CYBERWARES, id)
    override suspend fun getCyberwares(): Result<List<cyberware>> = getAssets(Constants.CYBERWARES)

    // Drug
    override suspend fun addDrug(item: drug): Result<Unit> = addAsset(Constants.DRUGS, item)
    override suspend fun editDrug(item: drug): Result<Unit> = editAsset(Constants.DRUGS, item.id, item)
    override suspend fun deleteDrug(id: String): Result<Unit> = deleteAsset(Constants.DRUGS, id)
    override suspend fun getDrugs(): Result<List<drug>> = getAssets(Constants.DRUGS)

    // Gadget
    override suspend fun addGadget(item: gadget): Result<Unit> = addAsset(Constants.GADGETS, item)
    override suspend fun editGadget(item: gadget): Result<Unit> = editAsset(Constants.GADGETS, item.id, item)
    override suspend fun deleteGadget(id: String): Result<Unit> = deleteAsset(Constants.GADGETS, id)
    override suspend fun getGadgets(): Result<List<gadget>> = getAssets(Constants.GADGETS)

    // Shard
    override suspend fun addShard(item: shard): Result<Unit> = addAsset(Constants.SHARDS, item)
    override suspend fun editShard(item: shard): Result<Unit> = editAsset(Constants.SHARDS, item.id, item)
    override suspend fun deleteShard(id: String): Result<Unit> = deleteAsset(Constants.SHARDS, id)
    override suspend fun getShards(): Result<List<shard>> = getAssets(Constants.SHARDS)

    // Quickhack
    override suspend fun addQuickhack(item: quickhack): Result<Unit> = addAsset(Constants.QUICKHACK, item)
    override suspend fun editQuickhack(item: quickhack): Result<Unit> = editAsset(Constants.QUICKHACK, item.id, item)
    override suspend fun deleteQuickhack(id: String): Result<Unit> = deleteAsset(Constants.QUICKHACK, id)
    override suspend fun getQuickhacks(): Result<List<quickhack>> = getAssets(Constants.QUICKHACK)

    // Daemon
    override suspend fun addDaemon(item: daemon): Result<Unit> = addAsset(Constants.DAEMON, item)
    override suspend fun editDaemon(item: daemon): Result<Unit> = editAsset(Constants.DAEMON, item.id, item)
    override suspend fun deleteDaemon(id: String): Result<Unit> = deleteAsset(Constants.DAEMON, id)
    override suspend fun getDaemons(): Result<List<daemon>> = getAssets(Constants.DAEMON)

    // Weapon
    override suspend fun addWeapon(item: weapon): Result<Unit> = addAsset(Constants.WEAPON, item)
    override suspend fun editWeapon(item: weapon): Result<Unit> = editAsset(Constants.WEAPON, item.id, item)
    override suspend fun deleteWeapon(id: String): Result<Unit> = deleteAsset(Constants.WEAPON, id)
    override suspend fun getWeapons(): Result<List<weapon>> = getAssets(Constants.WEAPON)
}
