package com.rp.historiacompagnon.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rp.historiacompagnon.MainActivity
import com.rp.historiacompagnon.Preferences
import com.rp.historiacompagnon.Preferences.PREF_CURRENT_CHARACTER
import com.rp.historiacompagnon.Preferences.PRIVATE_MODE
import com.rp.historiacompagnon.R
import com.rp.historiacompagnon.adapter.PlayerAdapter
import com.rp.historiacompagnon.entity.Team
import com.rp.historiacompagnon.viewModel.TeamViewModel
import com.rp.historiacompagnon.entity.Character;
import com.rp.historiacompagnon.util.RecyclerViewClickListener

class TeamDialogFragment : DialogFragment(), RecyclerViewClickListener {
    lateinit var viewModel: TeamViewModel
    private lateinit var recyclerViewPlayer: RecyclerView
    private lateinit var viewAdapterPlayer: PlayerAdapter
    private lateinit var viewManagerPlayer: RecyclerView.LayoutManager
    var isOnCreate = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Material_Dialog)

        viewModel = TeamViewModel(
            MainActivity.viewModel.currentTeam.value!!,
            MainActivity.viewModel.user.value!!,
            MainActivity.viewModel.currentTeamCharacters.value!!
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_dialog_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners(view)
        setupAdapter(view)

        MainActivity.viewModel.currentTeam.observe(viewLifecycleOwner, Observer {
            viewModel.team = it
            setupView(view)
        })

        MainActivity.viewModel.currentTeamCharacters.observe(viewLifecycleOwner, Observer {
            viewModel.characters = it
            viewAdapterPlayer.mDataset = viewModel.characters
            viewAdapterPlayer.notifyDataSetChanged()
        })

        MainActivity.viewModel.currentCharacter.observe(viewLifecycleOwner, Observer {
            viewAdapterPlayer.characterKey = it.key
            viewAdapterPlayer.notifyDataSetChanged()
        })
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    private fun setupView(view: View) {
        if (viewModel.team == Team()) { // Pas de Team
            view.findViewById<TextView>(R.id.team_title).text = getString(R.string.team_title)
            view.findViewById<ConstraintLayout>(R.id.team_display).visibility = View.GONE
            view.findViewById<ConstraintLayout>(R.id.team_init).visibility = View.VISIBLE
        } else { // Dans team
            view.findViewById<TextView>(R.id.team_title).text = viewModel.team.name
            view.findViewById<ConstraintLayout>(R.id.team_display).visibility = View.VISIBLE
            view.findViewById<ConstraintLayout>(R.id.team_init).visibility = View.GONE
            view.findViewById<TextView>(R.id.team_invitation_code_code).text =
                viewModel.team.invitationCode
        }
    }

    private fun setupAdapter(view: View) {
        viewManagerPlayer = LinearLayoutManager(this.context)
        viewAdapterPlayer = PlayerAdapter(
            viewModel.characters,
            this,
            MainActivity.viewModel.currentCharacter.value!!.key
        )

        recyclerViewPlayer = view.findViewById<RecyclerView>(R.id.team_recycler).apply {
            // use a linear layout manager
            layoutManager = viewManagerPlayer

            // specify an viewAdapter (see also next example)
            adapter = viewAdapterPlayer
        }
    }

    private fun setupClickListeners(view: View) {
        view.findViewById<ImageView>(R.id.team_back).setOnClickListener {
            dismiss()
        }

        changeDisplayNewTeam(view)

        validateNewTeam(view)

        copyInvitationCode(view)
    }

    /**
     * Gestion affichage champs création ou join
     */
    private fun changeDisplayNewTeam(view: View) {
        val buttonCreate = view.findViewById<Button>(R.id.team_create)
        val buttonJoin = view.findViewById<Button>(R.id.team_join)
        val orderText = view.findViewById<TextView>(R.id.team_order_text)
        val joinAsText = view.findViewById<TextView>(R.id.team_join_as)

        buttonCreate.setOnClickListener {
            buttonJoin.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.medium_grey)
            buttonCreate.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.colorPrimaryDark)
            orderText.text = getString(R.string.team_choose_name)
            joinAsText.text = getString(R.string.team_join_as_mj)
            isOnCreate = true
        }

        buttonJoin.setOnClickListener {
            buttonCreate.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.medium_grey)
            buttonJoin.backgroundTintList =
                ContextCompat.getColorStateList(requireContext(), R.color.colorPrimaryDark)
            orderText.text = getString(R.string.team_invitation_code)
            joinAsText.text = getString(R.string.team_join_as_player)
            isOnCreate = false
        }
    }

    /**
     * Gestion validation création ou join
     */
    private fun validateNewTeam(view: View) {
        view.findViewById<Button>(R.id.team_validate).setOnClickListener {
            val editText = view.findViewById<EditText>(R.id.team_edit_text).text.toString()
            if (editText.isNotBlank()) {
                if (isOnCreate) {
                    viewModel.createTeam(editText)
                } else {
                    viewModel.joinTeam(requireContext(), editText)
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.team_empty_edit),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Listener pour copie code invitation
     */
    private fun copyInvitationCode(view: View) {
        view.findViewById<ImageView>(R.id.team_invitation_code_copy).setOnClickListener {
            val cm =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(
                ClipData.newPlainText(
                    "text",
                    view.findViewById<TextView>(R.id.team_invitation_code_code).text
                )
            )
            Toast.makeText(context, getString(R.string.copy), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onItemClicked(position: Int, v: View, id: Int) {
        when (id) {
            R.id.player_card -> {
                // Ajout dans préférences
                val sharedPref: SharedPreferences = requireContext().getSharedPreferences(PREF_CURRENT_CHARACTER, PRIVATE_MODE)
                val editor = sharedPref.edit()
                editor.putString(PREF_CURRENT_CHARACTER, MainActivity.viewModel._currentTeamCharacters.value!![position].key)
                editor.apply()

                MainActivity.viewModel._currentCharacter.value =
                    MainActivity.viewModel._currentTeamCharacters.value!![position]

                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "TeamDialog"

        private const val KEY_TITLE = "KEY_TITLE"

        fun newInstance(title: String): TeamDialogFragment {
            val args = Bundle()
            args.putString(KEY_TITLE, title) // ex argument

            val fragment = TeamDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}