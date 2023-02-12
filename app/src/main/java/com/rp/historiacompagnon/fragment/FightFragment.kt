package com.rp.historiacompagnon.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rp.historiacompagnon.MainActivity
import com.rp.historiacompagnon.R
import com.rp.historiacompagnon.adapter.EquipmentAdapter
import com.rp.historiacompagnon.component.EquipmentComponent
import com.rp.historiacompagnon.component.IndicComponent
import com.rp.historiacompagnon.component.PvDiceComponent
import com.rp.historiacompagnon.entity.Character
import com.rp.historiacompagnon.entity.Equipment
import com.rp.historiacompagnon.entity.LifeDice
import com.rp.historiacompagnon.enum.CharacteristicEnum
import com.rp.historiacompagnon.enum.EquipmentTypeEnum
import com.rp.historiacompagnon.util.RecyclerViewEquipmenetClickListener

class FightFragment : Fragment(), RecyclerViewEquipmenetClickListener {
    private var character = Character()
    private var shield: Equipment? = null
    private var armor: Equipment? = null
    private var weapons = ArrayList<Equipment>()
    private var accessories = ArrayList<Equipment>()
    private lateinit var recyclerViewWeapons: RecyclerView
    private lateinit var viewAdapterWeapons: EquipmentAdapter
    private lateinit var viewManagerWeapons: RecyclerView.LayoutManager
    private lateinit var recyclerViewAccessories: RecyclerView
    private lateinit var viewAdapterAccessories: EquipmentAdapter
    private lateinit var viewManagerAccessories: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fight, container, false)

        val pvView = view.findViewById<PvDiceComponent>(R.id.fight_pv)
        setupPvView(pvView)

        val diceView = view.findViewById<PvDiceComponent>(R.id.fight_dice)
        setupDiceView(diceView)

        val initView = view.findViewById<IndicComponent>(R.id.fight_initiative)
        initView.indicTitle.text = getString(R.string.fight_init)
        initView.indicEdit.visibility = View.GONE
        val caView = view.findViewById<IndicComponent>(R.id.fight_ca)
        caView.indicTitle.text = getString(R.string.fight_ca)
        caView.indicEdit.visibility = View.GONE
        val inspiPointView = view.findViewById<IndicComponent>(R.id.fight_inspiration_points)
        inspiPointView.indicTitle.text = getString(R.string.fight_inspiration_points)
        inspiPointView.indicEdit.visibility = View.GONE
        inspiPointView.indicPlus.visibility = View.VISIBLE
        inspiPointView.indicPlus.setOnClickListener {
            character.inspirationPoints++
            MainActivity.viewModel.editCharacter(character)
        }
        inspiPointView.indicMinus.visibility = View.VISIBLE
        inspiPointView.indicMinus.setOnClickListener {
            character.inspirationPoints--
            MainActivity.viewModel.editCharacter(character)
        }
        val gloryPointView = view.findViewById<IndicComponent>(R.id.fight_glory_points)
        gloryPointView.indicTitle.text = getString(R.string.fight_glory_points)
        gloryPointView.indicEdit.visibility = View.GONE
        gloryPointView.indicPlus.visibility = View.VISIBLE
        gloryPointView.indicPlus.setOnClickListener {
            character.gloryPoints++
            MainActivity.viewModel.editCharacter(character)
        }
        gloryPointView.indicMinus.visibility = View.VISIBLE
        gloryPointView.indicMinus.setOnClickListener {
            character.gloryPoints--
            MainActivity.viewModel.editCharacter(character)
        }
        val speedView = view.findViewById<IndicComponent>(R.id.fight_speed)
        speedView.indicTitle.text = getString(R.string.fight_speed)
        speedView.indicEdit.setOnClickListener {
            openSpeedDialog()
        }

        setupWeaponsAdapter(view)
        // TODO mettre tout dans recyclers
        val shieldView = view.findViewById<EquipmentComponent>(R.id.fight_equipment_shield)
        shieldView.equipmentTypeIcon.setImageResource(R.drawable.shield)
        shieldView.equipmentExpend.setOnClickListener {
            if (shieldView.equipmentUnfold.visibility == View.GONE) {
                shieldView.equipmentUnfold.visibility = View.VISIBLE
                shieldView.equipmentExpend.setImageResource(R.drawable.expand_less)
            } else {
                shieldView.equipmentUnfold.visibility = View.GONE
                shieldView.equipmentExpend.setImageResource(R.drawable.expand_more)
            }
        }
        shieldView.equipmentEdit.setOnClickListener {
            openEquipmentDialog(shield!!)
        }


        val armorView = view.findViewById<EquipmentComponent>(R.id.fight_equipment_armor)
        armorView.equipmentTypeIcon.setImageResource(R.drawable.armor)
        armorView.equipmentExpend.setOnClickListener {
            if (armorView.equipmentUnfold.visibility == View.GONE) {
                armorView.equipmentUnfold.visibility = View.VISIBLE
                armorView.equipmentExpend.setImageResource(R.drawable.expand_less)
            } else {
                armorView.equipmentUnfold.visibility = View.GONE
                armorView.equipmentExpend.setImageResource(R.drawable.expand_more)
            }
        }
        armorView.equipmentEdit.setOnClickListener {
            openEquipmentDialog(armor!!)
        }

        setupAccessoriesAdapter(view)

        MainActivity.viewModel.currentCharacter.observe(viewLifecycleOwner, Observer {
            character = it

            shield = character.inventory.equipments
                .find { s -> s.equiped && s.type == EquipmentTypeEnum.SHIELD }
            armor = character.inventory.equipments
                .find { s -> s.equiped && s.type == EquipmentTypeEnum.ARMOR }
            weapons = character.inventory.equipments.filter { w ->
                w.equiped && (w.type == EquipmentTypeEnum.WEAPON
                        || w.type == EquipmentTypeEnum.WEAPON_RANGE
                        || w.type == EquipmentTypeEnum.GUN)
            } as ArrayList<Equipment>
            accessories = character.inventory.equipments.filter { w -> w.equiped && (w.type == EquipmentTypeEnum.OTHER) } as ArrayList<Equipment>

            // Maj PV
            updatePvView(pvView)

            // Maj Dice
            updateDiceView(diceView)

            // Maj Initiative
            val dex = character.characteristics
                .find { c -> c.name == CharacteristicEnum.DEXTERITY }
            if (null != dex) {
                initView.indicValue.text = getString(
                    R.string.fight_init_dice,
                    CharacteristicEnum.getCharacteristicModifier(dex.value)
                )
            }

            // Maj CA
            var ca = 0
            if (null != shield) ca += shield!!.ca
            if (null != armor) ca += armor!!.ca
            caView.indicValue.text = ca.toString()

            // Maj inspiration
            inspiPointView.indicValue.text = character.inspirationPoints.toString()

            // Maj glory
            gloryPointView.indicValue.text = character.gloryPoints.toString()

            // Maj vitesse
            speedView.indicValue.text = character.speed

            // TODO refacto recycler
            if (null != shield) {
                shieldView.equipmentName.text = shield!!.name
                shieldView.equipmentDamageCa.text = getString(R.string.equipment_x_ca, shield!!.ca.toString())
                shieldView.equipmentProperties.text = shield!!.properties
                shieldView.equipmentSpecial.text = shield!!.special
                shieldView.equipmentDescription.text = shield!!.description
            }
            if (null != armor) {
                armorView.equipmentName.text = armor!!.name
                armorView.equipmentDamageCa.text = getString(R.string.equipment_x_ca, armor!!.ca.toString())
                armorView.equipmentProperties.text = armor!!.properties
                armorView.equipmentSpecial.text = armor!!.special
                armorView.equipmentDescription.text = armor!!.description
            }

            updateWeaponDataset()
            updateAccessoriesDataset()
        })

        return view
    }

    private fun openSpeedDialog() {
        val speedDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)

        speedDialog.setContentView(R.layout.dialog_one_field)
        speedDialog.findViewById<TextView>(R.id.dialog_one_edit_title).text =
            getString(R.string.fight_speed)
        speedDialog.findViewById<TextInputLayout>(R.id.dialog_one_edit_field_layout).hint =
            getString(R.string.fight_speed)
        speedDialog.show()

        speedDialog.findViewById<ImageView>(R.id.dialog_one_edit_close)
            .setOnClickListener { speedDialog.dismiss() }

        speedDialog.findViewById<TextInputEditText>(R.id.dialog_one_edit_field_value)
            .setText(character.speed.toString())

        speedDialog.findViewById<Button>(R.id.dialog_one_edit_save_button).setOnClickListener {
            character.speed =
                speedDialog.findViewById<TextInputEditText>(R.id.dialog_one_edit_field_value).text.toString()
            MainActivity.viewModel.editCharacter(character)
            speedDialog.dismiss()
        }
    }

    private fun setupWeaponsAdapter(view: View) {
        viewManagerWeapons = LinearLayoutManager(this.context)
        viewAdapterWeapons = EquipmentAdapter(
            weapons,
            this,
            requireContext()
        )

        recyclerViewWeapons =
            view.findViewById<RecyclerView>(R.id.fight_equipment_weapon_recycler).apply {
                // use a linear layout manager
                layoutManager = viewManagerWeapons

                // specify an viewAdapter (see also next example)
                adapter = viewAdapterWeapons
            }
    }

    private fun setupAccessoriesAdapter(view: View) {
        viewManagerAccessories = LinearLayoutManager(this.context)
        viewAdapterAccessories = EquipmentAdapter(
            accessories,
            this,
            requireContext()
        )

        recyclerViewAccessories =
            view.findViewById<RecyclerView>(R.id.fight_equipment_accessory_recycler).apply {
                // use a linear layout manager
                layoutManager = viewManagerAccessories

                // specify an viewAdapter (see also next example)
                adapter = viewAdapterAccessories
            }
    }

    private fun updateDiceView(diceView: PvDiceComponent) {
        if (character.health.lifeDices.isNotEmpty()) {
            diceView.pvDiceValue.text = character.health.lifeDices[0].number.toString()
            diceView.pvDiceMaxValue.text = character.health.lifeDices[0].type.toString()
        }
    }

    private fun updatePvView(pvView: PvDiceComponent) {
        pvView.pvDiceValue.text = character.health.pv.current.toString()
        pvView.pvDiceMaxValue.text = character.health.pv.max.toString()
        pvView.pvDiceBonus.text = character.health.pv.bonus.toString()
    }

    /**
     * Mise en place encart dés de vie
     * TODO gestion multiclasse
     */
    private fun setupDiceView(diceView: PvDiceComponent) {
        diceView.pvDiceTitle.text = getString(R.string.fight_pv_dice)
        diceView.pvDiceBonus.visibility = View.GONE
        diceView.pvDiceSeparator.text = getString(R.string.fight_pv_d)
        diceView.pvDiceAdd.setImageResource(R.drawable.ic_baseline_exposure_plus_1_24)
        diceView.pvDiceMinus.setImageResource(R.drawable.ic_baseline_exposure_neg_1_24)
        diceView.pvDiceAdd.setOnClickListener {
            if (character.health.lifeDices.isNotEmpty()) {
                character.health.lifeDices[0].number++
                MainActivity.viewModel.editCharacter(character)
            }
        }
        diceView.pvDiceMinus.setOnClickListener {
            if (character.health.lifeDices.isNotEmpty()) {
                character.health.lifeDices[0].number--
                MainActivity.viewModel.editCharacter(character)
            }
        }
        diceView.pvDiceEdit.setOnClickListener {
            openDiceEditDialog()
        }
    }

    private fun openDiceEditDialog() {
        val diceDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)

        diceDialog.setContentView(R.layout.dialog_two_fields)
        diceDialog.findViewById<TextView>(R.id.dialog_two_edit_title).text =
            getString(R.string.fight_edit_pv_dice)
        diceDialog.findViewById<TextInputLayout>(R.id.dialog_two_edit_field_one_layout).hint =
            getString(R.string.fight_nb_dice)
        diceDialog.findViewById<TextInputLayout>(R.id.dialog_two_edit_field_two_layout).hint =
            getString(R.string.fight_type_dice)
        diceDialog.show()

        diceDialog.findViewById<ImageView>(R.id.dialog_two_edit_close)
            .setOnClickListener { diceDialog.dismiss() }

        if (character.health.lifeDices.isNotEmpty()) {
            diceDialog.findViewById<TextInputEditText>(R.id.dialog_two_edit_field_one_value)
                .setText(character.health.lifeDices[0].number.toString())
            diceDialog.findViewById<TextInputEditText>(R.id.dialog_two_edit_field_two_value)
                .setText(character.health.lifeDices[0].type.toString())
        }

        diceDialog.findViewById<Button>(R.id.dialog_two_edit_save_button).setOnClickListener {
            if (character.health.lifeDices.isEmpty()) {
                character.health.lifeDices.add(LifeDice())
            }
            character.health.lifeDices[0].number =
                diceDialog.findViewById<TextInputEditText>(R.id.dialog_two_edit_field_one_value).text.toString()
                    .toInt()
            character.health.lifeDices[0].type =
                diceDialog.findViewById<TextInputEditText>(R.id.dialog_two_edit_field_two_value).text.toString()
                    .toInt()
            MainActivity.viewModel.editCharacter(character)
            diceDialog.dismiss()
        }
    }

    private fun setupPvView(pvView: PvDiceComponent) {
        pvView.pvDiceTitle.text = getString(R.string.fight_pv)
        pvView.pvDiceAdd.setImageResource(R.drawable.ic_baseline_add_24)
        pvView.pvDiceMinus.setImageResource(R.drawable.ic_baseline_remove_24)
        pvView.pvDiceAdd.setOnClickListener {
            openEditCurrentPvDialog(true)
        }
        pvView.pvDiceMinus.setOnClickListener {
            openEditCurrentPvDialog(false)
        }
        pvView.pvDiceEdit.setOnClickListener {
            openEditPvDialog()
        }
        pvView.pvDiceBonus.setOnClickListener {
            // TODO edit bonus
        }
    }

    private fun openEditPvDialog() {
        val pvDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)

        pvDialog.setContentView(R.layout.dialog_two_fields)
        pvDialog.findViewById<TextView>(R.id.dialog_two_edit_title).text =
            getString(R.string.fight_edit_pv)
        pvDialog.findViewById<TextInputLayout>(R.id.dialog_two_edit_field_one_layout).hint =
            getString(R.string.fight_pv)
        pvDialog.findViewById<TextInputLayout>(R.id.dialog_two_edit_field_two_layout).hint =
            getString(R.string.fight_max_pv)
        pvDialog.show()

        pvDialog.findViewById<ImageView>(R.id.dialog_two_edit_close)
            .setOnClickListener { pvDialog.dismiss() }

        pvDialog.findViewById<TextInputEditText>(R.id.dialog_two_edit_field_one_value)
            .setText(character.health.pv.current.toString())
        pvDialog.findViewById<TextInputEditText>(R.id.dialog_two_edit_field_two_value)
            .setText(character.health.pv.max.toString())

        pvDialog.findViewById<Button>(R.id.dialog_two_edit_save_button).setOnClickListener {
            character.health.pv.current =
                pvDialog.findViewById<TextInputEditText>(R.id.dialog_two_edit_field_one_value).text.toString()
                    .toInt()
            character.health.pv.max =
                pvDialog.findViewById<TextInputEditText>(R.id.dialog_two_edit_field_two_value).text.toString()
                    .toInt()
            MainActivity.viewModel.editCharacter(character)
            pvDialog.dismiss()
        }
    }

    private fun openEditCurrentPvDialog(isAddition: Boolean) {
        val currentPvDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)

        currentPvDialog.setContentView(R.layout.dialog_one_field)
        if (isAddition) {
            currentPvDialog.findViewById<TextView>(R.id.dialog_one_edit_title).text =
                getString(R.string.fight_add_pv)
        } else {
            currentPvDialog.findViewById<TextView>(R.id.dialog_one_edit_title).text =
                getString(R.string.fight_minus_pv)
        }
        currentPvDialog.findViewById<TextInputLayout>(R.id.dialog_one_edit_field_layout).hint =
            getString(R.string.fight_pv)
        currentPvDialog.show()

        currentPvDialog.findViewById<ImageView>(R.id.dialog_one_edit_close)
            .setOnClickListener { currentPvDialog.dismiss() }

        currentPvDialog.findViewById<TextInputEditText>(R.id.dialog_one_edit_field_value)
            .setText("0")

        currentPvDialog.findViewById<Button>(R.id.dialog_one_edit_save_button).setOnClickListener {
            if (isAddition) {
                character.health.pv.current += currentPvDialog.findViewById<TextInputEditText>(R.id.dialog_one_edit_field_value).text.toString()
                    .toInt()
            } else {
                character.health.pv.current -= currentPvDialog.findViewById<TextInputEditText>(R.id.dialog_one_edit_field_value).text.toString()
                    .toInt()
            }
            MainActivity.viewModel.editCharacter(character)
            currentPvDialog.dismiss()
        }
    }

    private fun openEquipmentDialog(equipment: Equipment) {
        val equipmentDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)

        equipmentDialog.setContentView(R.layout.dialog_equipment)

        when (equipment.type) {
            EquipmentTypeEnum.SHIELD -> {
                equipmentDialog.findViewById<TextView>(R.id.equipment_edit_title).text =
                    getString(R.string.equipment_edit_shield)
                equipmentDialog.findViewById<LinearLayout>(R.id.equipment_edit_weapon_type_layout).visibility = View.GONE
            }
            EquipmentTypeEnum.ARMOR -> {
                equipmentDialog.findViewById<TextView>(R.id.equipment_edit_title).text =
                    getString(R.string.equipment_edit_armor)
                equipmentDialog.findViewById<LinearLayout>(R.id.equipment_edit_weapon_type_layout).visibility = View.GONE
            }
            EquipmentTypeEnum.OTHER -> {
                equipmentDialog.findViewById<TextView>(R.id.equipment_edit_title).text =
                    getString(R.string.equipment_edit_accessory)
                equipmentDialog.findViewById<LinearLayout>(R.id.equipment_edit_weapon_type_layout).visibility = View.GONE
            }
            else -> {
                // Tous types armes
                equipmentDialog.findViewById<TextView>(R.id.equipment_edit_title).text =
                    getString(R.string.equipment_edit_weapon)
                equipmentDialog.findViewById<LinearLayout>(R.id.equipment_edit_weapon_type_layout).visibility = View.VISIBLE
            }
        }

        equipmentDialog.show()

        equipmentDialog.findViewById<ImageView>(R.id.equipment_edit_close)
            .setOnClickListener { equipmentDialog.dismiss() }

        equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_name_value)
            .setText(equipment.name)

        if (equipment.type == EquipmentTypeEnum.SHIELD || equipment.type == EquipmentTypeEnum.ARMOR ||
            equipment.type == EquipmentTypeEnum.OTHER) {
            equipmentDialog.findViewById<TextInputLayout>(R.id.equipment_edit_damage_layout)
                .visibility = View.GONE
            equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_ca_value)
                .setText(equipment.ca.toString())
        } else {
            equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_damage_value)
                .setText(equipment.damage)
            equipmentDialog.findViewById<TextInputLayout>(R.id.equipment_edit_ca_layout)
                .visibility = View.GONE
            if (equipment.type == EquipmentTypeEnum.WEAPON) equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_cac).isChecked = true
            if (equipment.type == EquipmentTypeEnum.WEAPON_RANGE) equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_range).isChecked = true
            if (equipment.type == EquipmentTypeEnum.GUN) equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_gun).isChecked = true
        }

        equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_properties_value)
            .setText(equipment.properties)
        equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_special_value)
            .setText(equipment.special)
        equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_description_value)
            .setText(equipment.description)

        equipmentDialog.findViewById<Button>(R.id.equipment_edit_save_button).setOnClickListener {
            var characterEquipment = character.inventory.equipments.find { e -> e == equipment }

            equipment.name = equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_name_value).text.toString()
            if (equipment.type == EquipmentTypeEnum.SHIELD || equipment.type == EquipmentTypeEnum.ARMOR ||
                equipment.type == EquipmentTypeEnum.OTHER) {
                equipment.ca =
                    equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_ca_value).text.toString()
                        .toInt()
            } else {
                equipment.damage = equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_damage_value).text.toString()
                if (equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_cac).isChecked) {
                    equipment.type = EquipmentTypeEnum.WEAPON
                }
                if (equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_range).isChecked) {
                    equipment.type = EquipmentTypeEnum.WEAPON_RANGE
                }
                if (equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_gun).isChecked) {
                    equipment.type = EquipmentTypeEnum.GUN
                }
            }
            equipment.properties = equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_properties_value).text.toString()
            equipment.special = equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_special_value).text.toString()
            equipment.description = equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_description_value).text.toString()

            equipment.equiped = true

            if ( null == characterEquipment) {
                character.inventory.equipments.add(equipment)
            } else {
                characterEquipment = equipment
            }

            MainActivity.viewModel.editCharacter(character)
            equipmentDialog.dismiss()
        }
    }

    private fun updateWeaponDataset() {
        viewAdapterWeapons.mDataset = weapons
        viewAdapterWeapons.notifyDataSetChanged()
    }

    private fun updateAccessoriesDataset() {
        viewAdapterAccessories.mDataset = accessories
        viewAdapterAccessories.notifyDataSetChanged()
    }

    override fun onItemClicked(equipment: Equipment, id: Int) {
        when (id) {
           /* R.id.equipment_delete ->*/ // TODO
            R.id.equipment_edit -> openEquipmentDialog(equipment)
        }
    }
}