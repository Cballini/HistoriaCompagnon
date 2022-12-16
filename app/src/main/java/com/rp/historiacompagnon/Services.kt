package com.rp.historiacompagnon

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.rp.historiacompagnon.entity.Team
import com.rp.historiacompagnon.entity.User
import com.rp.historiacompagnon.entity.Character
import com.rp.historiacompagnon.util.FirebaseQueryLiveData

object Services {
    val database = FirebaseDatabase.getInstance(DATABASE_URL).reference

    /******** GET *******/
    @JvmStatic
    fun getUserDatabase(email: String): FirebaseQueryLiveData = FirebaseQueryLiveData(database.child("user").orderByChild("email").equalTo(email))

    @JvmStatic
    fun getTeamMjDatabase(mjKey: String): FirebaseQueryLiveData = FirebaseQueryLiveData(database.child("team").orderByChild("mj").equalTo(mjKey))

    @JvmStatic
    fun getTeamDatabase(teamKey: String): FirebaseQueryLiveData = FirebaseQueryLiveData(database.child("team").orderByChild("key").equalTo(teamKey))

    @JvmStatic
    fun getTeamByInvitation(invitationCode: String): Task<DataSnapshot> = database.child("team").orderByChild("invitationCode").equalTo(invitationCode).get()

    @JvmStatic
    fun getTeamCharactersDatabase(teamKey: String): FirebaseQueryLiveData = FirebaseQueryLiveData(database.child("character").orderByChild("team").equalTo(teamKey))


    /******** EDIT *******/
    @JvmStatic
    fun editUser(user: User){
        val ref = database.child("user")
        if(user.key.isEmpty()){
            val key = ref.push().key
            if (key == null) {
                Log.w(TAG, "Couldn't get push key for posts")
                return
            }

            user.key = key
        }

        val childUpdates = hashMapOf<String, Any>(
            "/${user.key}" to user
        )

        ref.updateChildren(childUpdates)
    }

    @JvmStatic
    fun editTeam(team: Team){
        val ref = database.child("team")

        if(team.key.isEmpty()) {
            val key = ref.push().key
            if (key == null) {
                Log.w(TAG, "Couldn't get push key for posts")
                return
            }

            team.key = key
        }

        val childUpdates = hashMapOf<String, Any>(
            "/${team.key}" to team
        )

        ref.updateChildren(childUpdates)
    }

    @JvmStatic
    fun editCharacter(character: Character, userKey: String){
        // Edition perso possible si user = player
        if(userKey == character.player) {
            val ref = database.child("character")
            if(character.key.isEmpty()){
                val key = ref.push().key
                if (key == null) {
                    Log.w(TAG, "Couldn't get push key for posts")
                    return
                }

                character.key = key
            }

            val childUpdates = hashMapOf<String, Any>(
                "/${character.key}" to character
            )

            ref.updateChildren(childUpdates)
        }
    }
}