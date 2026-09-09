package com.example.cyberpunkmanager.data.models

import com.example.cyberpunkmanager.data.models.enums.DICE

class drug {
    val id: String = ""
    var name: String = ""
    var cost: Int = 0
    var addiction_risk: DICE = DICE.DEF
    var description: String = ""
    val mechanics: List<String>? = null
}