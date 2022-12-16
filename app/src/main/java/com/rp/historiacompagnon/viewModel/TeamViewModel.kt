package com.rp.historiacompagnon.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.rp.historiacompagnon.R
import com.rp.historiacompagnon.Services
import com.rp.historiacompagnon.entity.Team
import com.rp.historiacompagnon.entity.User
import com.rp.historiacompagnon.util.Utils
import com.rp.historiacompagnon.entity.Character

class TeamViewModel(team: Team, user: User, characters: ArrayList<Character>) {
    var team = team
    var user = user
    var characters = characters

    /**
     * Création équipe en tant que MJ
     */
    fun createTeam(name: String){
        team.name = name
        team.mj = user.key
        team.invitationCode = Utils.randomStringByKotlinRandom()

        // Save team
        Services.editTeam(team)

        // Ajout team au user
        updateUser()
    }

    /**
     * Ajout à l'équipe en tant que joueur
     */
    fun joinTeam(context: Context, invitationCode: String){
        Services.getTeamByInvitation(invitationCode).addOnSuccessListener {
            for (c in it.children) {
                team = c.getValue(Team::class.java)!!
            }
            if(team != Team()) {
                initNewCharacter()

                team.players.add(user.key)
                Services.editTeam(team)

                updateUser()
            } else {
                Toast.makeText(context, context.getString(R.string.team_invalid_invitation), Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener{
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Création nouveau perso ajouté à la team
     */
    private fun initNewCharacter() {
        val newCharater = Character()
        newCharater.team = team.key
        newCharater.player = user.key
        Services.editCharacter(newCharater, user.key)
    }

    /**
     * Maj user avec nouvelle team
     */
    fun updateUser() {
        if(team.key.isNotBlank()) {
            user.currentTeam = team.key
            Services.editUser(user)
        }
    }
}