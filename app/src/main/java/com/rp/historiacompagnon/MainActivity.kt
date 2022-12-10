package com.rp.historiacompagnon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.util.ExtraConstants
import com.rp.historiacompagnon.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val liveData = viewModel.getDataSnapshotLiveData()
        liveData!!.observe(this, Observer { dataSnapshot ->
            Log.i("data = ", dataSnapshot.toString())
            if (dataSnapshot != null) {

            }
        })
    }

    init{
        viewModel = MainViewModel()
    }

    companion object{
        lateinit var viewModel : MainViewModel

        @SuppressLint("RestrictedApi")
        @JvmStatic
        open fun createIntent(context: Context, response: IdpResponse?): Intent? {
            return Intent().setClass(context, MainActivity::class.java)
                .putExtra(ExtraConstants.IDP_RESPONSE, response)
        }


    }
}