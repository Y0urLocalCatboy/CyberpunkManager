package com.example.cyberpunkmanager.data.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Shard {
    var id: String = ""
    var name: String = ""
    var cost: Int = 0
    var mechanics: List<String>? = null
}