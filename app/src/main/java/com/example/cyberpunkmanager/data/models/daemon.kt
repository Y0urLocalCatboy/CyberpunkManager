package com.example.cyberpunkmanager.data.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class daemon {
    var id: String = ""
    var name: String = ""
    var cost: Int = 200
    var mechanics: List<String>? = null
}
