package com.rp.historiacompagnon.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rp.historiacompagnon.MainActivity
import com.rp.historiacompagnon.Preferences
import com.rp.historiacompagnon.R
import com.rp.historiacompagnon.adapter.PnjAdapter
import com.rp.historiacompagnon.entity.Aptitude
import com.rp.historiacompagnon.entity.Character
import com.rp.historiacompagnon.entity.Pnj
import com.rp.historiacompagnon.enum.AptitudeTagEnum
import com.rp.historiacompagnon.enum.AptitudeTypeEnum
import com.rp.historiacompagnon.enum.RelationshipTypeEnum
import com.rp.historiacompagnon.util.RecyclerViewClickListener

class RelationshipFragment : Fragment(), RecyclerViewClickListener {
    private var character = Character()
    private var pnjs = ArrayList<Pnj>()

    private lateinit var recyclerViewPnj: RecyclerView
    private lateinit var viewAdapterPnj: PnjAdapter
    private lateinit var viewManagerPnjs: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_relashionship, container, false)

        setupAdapter(view)
        setupSortByType(view)

        view.findViewById<Button>(R.id.relashionship_pnj_add).setOnClickListener {
            openPnjDialog(-1)
        }

        MainActivity.viewModel.currentCharacter.observe(viewLifecycleOwner, Observer {
            character = it
            pnjs = ArrayList(it.relationships)

            sortPnjByType()
        })

        return view
    }

    private fun setupAdapter(view: View) {
        viewManagerPnjs = LinearLayoutManager(this.context)
        viewAdapterPnj = PnjAdapter(
            pnjs,
            this,
            requireContext()
        )

        recyclerViewPnj = view.findViewById<RecyclerView>(R.id.relashionship_pnj_recycler).apply {
            // use a linear layout manager
            layoutManager = viewManagerPnjs

            // specify an viewAdapter (see also next example)
            adapter = viewAdapterPnj
        }
    }

    private fun setupSortByType(view: View) {
        var spinner = view.findViewById<Spinner>(R.id.relashionship_pnj_sort_spinner)
        val filters = RelationshipTypeEnum.getListStringRelashionshipTypeForFilter(requireContext())
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, filters)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updatePrefSortType(filters[position])
                sortPnjByType()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // write code to perform some action
            }
        }

        spinner.setSelection(filters.indexOf(getPrefValueSortType()))
    }

    /**
     * Tri la liste des pnj par type
     */
    private fun sortPnjByType() {
        val lover = pnjs.filter { a -> a.relationshipType == RelationshipTypeEnum.LOVER }
        val amical = pnjs.filter { a -> a.relationshipType == RelationshipTypeEnum.AMICAL }
        val family = pnjs.filter { a -> a.relationshipType == RelationshipTypeEnum.FAMILY }
        val neutral = pnjs.filter { a -> a.relationshipType == RelationshipTypeEnum.NEUTRAL }
        val rivals = pnjs.filter { a -> a.relationshipType == RelationshipTypeEnum.RIVAL }
        val opponents = pnjs.filter { a -> a.relationshipType == RelationshipTypeEnum.OPPONENT }

        val sort = ArrayList<Pnj>()

        val first = getString(R.string.first)

        when (getPrefValueSortType()) {
            RelationshipTypeEnum.LOVER.value + first -> {
                sort.addAll(lover)
                sort.addAll(amical)
                sort.addAll(family)
                sort.addAll(neutral)
                sort.addAll(rivals)
                sort.addAll(opponents)
            }
            RelationshipTypeEnum.AMICAL.value + first -> {
                sort.addAll(amical)
                sort.addAll(lover)
                sort.addAll(family)
                sort.addAll(neutral)
                sort.addAll(rivals)
                sort.addAll(opponents)
            }
            RelationshipTypeEnum.FAMILY.value + first -> {
                sort.addAll(family)
                sort.addAll(lover)
                sort.addAll(amical)
                sort.addAll(neutral)
                sort.addAll(rivals)
                sort.addAll(opponents)
            }
            RelationshipTypeEnum.NEUTRAL.value + first -> {
                sort.addAll(neutral)
                sort.addAll(lover)
                sort.addAll(amical)
                sort.addAll(family)
                sort.addAll(rivals)
                sort.addAll(opponents)
            }
            RelationshipTypeEnum.RIVAL.value + first -> {
                sort.addAll(rivals)
                sort.addAll(lover)
                sort.addAll(amical)
                sort.addAll(family)
                sort.addAll(neutral)
                sort.addAll(opponents)
            }
            RelationshipTypeEnum.OPPONENT.value + first -> {
                sort.addAll(opponents)
                sort.addAll(lover)
                sort.addAll(amical)
                sort.addAll(family)
                sort.addAll(neutral)
                sort.addAll(rivals)
            }
            else -> {
                sort.addAll(lover)
                sort.addAll(amical)
                sort.addAll(family)
                sort.addAll(neutral)
                sort.addAll(rivals)
                sort.addAll(opponents)
            }
        }

        pnjs = sort
        updatePnjDataset()
    }

    private fun updatePnjDataset() {
        viewAdapterPnj.mDataset = pnjs
        viewAdapterPnj.notifyDataSetChanged()
    }

    /**
     * Met à jour la préférence de filtre pnj
     */
    fun updatePrefSortType(type: String) {
        val editor =
            requireContext().getSharedPreferences(
                Preferences.PREF_PNJ_SORT_TYPE,
                Preferences.PRIVATE_MODE
            ).edit()
        editor.putString(Preferences.PREF_PNJ_SORT_TYPE, type)
        editor.apply()
    }

    /**
     * Récupère la préférence filtre type
     */
    private fun getPrefValueSortType() = requireContext().getSharedPreferences(
        Preferences.PREF_PNJ_SORT_TYPE, Preferences.PRIVATE_MODE
    ).getString(Preferences.PREF_PNJ_SORT_TYPE, AptitudeTypeEnum.ACTION.value)

    private fun openPnjDialog(position: Int): Dialog {
        val pnjDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)
        pnjDialog.setContentView(R.layout.dialog_pnj)

        val typeEditText =
            pnjDialog.findViewById<AutoCompleteTextView>(R.id.pnj_edit_relashionship_type_text)
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            RelationshipTypeEnum.getListStringRelashionshipType()
        )
        typeEditText.setAdapter(adapter)

        pnjDialog.show()
        pnjDialog.findViewById<ImageView>(R.id.pnj_edit_close)
            .setOnClickListener { pnjDialog.dismiss() }

        pnjDialog.findViewById<Button>(R.id.pnj_edit_save_button)
            .setOnClickListener {
                val newPnj = Pnj()
                newPnj.name =
                    pnjDialog.findViewById<EditText>(R.id.pnj_edit_name_value).text.toString()
                newPnj.species =
                    pnjDialog.findViewById<EditText>(R.id.pnj_edit_species_value).text.toString()
                newPnj.relationshipType =
                    RelationshipTypeEnum.getTypeByValue(typeEditText.text.toString())
                newPnj.summary =
                    pnjDialog.findViewById<EditText>(R.id.pnj_edit_short_description_value).text.toString()
                newPnj.description =
                    pnjDialog.findViewById<EditText>(R.id.pnj_edit_description_value).text.toString()

                if (position == -1) {
                    character.relationships.add(newPnj)
                } else {
                    character.relationships[position] = newPnj
                }

                MainActivity.viewModel.editCharacter(character)

                pnjDialog.dismiss()
            }
        return pnjDialog
    }

    /**
     * Rempli la dialog avec les infos de la relation pnj à modifier
     */
    private fun fillPnjDialog(pnjDialog: Dialog, position: Int) {
        pnjDialog.findViewById<EditText>(R.id.pnj_edit_name_value)
            .setText(character.relationships[position].name)
        pnjDialog.findViewById<EditText>(R.id.pnj_edit_species_value)
            .setText(character.relationships[position].species)
        pnjDialog.findViewById<EditText>(R.id.pnj_edit_short_description_value)
            .setText(character.relationships[position].summary)
        pnjDialog.findViewById<EditText>(R.id.pnj_edit_description_value)
            .setText(character.relationships[position].description)
        when (character.relationships[position].relationshipType) {
            RelationshipTypeEnum.LOVER -> pnjDialog.findViewById<AutoCompleteTextView>(R.id.pnj_edit_relashionship_type_text)
                .setText(RelationshipTypeEnum.LOVER.value, false)
            RelationshipTypeEnum.AMICAL -> pnjDialog.findViewById<AutoCompleteTextView>(R.id.pnj_edit_relashionship_type_text)
                .setText(RelationshipTypeEnum.AMICAL.value, false)
            RelationshipTypeEnum.FAMILY -> pnjDialog.findViewById<AutoCompleteTextView>(R.id.pnj_edit_relashionship_type_text)
                .setText(RelationshipTypeEnum.FAMILY.value, false)
            RelationshipTypeEnum.RIVAL -> pnjDialog.findViewById<AutoCompleteTextView>(R.id.pnj_edit_relashionship_type_text)
                .setText(RelationshipTypeEnum.RIVAL.value, false)
            RelationshipTypeEnum.OPPONENT -> pnjDialog.findViewById<AutoCompleteTextView>(R.id.pnj_edit_relashionship_type_text)
                .setText(RelationshipTypeEnum.OPPONENT.value, false)
            else -> pnjDialog.findViewById<AutoCompleteTextView>(R.id.pnj_edit_relashionship_type_text)
                .setText(RelationshipTypeEnum.NEUTRAL.value, false)
        }
    }

    override fun onItemClicked(position: Int, v: View, id: Int) {
        when (id) {
            R.id.pnj_edit -> {
                val pnjDialog = openPnjDialog(getPositionCharacterPnj(position))
                fillPnjDialog(pnjDialog, getPositionCharacterPnj(position))
            }
            R.id.pnj_delete -> {
                val builder = AlertDialog.Builder(context)
                with(builder)
                {
                    setTitle(getString(R.string.delete_pnj))
                    setMessage(
                        getString(
                            R.string.delete_pnj_txt,
                            character.relationships[position].name
                        )
                    )
                    setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
                    setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                        character.relationships.removeAt(getPositionCharacterPnj(position))
                        MainActivity.viewModel.editCharacter(character)
                        dialog.cancel()
                    }
                    show()
                }
            }
        }
    }

    private fun getPositionCharacterPnj(position: Int) =
        character.relationships.indexOf(pnjs[position])
}