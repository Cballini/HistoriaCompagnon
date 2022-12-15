package com.rp.historiacompagnon.entity

import com.rp.historiacompagnon.enum.AptitudeTagEnum
import com.rp.historiacompagnon.enum.AptitudeTypeEnum

data class Aptitude(
    var name: String,
    var effect: String,
    var damage: String,
    var heal: String,
    var scope: String,
    var duration: String,
    var type: AptitudeTypeEnum,
    var tag: ArrayList<AptitudeTagEnum>
) {

    constructor() : this("", "", "", "", "", "", AptitudeTypeEnum.OTHER, ArrayList())
}
