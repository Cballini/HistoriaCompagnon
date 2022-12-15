package com.rp.historiacompagnon.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.rp.historiacompagnon.Services
import com.rp.historiacompagnon.entity.Team
import com.rp.historiacompagnon.entity.User
import com.rp.historiacompagnon.entity.Character

class MainViewModel {

    val _user = MutableLiveData<User>()
    val user : LiveData<User> get() = _user
    init {
        _user.value = User()
    }

    val _currentTeam = MutableLiveData<Team>()
    val currentTeam : LiveData<Team> get() = _currentTeam
    init {
        _currentTeam.value = Team()
    }

    val _currentCharacter = MutableLiveData<Character>()
    val currentCharacter : LiveData<Character> get() = _currentCharacter
    init {
        _currentCharacter.value = Character()
    }


    fun getUserDataSnapshotLiveData(email: String): LiveData<DataSnapshot?> {
        return Services.getUserDatabase(email)
    }

    fun getTeamMjDataSnapshotLiveData(mjKey: String): LiveData<DataSnapshot?> {
        return Services.getTeamMjDatabase(mjKey)
    }

    fun getTeamDataSnapshotLiveData(teamKey: String): LiveData<DataSnapshot?> {
        return Services.getTeamDatabase(teamKey)
    }

    fun getCharacterDataSnapshotLiveData(characterKey: String): LiveData<DataSnapshot?> {
        return Services.getCharacterDatabase(characterKey)
    }

    fun editUser(user: User){
        Services.editUser(user)
    }

    fun editTeam(team: Team){
        Services.editTeam(team)
    }

    fun editCharacter(character: Character){
        if(null != _user.value){
            Services.editCharacter(character, _user.value!!.key)
        }
    }
}