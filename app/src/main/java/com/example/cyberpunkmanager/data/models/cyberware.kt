package com.example.cyberpunkmanager.data.models

import com.example.cyberpunkmanager.data.models.enums._WARE_TYPE

class cyberware {
    val id: String = ""
    var name: String = ""
    var uniqueName: String? = null
    var cost: Int = 0
    var description: String = ""
    var type: _WARE_TYPE = _WARE_TYPE.DEF
    val mechanics: List<String>? = null
}