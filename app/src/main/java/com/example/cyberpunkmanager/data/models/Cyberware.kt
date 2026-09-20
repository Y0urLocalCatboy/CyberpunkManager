package com.example.cyberpunkmanager.data.models

import com.example.cyberpunkmanager.data.models.enums._WARE_TYPE
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Cyberware {
    var id: String = ""
    var name: String = ""
    var uniqueName: String? = null
    var cost: Int = 0
    var pcCost: String = ""
    var description: String = ""
    var type: String = _WARE_TYPE.DEF.name
    var mechanics: List<String>? = null
}