package com.example.cyberpunkmanager.data.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Weapon {
    var id: String = ""
    var name: String = ""
    var isRanged: Boolean = true
    var attack: String = ""
    var description: String = ""
    var uniqueName: String? = null
    var mechanics: List<String>? = null
    var cost: Int = 0
}
