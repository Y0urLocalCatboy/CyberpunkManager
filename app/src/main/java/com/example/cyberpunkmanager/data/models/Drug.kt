package com.example.cyberpunkmanager.data.models

import com.example.cyberpunkmanager.data.models.enums.DICE
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Drug {
    var id: String = ""
    var name: String = ""
    var cost: Int = 0
    var addiction_risk: String = DICE.DEF.name
    var description: String = ""
    var mechanics: List<String>? = null
}