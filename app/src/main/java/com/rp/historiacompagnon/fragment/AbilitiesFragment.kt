package com.rp.historiacompagnon.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.rp.historiacompagnon.MainActivity
import com.rp.historiacompagnon.Preferences
import com.rp.historiacompagnon.Preferences.PRIVATE_MODE
import com.rp.historiacompagnon.R
import com.rp.historiacompagnon.Services
import com.rp.historiacompagnon.adapter.AptitudeAdapter
import com.rp.historiacompagnon.component.JobComponent
import com.rp.historiacompagnon.entity.Aptitude
import com.rp.historiacompagnon.entity.Character
import com.rp.historiacompagnon.entity.Job
import com.rp.historiacompagnon.enum.AptitudeTagEnum
import com.rp.historiacompagnon.enum.AptitudeTypeEnum
import com.rp.historiacompagnon.util.RecyclerViewClickListener

class AbilitiesFragment : Fragment(), RecyclerViewClickListener {
    private var character = Character()
    private var aptitudes = ArrayList<Aptitude>()
    private var setupDone = false
    private lateinit var recyclerViewAptitudes: RecyclerView
    private lateinit var viewAdapterAptitudes: AptitudeAdapter
    private lateinit var viewManagerAptitudes: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_abilities, container, false)

        setupAdapter(view)
        setupSortByType(view)

        view.findViewById<Button>(R.id.abilities_job_add).setOnClickListener {
            character.jobs.add(Job())
            MainActivity.viewModel.editCharacter(character)
        }

        view.findViewById<Button>(R.id.abilities_aptitude_add).setOnClickListener {
            openAptitudeDialog(-1)
        }

        MainActivity.viewModel.currentCharacter.observe(viewLifecycleOwner, Observer {
            character = it
            aptitudes = ArrayList(it.aptitudes)

            if (!setupDone) {
                setupFilterByTag(view)
                setupDone = true
            }

            // Infos 1er job
            if (character.jobs.size >= 1) {
                fillJob(view, R.id.abilities_job1, 0)
            } else {
                view.findViewById<JobComponent>(R.id.abilities_job1).visibility = View.GONE
            }

            // Infos 2nd job
            if (character.jobs.size == 2) {
                fillJob(view, R.id.abilities_job2, 1)
                // Cacher ajout si déjà 2 jobs
                view.findViewById<Button>(R.id.abilities_job_add).visibility = View.GONE
            } else {
                view.findViewById<Button>(R.id.abilities_job_add).visibility = View.VISIBLE
                view.findViewById<JobComponent>(R.id.abilities_job2).visibility = View.GONE
            }

            sortAptitudeByType()
        })


        return view
    }

    private fun openAptitudeDialog(position: Int): Dialog {
        val aptitudeDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)

        aptitudeDialog.setContentView(R.layout.dialog_abilities_aptitude)

        val typeEditText =
            aptitudeDialog.findViewById<AutoCompleteTextView>(R.id.abilities_aptitude_edit_type_text)
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            AptitudeTypeEnum.getListStringAptitudeType()
        )
        typeEditText.setAdapter(adapter)

        aptitudeDialog.show()
        aptitudeDialog.findViewById<ImageView>(R.id.abilities_aptitude_edit_close)
            .setOnClickListener { aptitudeDialog.dismiss() }

        aptitudeDialog.findViewById<Button>(R.id.abilities_aptitude_edit_save_button)
            .setOnClickListener {
                val newAptitude = Aptitude()
                newAptitude.name =
                    aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_name_value).text.toString()
                newAptitude.shortDescription =
                    aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_short_description_value).text.toString()
                newAptitude.type =
                    AptitudeTypeEnum.getTypeByValue(typeEditText.text.toString())
                newAptitude.damage =
                    aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_damage_value).text.toString()
                newAptitude.heal =
                    aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_heal_value).text.toString()
                newAptitude.scope =
                    aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_scope_value).text.toString()
                newAptitude.duration =
                    aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_duration_value).text.toString()
                newAptitude.use =
                    aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_use_value).text.toString()
                newAptitude.effect =
                    aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_effect_value).text.toString()
                newAptitude.tag = getTagListChecked(aptitudeDialog)

                if (position == -1) {
                    character.aptitudes.add(newAptitude)
                } else {
                    character.aptitudes[position] = newAptitude
                }

                Services.editCharacter(character, MainActivity.viewModel.user.value!!.key)

                aptitudeDialog.dismiss()
            }
        return aptitudeDialog
    }

    private fun getPositionCharacterAptitude(position: Int) =
        character.aptitudes.indexOf(aptitudes[position])

    /**
     * Récupère la liste des tag check
     */
    private fun getTagListChecked(newAptitudeDialog: Dialog): ArrayList<AptitudeTagEnum> {
        val tagList = ArrayList<AptitudeTagEnum>()
        if (newAptitudeDialog.findViewById<CheckBox>(R.id.abilities_aptitude_tag_fight).isChecked) {
            tagList.add(AptitudeTagEnum.FIGHT)
        }
        if (newAptitudeDialog.findViewById<CheckBox>(R.id.abilities_aptitude_tag_utility).isChecked) {
            tagList.add(AptitudeTagEnum.UTILITY)
        }
        if (newAptitudeDialog.findViewById<CheckBox>(R.id.abilities_aptitude_tag_heal).isChecked) {
            tagList.add(AptitudeTagEnum.HEAL)
        }
        if (newAptitudeDialog.findViewById<CheckBox>(R.id.abilities_aptitude_tag_out_fight).isChecked) {
            tagList.add(AptitudeTagEnum.OUT_FIGHT)
        }
        return tagList
    }

    private fun setupAdapter(view: View) {
        viewManagerAptitudes = LinearLayoutManager(this.context)
        viewAdapterAptitudes = AptitudeAdapter(
            aptitudes,
            this,
            requireContext()
        )

        recyclerViewAptitudes = view.findViewById<RecyclerView>(R.id.abilities_recycler).apply {
            // use a linear layout manager
            layoutManager = viewManagerAptitudes

            // specify an viewAdapter (see also next example)
            adapter = viewAdapterAptitudes
        }
    }

    /**
     * Gestion clicks aptitudes
     */
    override fun onItemClicked(position: Int, v: View, id: Int) {
        when (id) {
            R.id.aptitude_type_edit -> {
                val aptitudeDialog = openAptitudeDialog(getPositionCharacterAptitude(position))
                fillAptitudeDialog(aptitudeDialog, getPositionCharacterAptitude(position))
            }
            R.id.aptitude_type_delete -> {
                val builder = AlertDialog.Builder(context)
                with(builder)
                {
                    setTitle(getString(R.string.delete_aptitude))
                    setMessage(
                        getString(
                            R.string.delete_aptitude_txt,
                            character.aptitudes[position].name
                        )
                    )
                    setNegativeButton(getString(R.string.cancel)) { dialog, which -> dialog.cancel() }
                    setPositiveButton(getString(R.string.yes)) { dialog, which ->
                        character.aptitudes.removeAt(getPositionCharacterAptitude(position))
                        MainActivity.viewModel.editCharacter(character)
                        dialog.cancel()
                    }
                    show()
                }
            }
            R.id.aptitude_add_used -> {
                character.aptitudes[getPositionCharacterAptitude(position)].used ++
                MainActivity.viewModel.editCharacter(character)
            }
            R.id.aptitude_minus_used -> {
                character.aptitudes[getPositionCharacterAptitude(position)].used --
                MainActivity.viewModel.editCharacter(character)
            }
            R.id.aptitude_reset_used -> {
                character.aptitudes[getPositionCharacterAptitude(position)].used = 0
                MainActivity.viewModel.editCharacter(character)
            }
        }
    }

    /**
     * Rempli la dialog avec les infos de l'aptitude à modifier
     */
    private fun fillAptitudeDialog(aptitudeDialog: Dialog, position: Int) {
        aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_name_value)
            .setText(character.aptitudes[position].name)
        aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_short_description_value)
            .setText(character.aptitudes[position].shortDescription)
        when (character.aptitudes[position].type) {
            AptitudeTypeEnum.ACTION -> aptitudeDialog.findViewById<AutoCompleteTextView>(R.id.abilities_aptitude_edit_type_text)
                .setText(AptitudeTypeEnum.ACTION.value, false)
            AptitudeTypeEnum.BONUS_ACTION -> aptitudeDialog.findViewById<AutoCompleteTextView>(R.id.abilities_aptitude_edit_type_text)
                .setText(AptitudeTypeEnum.BONUS_ACTION.value, false)
            AptitudeTypeEnum.REACTION -> aptitudeDialog.findViewById<AutoCompleteTextView>(R.id.abilities_aptitude_edit_type_text)
                .setText(AptitudeTypeEnum.REACTION.value, false)
            AptitudeTypeEnum.PASSIVE -> aptitudeDialog.findViewById<AutoCompleteTextView>(R.id.abilities_aptitude_edit_type_text)
                .setText(AptitudeTypeEnum.PASSIVE.value, false)
            AptitudeTypeEnum.AVANTAGE -> aptitudeDialog.findViewById<AutoCompleteTextView>(R.id.abilities_aptitude_edit_type_text)
                .setText(AptitudeTypeEnum.AVANTAGE.value, false)
            AptitudeTypeEnum.DESAVANTAGE -> aptitudeDialog.findViewById<AutoCompleteTextView>(R.id.abilities_aptitude_edit_type_text)
                .setText(AptitudeTypeEnum.DESAVANTAGE.value, false)
            AptitudeTypeEnum.SPELL -> aptitudeDialog.findViewById<AutoCompleteTextView>(R.id.abilities_aptitude_edit_type_text)
                .setText(AptitudeTypeEnum.SPELL.value, false)
            AptitudeTypeEnum.OTHER -> aptitudeDialog.findViewById<AutoCompleteTextView>(R.id.abilities_aptitude_edit_type_text)
                .setText(AptitudeTypeEnum.OTHER.value, false)
        }
        aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_damage_value)
            .setText(character.aptitudes[position].damage)
        aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_heal_value)
            .setText(character.aptitudes[position].heal)
        aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_scope_value)
            .setText(character.aptitudes[position].scope)
        aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_duration_value)
            .setText(character.aptitudes[position].duration)
        aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_use_value)
            .setText(character.aptitudes[position].use)
        aptitudeDialog.findViewById<EditText>(R.id.abilities_aptitude_edit_effect_value)
            .setText(character.aptitudes[position].effect)
        if (character.aptitudes[position].tag.contains(AptitudeTagEnum.FIGHT)) {
            aptitudeDialog.findViewById<CheckBox>(R.id.abilities_aptitude_tag_fight).isChecked =
                true
        }
        if (character.aptitudes[position].tag.contains(AptitudeTagEnum.UTILITY)) {
            aptitudeDialog.findViewById<CheckBox>(R.id.abilities_aptitude_tag_utility).isChecked =
                true
        }
        if (character.aptitudes[position].tag.contains(AptitudeTagEnum.HEAL)) {
            aptitudeDialog.findViewById<CheckBox>(R.id.abilities_aptitude_tag_heal).isChecked = true
        }
        if (character.aptitudes[position].tag.contains(AptitudeTagEnum.OUT_FIGHT)) {
            aptitudeDialog.findViewById<CheckBox>(R.id.abilities_aptitude_tag_out_fight).isChecked =
                true
        }
    }

    private fun setupFilterByTag(view: View) {
        val chipFight = view.findViewById<Chip>(R.id.abilities_aptitude_chip_fight)
        chipFight.text = AptitudeTagEnum.FIGHT.value
        if (getPrefValueFilterFight()) {
            chipFight.isChecked = true
            filterByType(true, AptitudeTagEnum.FIGHT, view)
        }
        chipFight.setOnCheckedChangeListener { chip, isChecked ->
            filterByType(isChecked, AptitudeTagEnum.FIGHT, view)
            updatePrefFilterFight(isChecked)
        }

        val chipUtility = view.findViewById<Chip>(R.id.abilities_aptitude_chip_utility)
        chipUtility.text = AptitudeTagEnum.UTILITY.value
        if (getPrefValueFilterUtility()) {
            chipUtility.isChecked = true
            filterByType(true, AptitudeTagEnum.UTILITY, view)
        }
        chipUtility.setOnCheckedChangeListener { chip, isChecked ->
            filterByType(isChecked, AptitudeTagEnum.UTILITY, view)
            updatePrefFilterUtility(isChecked)
        }

        val chipHeal = view.findViewById<Chip>(R.id.abilities_aptitude_chip_heal)
        chipHeal.text = AptitudeTagEnum.HEAL.value
        if (getPrefValueFilterHeal()) {
            chipHeal.isChecked = true
            filterByType(true, AptitudeTagEnum.HEAL, view)
        }
        chipHeal.setOnCheckedChangeListener { chip, isChecked ->
            filterByType(isChecked, AptitudeTagEnum.HEAL, view)
            updatePrefFilterHeal(isChecked)
        }

        val chipOutFight = view.findViewById<Chip>(R.id.abilities_aptitude_chip_out_fight)
        chipOutFight.text = AptitudeTagEnum.OUT_FIGHT.value
        if (getPrefValueFilterOutFight()) {
            chipOutFight.isChecked = true
            filterByType(true, AptitudeTagEnum.OUT_FIGHT, view)
        }
        chipOutFight.setOnCheckedChangeListener { chip, isChecked ->
            filterByType(isChecked, AptitudeTagEnum.OUT_FIGHT, view)
            updatePrefFilterOutFight(isChecked)
        }
    }

    private fun filterByType(isChecked: Boolean, type: AptitudeTagEnum, view: View) {
        val aptitudesByType =
            ArrayList(character.aptitudes.filter { aptitude -> aptitude.tag.contains(type) })
        if (isChecked) {
            if (chipsSelectedCount(view) > 1) { // on ajoute
                for (aptitude in aptitudesByType) {
                    if (!aptitudes.contains(aptitude)) {
                        aptitudes.add(aptitude)
                    }
                }
            } else { // on remplace
                aptitudes = aptitudesByType
            }
        } else {
            if (chipsSelectedCount(view) > 0) { // on retire mais on vérifie appartenance autre coché
                for (aptitude in aptitudesByType) {
                    var remove = true
                    if (aptitude.tag.contains(AptitudeTagEnum.FIGHT)
                        && view.findViewById<Chip>(R.id.abilities_aptitude_chip_fight).isChecked) {
                        remove = false
                    } else if (aptitude.tag.contains(AptitudeTagEnum.UTILITY)
                        && view.findViewById<Chip>(R.id.abilities_aptitude_chip_utility).isChecked) {
                        remove = false
                    } else if (aptitude.tag.contains(AptitudeTagEnum.HEAL)
                        && view.findViewById<Chip>(R.id.abilities_aptitude_chip_heal).isChecked) {
                        remove = false
                    } else if (aptitude.tag.contains(AptitudeTagEnum.OUT_FIGHT)
                        && view.findViewById<Chip>(R.id.abilities_aptitude_chip_out_fight).isChecked) {
                        remove = false
                    }

                    if (remove) {
                        aptitudes.remove(aptitude)
                    }
                }
            } else { // on remet tout
                aptitudes = character.aptitudes
            }
        }
        sortAptitudeByType()
        updateAptitudeDataset()
    }

    private fun chipsSelectedCount(view: View): Int {
        var count = 0

        if (view.findViewById<Chip>(R.id.abilities_aptitude_chip_fight).isChecked) count ++
        if (view.findViewById<Chip>(R.id.abilities_aptitude_chip_utility).isChecked) count ++
        if (view.findViewById<Chip>(R.id.abilities_aptitude_chip_heal).isChecked) count ++
        if (view.findViewById<Chip>(R.id.abilities_aptitude_chip_out_fight).isChecked) count ++

        return count
    }

    private fun setupSortByType(view: View) {
        var spinner = view.findViewById<Spinner>(R.id.abilities_aptitude_sort_spinner)
        val filters = AptitudeTypeEnum.getListStringAptitudeTypeForFilter(requireContext())
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
                sortAptitudeByType()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // write code to perform some action
            }
        }

        spinner.setSelection(filters.indexOf(getPrefValueSortType()))
    }

    /**
     * Récupère la préférence filtre type
     */
    private fun getPrefValueSortType() = requireContext().getSharedPreferences(
        Preferences.PREF_APTITUDES_SORT_TYPE, PRIVATE_MODE
    ).getString(Preferences.PREF_APTITUDES_SORT_TYPE, AptitudeTypeEnum.ACTION.value)

    private fun getPrefValueFilterFight() = requireContext().getSharedPreferences(
        Preferences.PREF_APTITUDES_FILTER_FIGHT, PRIVATE_MODE
    ).getBoolean(Preferences.PREF_APTITUDES_FILTER_FIGHT, false)

    private fun getPrefValueFilterUtility() = requireContext().getSharedPreferences(
        Preferences.PREF_APTITUDES_FILTER_UTILITY, PRIVATE_MODE
    ).getBoolean(Preferences.PREF_APTITUDES_FILTER_UTILITY, false)

    private fun getPrefValueFilterHeal() = requireContext().getSharedPreferences(
        Preferences.PREF_APTITUDES_FILTER_HEAL, PRIVATE_MODE
    ).getBoolean(Preferences.PREF_APTITUDES_FILTER_HEAL, false)

    private fun getPrefValueFilterOutFight() = requireContext().getSharedPreferences(
        Preferences.PREF_APTITUDES_FILTER_OUT_FIGHT, PRIVATE_MODE
    ).getBoolean(Preferences.PREF_APTITUDES_FILTER_OUT_FIGHT, false)

    /**
     * Tri la liste des aptitudes par type
     */
    private fun sortAptitudeByType() {
        val actions = aptitudes.filter { a -> a.type == AptitudeTypeEnum.ACTION }
        val bonusActions = aptitudes.filter { a -> a.type == AptitudeTypeEnum.BONUS_ACTION }
        val reaction = aptitudes.filter { a -> a.type == AptitudeTypeEnum.REACTION }
        val passive = aptitudes.filter { a -> a.type == AptitudeTypeEnum.PASSIVE }
        val avantage = aptitudes.filter { a -> a.type == AptitudeTypeEnum.AVANTAGE }
        val desavantage = aptitudes.filter { a -> a.type == AptitudeTypeEnum.DESAVANTAGE }
        val spell = aptitudes.filter { a -> a.type == AptitudeTypeEnum.SPELL }

        val sort = ArrayList<Aptitude>()

        val first = getString(R.string.first)

        when (getPrefValueSortType()) {
            AptitudeTypeEnum.ACTION.value + first -> {
                sort.addAll(actions)
                sort.addAll(bonusActions)
                sort.addAll(reaction)
                sort.addAll(passive)
                sort.addAll(avantage)
                sort.addAll(desavantage)
                sort.addAll(spell)
            }
            AptitudeTypeEnum.BONUS_ACTION.value + first -> {
                sort.addAll(bonusActions)
                sort.addAll(actions)
                sort.addAll(reaction)
                sort.addAll(passive)
                sort.addAll(avantage)
                sort.addAll(desavantage)
                sort.addAll(spell)
            }
            AptitudeTypeEnum.REACTION.value + first -> {
                sort.addAll(reaction)
                sort.addAll(actions)
                sort.addAll(bonusActions)
                sort.addAll(passive)
                sort.addAll(avantage)
                sort.addAll(desavantage)
                sort.addAll(spell)
            }
            AptitudeTypeEnum.PASSIVE.value + first -> {
                sort.addAll(passive)
                sort.addAll(actions)
                sort.addAll(bonusActions)
                sort.addAll(reaction)
                sort.addAll(avantage)
                sort.addAll(desavantage)
                sort.addAll(spell)
            }
            AptitudeTypeEnum.AVANTAGE.value + first -> {
                sort.addAll(avantage)
                sort.addAll(actions)
                sort.addAll(bonusActions)
                sort.addAll(reaction)
                sort.addAll(passive)
                sort.addAll(desavantage)
                sort.addAll(spell)
            }
            AptitudeTypeEnum.DESAVANTAGE.value + first -> {
                sort.addAll(desavantage)
                sort.addAll(actions)
                sort.addAll(bonusActions)
                sort.addAll(reaction)
                sort.addAll(passive)
                sort.addAll(avantage)
                sort.addAll(spell)
            }
            AptitudeTypeEnum.SPELL.value + first -> {
                sort.addAll(spell)
                sort.addAll(actions)
                sort.addAll(bonusActions)
                sort.addAll(reaction)
                sort.addAll(passive)
                sort.addAll(avantage)
                sort.addAll(desavantage)
            }
        }

        aptitudes = sort
        updateAptitudeDataset()
    }

    private fun updateAptitudeDataset() {
        viewAdapterAptitudes.mDataset = aptitudes
        viewAdapterAptitudes.notifyDataSetChanged()
    }

    /**
     * Met à jour la préférence de filtre aptitudes
     */
    fun updatePrefSortType(type: String) {
        val editor =
            requireContext().getSharedPreferences(
                Preferences.PREF_APTITUDES_SORT_TYPE,
                PRIVATE_MODE
            ).edit()
        editor.putString(Preferences.PREF_APTITUDES_SORT_TYPE, type)
        editor.apply()
    }

    fun updatePrefFilterFight(check: Boolean) {
        val editor =
            requireContext().getSharedPreferences(
                Preferences.PREF_APTITUDES_FILTER_FIGHT,
                PRIVATE_MODE
            ).edit()
        editor.putBoolean(Preferences.PREF_APTITUDES_FILTER_FIGHT, check)
        editor.apply()
    }

    fun updatePrefFilterUtility(check: Boolean) {
        val editor =
            requireContext().getSharedPreferences(
                Preferences.PREF_APTITUDES_FILTER_UTILITY,
                PRIVATE_MODE
            ).edit()
        editor.putBoolean(Preferences.PREF_APTITUDES_FILTER_UTILITY, check)
        editor.apply()
    }

    fun updatePrefFilterHeal(check: Boolean) {
        val editor =
            requireContext().getSharedPreferences(
                Preferences.PREF_APTITUDES_FILTER_HEAL,
                PRIVATE_MODE
            ).edit()
        editor.putBoolean(Preferences.PREF_APTITUDES_FILTER_HEAL, check)
        editor.apply()
    }

    fun updatePrefFilterOutFight(check: Boolean) {
        val editor =
            requireContext().getSharedPreferences(
                Preferences.PREF_APTITUDES_FILTER_OUT_FIGHT,
                PRIVATE_MODE
            ).edit()
        editor.putBoolean(Preferences.PREF_APTITUDES_FILTER_OUT_FIGHT, check)
        editor.apply()
    }

    /**
     * Gestion encart métier
     */
    private fun fillJob(view: View, componentId: Int, jobPos: Int) {
        val job = character.jobs[jobPos]
        val component = view.findViewById<JobComponent>(componentId)

        component.visibility = View.VISIBLE
        component.jobName.text = job.name
        component.jobSpecialityLvl.text =
            getString(R.string.abilities_specialty_lvl, job.specialty, job.level)
        component.jobModifier.text = getString(R.string.abilities_plus_something, job.modifier)
        component.jobLifeDiceLvl.text = job.lifeDiceByLvl
        component.jobArmors.text = job.typeArmor
        component.jobWeapons.text = job.typeWeapon
        component.jobThrowSave.text = job.save

        component.jobEdit.setOnClickListener {
            openJobDialog(jobPos)
        }

        if (jobPos == 1) { // 2ieme job
            component.jobDelete.visibility = View.VISIBLE
            component.jobDelete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                with(builder)
                {
                    setTitle(getString(R.string.delete_job))
                    setMessage(
                        getString(
                            R.string.delete_job_txt,
                            character.jobs[jobPos].name
                        )
                    )
                    setNegativeButton(getString(R.string.cancel)) { dialog, which -> dialog.cancel() }
                    setPositiveButton(getString(R.string.yes)) { dialog, which ->
                        character.jobs.removeAt(jobPos)
                        MainActivity.viewModel.editCharacter(character)
                        dialog.cancel()
                    }
                    show()
                }
            }
        }
    }

    /**
     * Ouvre la dialog pour le métier
     */
    private fun openJobDialog(jobPos: Int) {
        val joDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)

        joDialog.setContentView(R.layout.dialog_abilities_job)
        joDialog.show()
        joDialog.findViewById<ImageView>(R.id.abilities_job_edit_close)
            .setOnClickListener { joDialog.dismiss() }

        fillJobDialog(joDialog, jobPos)

        joDialog.findViewById<Button>(R.id.abilities_job_edit_save_button).setOnClickListener {
            character.jobs[jobPos].name =
                joDialog.findViewById<EditText>(R.id.abilities_job_edit_name_value).text.toString()
            character.jobs[jobPos].specialty =
                joDialog.findViewById<EditText>(R.id.abilities_job_edit_specialty_value).text.toString()
            character.jobs[jobPos].level =
                joDialog.findViewById<EditText>(R.id.abilities_job_edit_level_value).text.toString()
                    .toInt()
            character.jobs[jobPos].modifier =
                joDialog.findViewById<EditText>(R.id.abilities_job_edit_modifier_value).text.toString()
                    .toInt()
            character.jobs[jobPos].lifeDiceByLvl =
                joDialog.findViewById<EditText>(R.id.abilities_job_edit_dice_lvl_value).text.toString()
            character.jobs[jobPos].typeArmor =
                joDialog.findViewById<EditText>(R.id.abilities_job_edit_armors_value).text.toString()
            character.jobs[jobPos].typeWeapon =
                joDialog.findViewById<EditText>(R.id.abilities_job_edit_weapons_value).text.toString()
            character.jobs[jobPos].save =
                joDialog.findViewById<EditText>(R.id.abilities_job_edit_throw_save_value).text.toString()

            MainActivity.viewModel.editCharacter(character)
            joDialog.dismiss()
        }
    }

    /**
     * Rempli la dialog avec les infos du job
     */
    private fun fillJobDialog(joDialog: Dialog, jobPos: Int) {
        joDialog.findViewById<EditText>(R.id.abilities_job_edit_name_value)
            .setText(character.jobs[jobPos].name)
        joDialog.findViewById<EditText>(R.id.abilities_job_edit_specialty_value)
            .setText(character.jobs[jobPos].specialty)
        joDialog.findViewById<EditText>(R.id.abilities_job_edit_level_value)
            .setText(character.jobs[jobPos].level.toString())
        joDialog.findViewById<EditText>(R.id.abilities_job_edit_modifier_value)
            .setText(character.jobs[jobPos].modifier.toString())
        joDialog.findViewById<EditText>(R.id.abilities_job_edit_dice_lvl_value)
            .setText(character.jobs[jobPos].lifeDiceByLvl)
        joDialog.findViewById<EditText>(R.id.abilities_job_edit_armors_value)
            .setText(character.jobs[jobPos].typeArmor)
        joDialog.findViewById<EditText>(R.id.abilities_job_edit_weapons_value)
            .setText(character.jobs[jobPos].typeWeapon)
        joDialog.findViewById<EditText>(R.id.abilities_job_edit_throw_save_value)
            .setText(character.jobs[jobPos].save)
    }
}