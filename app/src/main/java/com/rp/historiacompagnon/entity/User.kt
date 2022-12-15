package com.rp.historiacompagnon.entity

data class User(
    var email: String?,
    var nom: String?,
    var currentTeam: String, // team.key
    var key: String
) {

    var teams: ArrayList<String> = ArrayList() // team.key évol

    constructor() : this("", "", "", "")
}