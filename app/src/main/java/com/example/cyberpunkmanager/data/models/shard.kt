package com.example.cyberpunkmanager.data.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class shard {
    var id: String = ""
    var name: String = ""
    var cost: Int = 0
    var description: String = ""
    var mechanics: List<String>? = null
}