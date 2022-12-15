package com.rp.historiacompagnon

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.util.ExtraConstants
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rp.historiacompagnon.entity.Team
import com.rp.historiacompagnon.entity.User
import com.rp.historiacompagnon.fragment.*
import com.rp.historiacompagnon.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private val NUM_PAGES = 5
    private lateinit var mPager: ViewPager2
    // TODO check si besoin toutes permissions dans manifest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            startActivity(AuthActivity.createIntent(this))
            finish()
            return
        }

        initData()
        initNavigationView()
        initTopAppBar()
    }

    fun initData() {
        val auth = FirebaseAuth.getInstance()

        val liveData = viewModel.getUserDataSnapshotLiveData(auth.currentUser!!.email!!)
        liveData.observe(this) { dataSnapshot ->
            // Récupération user
            if (dataSnapshot != null && dataSnapshot.exists()) {
                for (c in dataSnapshot.children) {
                    viewModel._user.value = c.getValue(User::class.java)!! // récup infos
                }

                // une fois qu'on a user on va chercher la team actuelle (si existe)
                if (viewModel.user.value?.currentTeam?.isNotEmpty() == true) {
                    val liveDateTeam = viewModel.getTeamDataSnapshotLiveData(viewModel.user.value!!.currentTeam)
                    liveDateTeam.observe(this) { dataSnapshot ->
                        if (dataSnapshot != null) {
                            if (dataSnapshot.hasChildren()) {
                                for (c in dataSnapshot.children) {
                                    if (null != c.key) {
                                        viewModel._currentTeam.value = c.getValue(Team::class.java)
                                    }
                                }
                            }
                        }
                    }
                }

            } else {
                // Création user
                viewModel._user.value =
                    User(auth.currentUser!!.email, auth.currentUser!!.displayName, "", "")
                viewModel.editUser(viewModel.user.value!!)
            }
        }
    }

    private fun initTopAppBar() {
        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)

        topAppBar.setNavigationOnClickListener {
            Toast.makeText(applicationContext,"nav", Toast.LENGTH_SHORT).show()
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            Log.i("setOnMenuItemClick:", menuItem.toString())
            when (menuItem.itemId) {
                R.id.navigation_team -> {
                    TeamDialogFragment.newInstance(getString(R.string.app_name)).show(supportFragmentManager, TeamDialogFragment.TAG)
                    true
                }
                R.id.item_credit -> {
                    MaterialAlertDialogBuilder(this)
                        .setTitle(resources.getString(R.string.credit))
                        .setMessage(resources.getString(R.string.credit_msg))
                        .setPositiveButton(resources.getString(R.string.ok)) { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                    true
                }
                // TODO voir gestion déco
//                R.id.item_logout -> {
//                    Firebase.auth.signOut()
//                    val currentUser = FirebaseAuth.getInstance().currentUser
//                    Log.i("currentUser = ", currentUser.toString())
//                    if (currentUser == null) {
//                        startActivity(AuthActivity.createIntent(this))
//                        finish()
//                    }
//                    true
//                }
                else -> false
            }
        }
    }

    private fun initNavigationView() {
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.fragment_container)

        // The pager adapter, which provides the pages to the view pager widget.
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        mPager.adapter = pagerAdapter
        mPager.currentItem = 0
        mPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onPageSelected(position: Int) {
                when(position){
                    0 -> findViewById<BottomNavigationView>(R.id.navigation).selectedItemId = R.id.navigation_character
                    1 -> findViewById<BottomNavigationView>(R.id.navigation).selectedItemId = R.id.navigation_abilities
                    2 -> findViewById<BottomNavigationView>(R.id.navigation).selectedItemId = R.id.navigation_fight
                    3 -> findViewById<BottomNavigationView>(R.id.navigation).selectedItemId = R.id.navigation_relashionship
                    4 -> findViewById<BottomNavigationView>(R.id.navigation).selectedItemId = R.id.navigation_inventory
                    else -> findViewById<BottomNavigationView>(R.id.navigation).selectedItemId = R.id.navigation_character
                }
                super.onPageSelected(position)
            }
        })

        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_character -> {
                    mPager.currentItem = 0
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_abilities -> {
                    mPager.currentItem = 1
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_fight -> {
                    mPager.currentItem = 2
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_relashionship -> {
                    mPager.currentItem = 3
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_inventory -> {
                    mPager.currentItem = 4
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            return when(position){
                0 -> CharacterFragment()
                1 -> AbilitiesFragment()
                2 -> FightFragment()
                3 -> RelationshipFragment()
                4 -> InventoryFragment()
                else -> CharacterFragment()
            }
        }
    }


    init {
        viewModel = MainViewModel()
    }

    companion object {
        lateinit var viewModel: MainViewModel

        @SuppressLint("RestrictedApi")
        @JvmStatic
        open fun createIntent(context: Context, response: IdpResponse?): Intent? {
            return Intent().setClass(context, MainActivity::class.java)
                .putExtra(ExtraConstants.IDP_RESPONSE, response)
        }

    }
}