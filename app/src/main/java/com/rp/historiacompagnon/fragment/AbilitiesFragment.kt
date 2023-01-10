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
import com.google.android.material.chip.Chip
import com.rp.historiacompagnon.MainActivity
import com.rp.historiacompagnon.Preferences
import com.rp.historiacompagnon.Preferences.PRIVATE_MODE
import com.rp.historiacompagnon.R
import com.rp.historiacompagnon.Services
import com.rp.historiacompagnon.adapter.AptitudeAdapter
import com.rp.historiacompagnon.entity.Aptitude
import com.rp.historiacompagnon.entity.Character
import com.rp.historiacompagnon.entity.Characteristic
import com.rp.historiacompagnon.entity.Skill
import com.rp.historiacompagnon.enum.AptitudeTagEnum
import com.rp.historiacompagnon.enum.AptitudeTypeEnum
import com.rp.historiacompagnon.enum.CharacteristicEnum
import com.rp.historiacompagnon.enum.SkillNameEnum
import com.rp.historiacompagnon.util.RecyclerViewClickListener

class AbilitiesFragment : Fragment(), RecyclerViewClickListener {
    private var character = Character()
    private var aptitudes = ArrayList<Aptitude>()
    private var setupDone = false
    private var strengthVal: Int? = 0
    private var dexVal: Int? = 0
    private var constVal: Int? = 0
    private var charismaVal: Int? = 0
    private var wisdomVal: Int? = 0
    private var intVal: Int? = 0
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

        view.findViewById<Button>(R.id.abilities_aptitude_add).setOnClickListener {
            openAptitudeDialog(-1)
        }

        view.findViewById<ImageView>(R.id.abilities_characteristic_edit).setOnClickListener {
            openCharacteristicDialog()
        }

        view.findViewById<ImageView>(R.id.abilities_skill_edit).setOnClickListener {
            openSkillDialog()
        }

        MainActivity.viewModel.currentCharacter.observe(viewLifecycleOwner, Observer {
            character = it
            aptitudes = ArrayList(it.aptitudes)

            if (!setupDone) {
                setupFilterByTag(view)
                setupDone = true
            }

            sortAptitudeByType()

            setCharacteristics(view)
            setSkillsModifiers(view)
        })

