package com.example.cyberpunkmanager.data.network

import com.example.cyberpunkmanager.data.Constants
import com.example.cyberpunkmanager.data.models.Cyberware
import com.example.cyberpunkmanager.data.models.Daemon
import com.example.cyberpunkmanager.data.models.Drug
import com.example.cyberpunkmanager.data.models.Gadget
import com.example.cyberpunkmanager.data.models.Quickhack
import com.example.cyberpunkmanager.data.models.Shard
import com.example.cyberpunkmanager.data.models.Weapon
import com.example.cyberpunkmanager.data.models.Agent
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

    private suspend inline fun <reified T : Any> getAssets(collection: String): Result<List<T>> = try {
        val snapshot = mFirestore.collection(collection).get().await()
        val items = snapshot.documents.mapNotNull { doc ->
            try {
                val item = doc.toObject(T::class.java)
                item?.apply {
                    // Przypisanie id z dokumentu Firestore do pola 'id' w obiekcie
                    try {
                        val field = javaClass.getDeclaredField("id")
                        field.isAccessible = true
                        field.set(this, doc.id)
                    } catch (_: Exception) {
                        // Ignorujemy, jeśli dany model nie ma pola 'id'
                    }
                }
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
    override suspend fun addCyberware(item: Cyberware): Result<Unit> = addAsset(Constants.CYBERWARES, item)
    override suspend fun editCyberware(item: Cyberware): Result<Unit> = editAsset(Constants.CYBERWARES, item.id, item)
    override suspend fun deleteCyberware(id: String): Result<Unit> = deleteAsset(Constants.CYBERWARES, id)
    override suspend fun getCyberwares(): Result<List<Cyberware>> = getAssets(Constants.CYBERWARES)

    // Drug
    override suspend fun addDrug(item: Drug): Result<Unit> = addAsset(Constants.DRUGS, item)
    override suspend fun editDrug(item: Drug): Result<Unit> = editAsset(Constants.DRUGS, item.id, item)
    override suspend fun deleteDrug(id: String): Result<Unit> = deleteAsset(Constants.DRUGS, id)
    override suspend fun getDrugs(): Result<List<Drug>> = getAssets(Constants.DRUGS)

    // Gadget
    override suspend fun addGadget(item: Gadget): Result<Unit> = addAsset(Constants.GADGETS, item)
    override suspend fun editGadget(item: Gadget): Result<Unit> = editAsset(Constants.GADGETS, item.id, item)
    override suspend fun deleteGadget(id: String): Result<Unit> = deleteAsset(Constants.GADGETS, id)
    override suspend fun getGadgets(): Result<List<Gadget>> = getAssets(Constants.GADGETS)

    // Shard
    override suspend fun addShard(item: Shard): Result<Unit> = addAsset(Constants.SHARDS, item)
    override suspend fun editShard(item: Shard): Result<Unit> = editAsset(Constants.SHARDS, item.id, item)
    override suspend fun deleteShard(id: String): Result<Unit> = deleteAsset(Constants.SHARDS, id)
    override suspend fun getShards(): Result<List<Shard>> = getAssets(Constants.SHARDS)

    // Quickhack
    override suspend fun addQuickhack(item: Quickhack): Result<Unit> = addAsset(Constants.QUICKHACK, item)
    override suspend fun editQuickhack(item: Quickhack): Result<Unit> = editAsset(Constants.QUICKHACK, item.id, item)
    override suspend fun deleteQuickhack(id: String): Result<Unit> = deleteAsset(Constants.QUICKHACK, id)
    override suspend fun getQuickhacks(): Result<List<Quickhack>> = getAssets(Constants.QUICKHACK)

    // Daemon
    override suspend fun addDaemon(item: Daemon): Result<Unit> = addAsset(Constants.DAEMON, item)
    override suspend fun editDaemon(item: Daemon): Result<Unit> = editAsset(Constants.DAEMON, item.id, item)
    override suspend fun deleteDaemon(id: String): Result<Unit> = deleteAsset(Constants.DAEMON, id)
    override suspend fun getDaemons(): Result<List<Daemon>> = getAssets(Constants.DAEMON)

    // Weapon
    override suspend fun addWeapon(item: Weapon): Result<Unit> = addAsset(Constants.WEAPON, item)
    override suspend fun editWeapon(item: Weapon): Result<Unit> = editAsset(Constants.WEAPON, item.id, item)
    override suspend fun deleteWeapon(id: String): Result<Unit> = deleteAsset(Constants.WEAPON, id)
    override suspend fun getWeapons(): Result<List<Weapon>> = getAssets(Constants.WEAPON)

    // Agent
    override suspend fun addAgent(item: Agent): Result<Unit> = addAsset(Constants.AGENT, item)
    override suspend fun editAgent(item: Agent): Result<Unit> = editAsset(Constants.AGENT, item.id, item)
    override suspend fun deleteAgent(id: String): Result<Unit> = deleteAsset(Constants.AGENT, id)
    override suspend fun getAgents(): Result<List<Agent>> = getAssets(Constants.AGENT)
}