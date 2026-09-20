package com.example.cyberpunkmanager.data.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Daemon {
    var id: String = ""
    var name: String = ""
    var cost: Int = 200
    var mechanics: List<String>? = null
}