        return view
    }

    private fun openSkillDialog() {
        val skillDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)

        skillDialog.setContentView(R.layout.dialog_abilities_skills)
        skillDialog.show()
        skillDialog.findViewById<ImageView>(R.id.skill_edit_close)
            .setOnClickListener { skillDialog.dismiss() }

        fillSkillDialog(skillDialog)

        skillDialog.findViewById<Button>(R.id.skill_edit_save_button).setOnClickListener {

            setSkillCharacter(SkillNameEnum.ACROBATICS, R.id.skill_acrobatics_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.ARCANA, R.id.skill_arcana_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.ATHLETICS, R.id.skill_athletics_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.DISCRETION, R.id.skill_discretion_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.DRESSAGE, R.id.skill_dressage_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.SNEAKING, R.id.skill_sneaking_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.HISTORY, R.id.skill_history_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.INTIMIDATION, R.id.skill_intimidation_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.INVESTIGATION, R.id.skill_investigation_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.MEDICINE, R.id.skill_medecine_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.NATURE, R.id.skill_nature_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.PERCEPTION, R.id.skill_perception_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.INSIGHT, R.id.skill_insight_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.PERSUASION, R.id.skill_persuasion_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.RELIGION, R.id.skill_religion_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.REPRESENTATION, R.id.skill_representation_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.TRICKERY, R.id.skill_trickery_checkbox, skillDialog)
            setSkillCharacter(SkillNameEnum.SURVIVAL, R.id.skill_survival_checkbox, skillDialog)

            MainActivity.viewModel.editCharacter(character)
            skillDialog.dismiss()
        }
    }

    private fun fillSkillDialog(skillDialog: Dialog) {
        if (null != getSkill(SkillNameEnum.ACROBATICS) && getSkill(SkillNameEnum.ACROBATICS)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_acrobatics_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.ARCANA) && getSkill(SkillNameEnum.ARCANA)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_arcana_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.ATHLETICS) && getSkill(SkillNameEnum.ATHLETICS)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_athletics_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.DISCRETION) && getSkill(SkillNameEnum.DISCRETION)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_discretion_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.DRESSAGE) && getSkill(SkillNameEnum.DRESSAGE)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_dressage_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.SNEAKING) && getSkill(SkillNameEnum.SNEAKING)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_sneaking_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.HISTORY) && getSkill(SkillNameEnum.HISTORY)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_history_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.INTIMIDATION) && getSkill(SkillNameEnum.INTIMIDATION)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_intimidation_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.INVESTIGATION) && getSkill(SkillNameEnum.INVESTIGATION)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_investigation_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.MEDICINE) && getSkill(SkillNameEnum.MEDICINE)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_medecine_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.NATURE) && getSkill(SkillNameEnum.NATURE)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_nature_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.PERCEPTION) && getSkill(SkillNameEnum.PERCEPTION)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_perception_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.INSIGHT) && getSkill(SkillNameEnum.INSIGHT)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_insight_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.PERSUASION) && getSkill(SkillNameEnum.PERSUASION)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_persuasion_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.RELIGION) && getSkill(SkillNameEnum.RELIGION)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_religion_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.REPRESENTATION) && getSkill(SkillNameEnum.REPRESENTATION)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_representation_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.TRICKERY) && getSkill(SkillNameEnum.TRICKERY)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_trickery_checkbox).isChecked = true
        }
        if (null != getSkill(SkillNameEnum.SURVIVAL) && getSkill(SkillNameEnum.SURVIVAL)!!.mastery) {
            skillDialog.findViewById<CheckBox>(R.id.skill_survival_checkbox).isChecked = true
        }
    }

    private fun setSkillCharacter(skill: SkillNameEnum, viewId: Int, dialog: Dialog) {
        var characterSkill = getSkill(skill)
        if (null == characterSkill) {
            character.skills.add(
                Skill(
                    skill,
                    dialog.findViewById<CheckBox>(viewId).isChecked
                )
            )
        } else {
            characterSkill.mastery = dialog.findViewById<CheckBox>(viewId).isChecked
        }
    }

    private fun openCharacteristicDialog() {
        val characDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)

        characDialog.setContentView(R.layout.dialog_abilities_characteristics)
        characDialog.show()
        characDialog.findViewById<ImageView>(R.id.characteristic_edit_close)
            .setOnClickListener { characDialog.dismiss() }

        fillCharacteristicDialog(characDialog)

        characDialog.findViewById<Button>(R.id.characteristic_edit_save_button).setOnClickListener {

            setCharacteristicCharacter(CharacteristicEnum.STRENGTH, R.id.characteristic_edit_strength_value, characDialog)
            setCharacteristicCharacter(CharacteristicEnum.DEXTERITY, R.id.characteristic_edit_dex_value, characDialog)
            setCharacteristicCharacter(CharacteristicEnum.CONSTITUTION, R.id.characteristic_edit_const_value, characDialog)
            setCharacteristicCharacter(CharacteristicEnum.CHARISMA, R.id.characteristic_edit_charisma_value, characDialog)
            setCharacteristicCharacter(CharacteristicEnum.WISDOM, R.id.characteristic_edit_wisdom_value, characDialog)
            setCharacteristicCharacter(CharacteristicEnum.INTELLECT, R.id.characteristic_edit_int_value, characDialog)

            MainActivity.viewModel.editCharacter(character)
            characDialog.dismiss()
        }
    }

    private fun fillCharacteristicDialog(characDialog: Dialog) {
        if (null != getCharacteristic(CharacteristicEnum.STRENGTH)) {
            characDialog.findViewById<EditText>(R.id.characteristic_edit_strength_value)
                .setText(getCharacteristic(CharacteristicEnum.STRENGTH)!!.value.toString())
        }
        if (null != getCharacteristic(CharacteristicEnum.DEXTERITY)) {
            characDialog.findViewById<EditText>(R.id.characteristic_edit_dex_value)
                .setText(getCharacteristic(CharacteristicEnum.DEXTERITY)!!.value.toString())
        }
        if (null != getCharacteristic(CharacteristicEnum.CONSTITUTION)) {
            characDialog.findViewById<EditText>(R.id.characteristic_edit_const_value)
                .setText(getCharacteristic(CharacteristicEnum.CONSTITUTION)!!.value.toString())
        }
        if (null != getCharacteristic(CharacteristicEnum.CHARISMA)) {
            characDialog.findViewById<EditText>(R.id.characteristic_edit_charisma_value)
                .setText(getCharacteristic(CharacteristicEnum.CHARISMA)!!.value.toString())
        }
        if (null != getCharacteristic(CharacteristicEnum.WISDOM)) {
            characDialog.findViewById<EditText>(R.id.characteristic_edit_wisdom_value)
                .setText(getCharacteristic(CharacteristicEnum.WISDOM)!!.value.toString())
        }
        if (null != getCharacteristic(CharacteristicEnum.INTELLECT)) {
            characDialog.findViewById<EditText>(R.id.characteristic_edit_int_value)
                .setText(getCharacteristic(CharacteristicEnum.INTELLECT)!!.value.toString())
        }
    }

    private fun getCharacteristic(characteristic: CharacteristicEnum): Characteristic? {
        return character.characteristics.find { c -> c.name == characteristic }
    }

    private fun setCharacteristicCharacter(characteristic: CharacteristicEnum, viewId: Int, dialog: Dialog) {
        var characterCharac = getCharacteristic(characteristic)
        if (null == characterCharac) {
            character.characteristics.add(
                Characteristic(
                    characteristic,
                    dialog.findViewById<EditText>(viewId).text.toString().toInt()
                )
            )
        } else {
            characterCharac.value = dialog.findViewById<EditText>(viewId).text.toString().toInt()
        }
    }

    private fun setSkillsModifiers(view: View) {
        // TODO calcul selon job pour maitrisés
        getSkillModifier(view, SkillNameEnum.ACROBATICS, R.id.abilities_acrobatics_modifier, R.id.abilities_acrobatics_title)
        getSkillModifier(view, SkillNameEnum.ARCANA, R.id.abilities_arcana_modifier, R.id.abilities_arcana_title)
        getSkillModifier(view, SkillNameEnum.ATHLETICS, R.id.abilities_athletics_modifier, R.id.abilities_athletics_title)
        getSkillModifier(view, SkillNameEnum.DISCRETION, R.id.abilities_discretion_modifier, R.id.abilities_discretion_title)
        getSkillModifier(view, SkillNameEnum.DRESSAGE, R.id.abilities_dressage_modifier, R.id.abilities_dressage_title)
        getSkillModifier(view, SkillNameEnum.SNEAKING, R.id.abilities_sneaking_modifier, R.id.abilities_sneaking_title)
        getSkillModifier(view, SkillNameEnum.HISTORY, R.id.abilities_history_modifier, R.id.abilities_history_title)
        getSkillModifier(view, SkillNameEnum.INTIMIDATION, R.id.abilities_intimidation_modifier, R.id.abilities_intimidation_title)
        getSkillModifier(view, SkillNameEnum.INVESTIGATION, R.id.abilities_investigation_modifier, R.id.abilities_investigation_title)
        getSkillModifier(view, SkillNameEnum.MEDICINE, R.id.abilities_medecine_modifier, R.id.abilities_medecine_title)
        getSkillModifier(view, SkillNameEnum.NATURE, R.id.abilities_nature_modifier, R.id.abilities_nature_title)
        getSkillModifier(view, SkillNameEnum.PERCEPTION, R.id.abilities_perception_modifier, R.id.abilities_perception_title)
        getSkillModifier(view, SkillNameEnum.INSIGHT, R.id.abilities_insight_modifier, R.id.abilities_insight_title)
        getSkillModifier(view, SkillNameEnum.PERSUASION, R.id.abilities_persuasion_modifier, R.id.abilities_persuasion_title)
        getSkillModifier(view, SkillNameEnum.RELIGION, R.id.abilities_religion_modifier, R.id.abilities_religion_title)
        getSkillModifier(view, SkillNameEnum.REPRESENTATION, R.id.abilities_representation_modifier, R.id.abilities_representation_title)
        getSkillModifier(view, SkillNameEnum.TRICKERY, R.id.abilities_trickery_modifier, R.id.abilities_trickery_title)
        getSkillModifier(view, SkillNameEnum.SURVIVAL, R.id.abilities_survival_modifier, R.id.abilities_survival_title)
    }

    private fun getSkill(typeSkill: SkillNameEnum) = character.skills.find { s -> s.name == typeSkill }

    private fun getSkillModifier(view: View, typeSkill: SkillNameEnum, idViewCheckbox: Int, idViewTxt: Int) {
        val skill = getSkill(typeSkill)
        var modifier = ""
        if (null != skill) {
            if (skill.mastery) {
                var bonusJob = 0
                if (character.jobs.isNotEmpty()) bonusJob = character.jobs[0].modifier
                modifier = getString(
                    R.string.job_plus_something,
                    getCaracteristicModifier(skill).toInt() + bonusJob
                )
                view.findViewById<TextView>(idViewTxt).setTextColor(resources.getColor(R.color.bonus))
            } else {
                modifier = getCaracteristicModifier(skill)
                view.findViewById<TextView>(idViewTxt).setTextColor(resources.getColor(R.color.color_txt))
            }
        } else {
            view.findViewById<TextView>(idViewTxt).setTextColor(resources.getColor(R.color.color_txt))
        }
        view.findViewById<TextView>(idViewCheckbox).text = modifier
    }

    private fun getCaracteristicModifier(acrobaticsSkill: Skill): String {
        var modifier = ""
        when (acrobaticsSkill.name.attribute) {
            CharacteristicEnum.STRENGTH -> {
                modifier = if (null != strengthVal) {
                    CharacteristicEnum.getCharacteristicModifier(strengthVal!!)
                } else {
                    ""
                }
            }
            CharacteristicEnum.DEXTERITY -> {
                modifier = if (null != dexVal) {
                    CharacteristicEnum.getCharacteristicModifier(dexVal!!)
                } else {
                    ""
                }
            }

            CharacteristicEnum.CONSTITUTION -> {
                modifier = if (null != constVal) {
                    CharacteristicEnum.getCharacteristicModifier(constVal!!)
                } else {
                    ""
                }
            }
            CharacteristicEnum.CHARISMA -> {
                modifier = if (null != charismaVal) {
                    CharacteristicEnum.getCharacteristicModifier(charismaVal!!)
                } else {
                    ""
                }
            }
            CharacteristicEnum.WISDOM -> {
                modifier = if (null != wisdomVal) {
                    CharacteristicEnum.getCharacteristicModifier(wisdomVal!!)
                } else {
                    ""
                }
            }
            CharacteristicEnum.INTELLECT -> {
                modifier = if (null != intVal) {
                    CharacteristicEnum.getCharacteristicModifier(intVal!!)
                } else {
                    ""
                }
            }
        }
        return modifier
    }

    private fun setCharacteristics(view: View) {
        strengthVal = getCharacteristic(CharacteristicEnum.STRENGTH)?.value
        if (null == strengthVal) {
            view.findViewById<TextView>(R.id.abilities_strength_value).text = ""
            view.findViewById<TextView>(R.id.abilities_strength_modifier).text = ""
        } else {
            view.findViewById<TextView>(R.id.abilities_strength_value).text = strengthVal.toString()
            view.findViewById<TextView>(R.id.abilities_strength_modifier).text =
                CharacteristicEnum.getCharacteristicModifier(strengthVal!!)
        }


        dexVal = getCharacteristic(CharacteristicEnum.DEXTERITY)?.value
        if (null == dexVal) {
            view.findViewById<TextView>(R.id.abilities_dexterity_value).text = ""
            view.findViewById<TextView>(R.id.abilities_dexterity_modifier).text = ""
        } else {
            view.findViewById<TextView>(R.id.abilities_dexterity_value).text = dexVal.toString()
            view.findViewById<TextView>(R.id.abilities_dexterity_modifier).text =
                CharacteristicEnum.getCharacteristicModifier(dexVal!!)
        }

        constVal = getCharacteristic(CharacteristicEnum.CONSTITUTION)?.value
        if (null == constVal) {
            view.findViewById<TextView>(R.id.abilities_constitution_value).text = ""
            view.findViewById<TextView>(R.id.abilities_constitution_modifier).text = ""
        } else {
            view.findViewById<TextView>(R.id.abilities_constitution_value).text = constVal.toString()
            view.findViewById<TextView>(R.id.abilities_constitution_modifier).text =
                CharacteristicEnum.getCharacteristicModifier(constVal!!)
        }

        charismaVal = getCharacteristic(CharacteristicEnum.CHARISMA)?.value
        if (null == charismaVal) {
            view.findViewById<TextView>(R.id.abilities_charisma_value).text = ""
            view.findViewById<TextView>(R.id.abilities_charisma_modifier).text = ""
        } else {
            view.findViewById<TextView>(R.id.abilities_charisma_value).text = charismaVal.toString()
            view.findViewById<TextView>(R.id.abilities_charisma_modifier).text =
                CharacteristicEnum.getCharacteristicModifier(charismaVal!!)
        }

        wisdomVal = getCharacteristic(CharacteristicEnum.WISDOM)?.value
        if (null == wisdomVal) {
            view.findViewById<TextView>(R.id.abilities_wisdom_value).text = ""
            view.findViewById<TextView>(R.id.abilities_wisdom_modifier).text = ""
        } else {
            view.findViewById<TextView>(R.id.abilities_wisdom_value).text = wisdomVal.toString()
            view.findViewById<TextView>(R.id.abilities_wisdom_modifier).text =
                CharacteristicEnum.getCharacteristicModifier(wisdomVal!!)
        }

        intVal = getCharacteristic(CharacteristicEnum.INTELLECT)?.value
        if (null == intVal) {
            view.findViewById<TextView>(R.id.abilities_intellect_value).text = ""
            view.findViewById<TextView>(R.id.abilities_intellect_modifier).text = ""
        } else {
            view.findViewById<TextView>(R.id.abilities_intellect_value).text = intVal.toString()
            view.findViewById<TextView>(R.id.abilities_intellect_modifier).text =
                CharacteristicEnum.getCharacteristicModifier(intVal!!)
        }
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
                    setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
                    setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                        character.aptitudes.removeAt(getPositionCharacterAptitude(position))
                        MainActivity.viewModel.editCharacter(character)
                        dialog.cancel()
                    }
                    show()
                }
            }
            R.id.aptitude_add_used -> {
                character.aptitudes[getPositionCharacterAptitude(position)].used++
                MainActivity.viewModel.editCharacter(character)
            }
            R.id.aptitude_minus_used -> {
                character.aptitudes[getPositionCharacterAptitude(position)].used--
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
        chipFight.setOnCheckedChangeListener { _, isChecked ->
            filterByType(isChecked, AptitudeTagEnum.FIGHT, view)
            updatePrefFilterFight(isChecked)
        }

        val chipUtility = view.findViewById<Chip>(R.id.abilities_aptitude_chip_utility)
        chipUtility.text = AptitudeTagEnum.UTILITY.value
        if (getPrefValueFilterUtility()) {
            chipUtility.isChecked = true
            filterByType(true, AptitudeTagEnum.UTILITY, view)
        }
        chipUtility.setOnCheckedChangeListener { _, isChecked ->
            filterByType(isChecked, AptitudeTagEnum.UTILITY, view)
            updatePrefFilterUtility(isChecked)
        }

        val chipHeal = view.findViewById<Chip>(R.id.abilities_aptitude_chip_heal)
        chipHeal.text = AptitudeTagEnum.HEAL.value
        if (getPrefValueFilterHeal()) {
            chipHeal.isChecked = true
            filterByType(true, AptitudeTagEnum.HEAL, view)
        }
        chipHeal.setOnCheckedChangeListener { _, isChecked ->
            filterByType(isChecked, AptitudeTagEnum.HEAL, view)
            updatePrefFilterHeal(isChecked)
        }

        val chipOutFight = view.findViewById<Chip>(R.id.abilities_aptitude_chip_out_fight)
        chipOutFight.text = AptitudeTagEnum.OUT_FIGHT.value
        if (getPrefValueFilterOutFight()) {
            chipOutFight.isChecked = true
            filterByType(true, AptitudeTagEnum.OUT_FIGHT, view)
        }
        chipOutFight.setOnCheckedChangeListener { _, isChecked ->
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
                        && view.findViewById<Chip>(R.id.abilities_aptitude_chip_fight).isChecked
                    ) {
                        remove = false
                    } else if (aptitude.tag.contains(AptitudeTagEnum.UTILITY)
                        && view.findViewById<Chip>(R.id.abilities_aptitude_chip_utility).isChecked
                    ) {
                        remove = false
                    } else if (aptitude.tag.contains(AptitudeTagEnum.HEAL)
                        && view.findViewById<Chip>(R.id.abilities_aptitude_chip_heal).isChecked
                    ) {
                        remove = false
                    } else if (aptitude.tag.contains(AptitudeTagEnum.OUT_FIGHT)
                        && view.findViewById<Chip>(R.id.abilities_aptitude_chip_out_fight).isChecked
                    ) {
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

        if (view.findViewById<Chip>(R.id.abilities_aptitude_chip_fight).isChecked) count++
        if (view.findViewById<Chip>(R.id.abilities_aptitude_chip_utility).isChecked) count++
        if (view.findViewById<Chip>(R.id.abilities_aptitude_chip_heal).isChecked) count++
        if (view.findViewById<Chip>(R.id.abilities_aptitude_chip_out_fight).isChecked) count++

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
}