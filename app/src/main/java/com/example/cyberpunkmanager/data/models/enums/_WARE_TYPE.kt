package com.example.cyberpunkmanager.data.models.enums

enum class _WARE_TYPE(val type: String, val cost: Int) {
    DEF("ware type error", -1),
    BIO("bio", 0),
    EGZO("egzo", 1),
    CYBER("cyber", 2),
    KORPO("korpo", 3),
    JUNK("złomo", 4),
    BORG("borg", 6),
}