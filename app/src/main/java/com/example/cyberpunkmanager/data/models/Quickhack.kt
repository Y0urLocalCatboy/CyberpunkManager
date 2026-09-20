package com.example.cyberpunkmanager.data.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Quickhack {
    var id: String = ""
    var name: String = ""
    var cost: Int = 100
    var mechanics: List<String>? = null
}
