package com.rp.historiacompagnon

import com.google.firebase.database.FirebaseDatabase
import com.rp.historiacompagnon.util.FirebaseQueryLiveData

object Services {

    @JvmStatic
    fun getUserDatabase(): FirebaseQueryLiveData = FirebaseQueryLiveData(FirebaseDatabase.getInstance(
        DATABASE_URL).getReference("user/"))

}