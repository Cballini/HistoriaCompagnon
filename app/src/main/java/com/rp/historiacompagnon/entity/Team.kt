package com.rp.historiacompagnon.entity

data class Team(var invitationCode: String,
                var mj: String, // user.key
                var players: ArrayList<String>, // user.key TODO verif nécessaire ?
                var name: String,
                var key: String) {

    constructor(): this("", "", ArrayList(), "", "")
}