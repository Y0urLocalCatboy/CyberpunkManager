package com.example.cyberpunkmanager

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cyberpunkmanager.data.Constants
import com.example.cyberpunkmanager.data.models.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataImporter {

    private val db = FirebaseFirestore.getInstance()
    private val gson = Gson()
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun importWeapons() = runBlocking { importCollection<weapon>("weapons.json", Constants.WEAPON) }

    @Test
    fun importCyberware() = runBlocking { importCollection<cyberware>("cyberware.json", Constants.CYBERWARES) }

    @Test
    fun importDrugs() = runBlocking { importCollection<drug>("drugs.json", Constants.DRUGS) }

    @Test
    fun importGadgets() = runBlocking { importCollection<gadget>("gadgets.json", Constants.GADGETS) }

    @Test
    fun importShards() = runBlocking { importCollection<shard>("shards.json", Constants.SHARDS) }

    @Test
    fun importQuickhacks() = runBlocking { importCollection<quickhack>("quickhacks.json", Constants.QUICKHACK) }

    @Test
    fun importDaemons() = runBlocking { importCollection<daemon>("daemons.json", Constants.DAEMON) }

    private suspend inline fun <reified T> importCollection(fileName: String, collectionName: String) {
        try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val jsonElement = gson.fromJson(jsonString, JsonElement::class.java)
            
            val jsonArray = if (jsonElement.isJsonArray) {
                jsonElement.asJsonArray
            } else {
                jsonElement.asJsonObject.entrySet().first().value.asJsonArray
            }

            val listType = object : TypeToken<List<T>>() {}.type
            val items: List<T> = gson.fromJson(jsonArray, listType)


            items.forEach { item ->
                try {
                    db.collection(collectionName).add(item!!).await()
                    println("Added: ${collectionName}")
                } catch (e: Exception) {
                    println("Error: $fileName: ${e.message}")
                }
            }
            
        } catch (e: Exception) {
            println("Critical Error (file $fileName): ${e.message}")
        }
    }
}
