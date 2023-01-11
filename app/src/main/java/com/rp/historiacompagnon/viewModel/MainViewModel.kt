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

    val _currentTeamCharacters = MutableLiveData<ArrayList<Character>>()
    val currentTeamCharacters : LiveData<ArrayList<Character>> get() = _currentTeamCharacters
    init {
        _currentTeamCharacters.value = ArrayList()

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

    fun getTeamCharactersDataSnapshotLiveData(): LiveData<DataSnapshot?> {
        return Services.getTeamCharactersDatabase(currentTeam.value!!.key)
    }


    fun editUser(user: User){
        Services.editUser(user)
    }

    fun editTeam(team: Team){
        Services.editTeam(team)
    }

    fun editCharacter(character: Character){
        if(null != _user.value){
            Services.editCharacter(character, _user.value!!.key, _currentTeam.value!!.mj)
        }
    }
}