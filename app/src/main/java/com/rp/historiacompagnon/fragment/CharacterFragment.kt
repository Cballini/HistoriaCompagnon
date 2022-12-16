package com.rp.historiacompagnon.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.rp.historiacompagnon.MainActivity
import com.rp.historiacompagnon.R
import com.rp.historiacompagnon.viewModel.CharacterViewModel
import com.rp.historiacompagnon.viewModel.TeamViewModel

class CharacterFragment : Fragment() {
    lateinit var viewModel: CharacterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_character, container, false)

        viewModel = CharacterViewModel(
            MainActivity.viewModel.currentCharacter.value!!
        )

        MainActivity.viewModel.currentCharacter.observe(viewLifecycleOwner, Observer {
            viewModel.character = it
            view.findViewById<TextView>(R.id.character_full_name).text = viewModel.character.concatFullName()
            view.findViewById<TextView>(R.id.character_species).text = viewModel.character.race.species
            view.findViewById<TextView>(R.id.character_age).text = viewModel.character.age.toString() + " ans"
        })

        return view
    }
}