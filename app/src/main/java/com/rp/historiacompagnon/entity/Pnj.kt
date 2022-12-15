package com.rp.historiacompagnon.entity

import com.rp.historiacompagnon.enum.RelationshipTypeEnum

data class Pnj(
    var name: String,
    var species: String, // TODO voir remplacer par une enum ?
    var relationshipType: RelationshipTypeEnum,
    var description: String,
    var summary: String
) {
    constructor(): this("", "", RelationshipTypeEnum.NEUTRAL, "", "")
}