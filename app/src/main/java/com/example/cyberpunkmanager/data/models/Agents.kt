package com.example.cyberpunkmanager.data.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
class Agent {
    var id: String = ""
    var name: String = ""
    var hitPoints: Int = 0
    var ww: Int = 0
    var intBonus: Int = 0
    var chaBonus: Int = 0
    var strBonus: Int = 0
    var spdBonus: Int = 0
    var accBonus: Int = 0
    var type: String = ""
    var uniqueName: String? = null
    var initialCost: Int = 0
    var monthlyCost: Int = 0
    var description: String = ""
    var actions: List<String>? = null
    var passiveActions: List<String>? = null
    var implants: List<String>? = null
}
