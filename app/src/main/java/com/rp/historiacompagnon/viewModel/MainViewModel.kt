package com.rp.historiacompagnon.viewModel

import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.rp.historiacompagnon.Services

class MainViewModel {
    var firebaseQuery = Services.getUserDatabase()
    fun getDataSnapshotLiveData(): LiveData<DataSnapshot?> {
        return firebaseQuery
    }
}