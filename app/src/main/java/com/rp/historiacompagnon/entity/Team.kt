package com.rp.historiacompagnon.entity

class Team(var invitationCode: String, var mj: String, var players: ArrayList<String>) {

    // TODO mj = user.key, players = list user.key

    constructor(): this("", "", ArrayList())
}