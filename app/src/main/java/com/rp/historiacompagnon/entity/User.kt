package com.rp.historiacompagnon.entity

class User(var email: String,
           var nom: String) {

    constructor(): this("", "")
}