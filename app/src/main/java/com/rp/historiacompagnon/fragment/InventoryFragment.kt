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
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rp.historiacompagnon.MainActivity
import com.rp.historiacompagnon.Preferences
import com.rp.historiacompagnon.R
import com.rp.historiacompagnon.adapter.EquipmentAdapter
import com.rp.historiacompagnon.adapter.ItemAdapter
import com.rp.historiacompagnon.entity.*
import com.rp.historiacompagnon.enum.AptitudeTypeEnum
import com.rp.historiacompagnon.enum.EquipmentTypeEnum
import com.rp.historiacompagnon.enum.ItemTypeEnum
import com.rp.historiacompagnon.enum.RelationshipTypeEnum
import com.rp.historiacompagnon.util.RecyclerViewEquipmentClickListener
import com.rp.historiacompagnon.util.RecyclerViewItemClickListener

// TODO voir si/comment ajout argent
class InventoryFragment : Fragment(), RecyclerViewItemClickListener, RecyclerViewEquipmentClickListener {
    private var character = Character()
    private var equipments = ArrayList<Equipment>()
    private var items = ArrayList<Item>()

    private lateinit var recyclerViewItem: RecyclerView
    private lateinit var viewAdapterItem: ItemAdapter
    private lateinit var viewManagerItems: RecyclerView.LayoutManager

    private lateinit var recyclerViewEquipment: RecyclerView
    private lateinit var viewAdapterEquipment: EquipmentAdapter
    private lateinit var viewManagerEquipments: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inventory, container, false)

        setupAdapters(view)
        setupSortItemByType(view)
        setupSortEquipmentByType(view)

        view.findViewById<Button>(R.id.inventory_item_add).setOnClickListener {
            openItemDialog(-1)
        }
        view.findViewById<Button>(R.id.inventory_equipment_add).setOnClickListener {
            openEquipmentDialog(-1)
        }

        MainActivity.viewModel.currentCharacter.observe(viewLifecycleOwner, Observer {
            character = it
            equipments = character.inventory.equipments.filter { e -> !e.equiped } as ArrayList<Equipment>
            items = character.inventory.items.filter { i -> !i.equiped } as ArrayList<Item>

            sortItemByType()
            sortEquipmentByType()
        })

        return view
    }

    private fun setupAdapters(view: View) {
        setupItemAdapter(view)

        setupEquipmentAdapter(view)
    }

    private fun setupEquipmentAdapter(view: View) {
        viewManagerEquipments = LinearLayoutManager(this.context)
        viewAdapterEquipment = EquipmentAdapter(
            equipments,
            this,
            requireContext()
        )

        recyclerViewEquipment =
            view.findViewById<RecyclerView>(R.id.inventory_equipment_recycler).apply {
                layoutManager = viewManagerEquipments
                adapter = viewAdapterEquipment
            }
    }

    private fun setupItemAdapter(view: View) {
        viewManagerItems = LinearLayoutManager(this.context)
        viewAdapterItem = ItemAdapter(
            items,
            this
        )

        recyclerViewItem = view.findViewById<RecyclerView>(R.id.inventory_item_recycler).apply {
            layoutManager = viewManagerItems
            adapter = viewAdapterItem
        }
    }

    /**
     * Mise en place tri pour item
     */
    private fun setupSortItemByType(view: View) {
        var spinner = view.findViewById<Spinner>(R.id.inventory_item_sort_spinner)
        val filters = ItemTypeEnum.getListStringItemForFilter(requireContext())
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
                updatePrefItemSortType(filters[position])
                sortItemByType()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // write code to perform some action
            }
        }

        spinner.setSelection(filters.indexOf(getPrefValueItemSortType()))
    }

    /**
     * Tri la liste des items par type
     */
    private fun sortItemByType() {
        val quest = items.filter { i -> i.type == ItemTypeEnum.QUEST }
        val consumable = items.filter { i -> i.type == ItemTypeEnum.CONSUMABLE }
        val potion = items.filter { i -> i.type == ItemTypeEnum.POTION }
        val scroll = items.filter { i -> i.type == ItemTypeEnum.SCROLL }
        val tool = items.filter { i -> i.type == ItemTypeEnum.TOOL }
        val item = items.filter { i -> i.type == ItemTypeEnum.ITEM }

        val sort = ArrayList<Item>()

        val first = getString(R.string.first)

        when (getPrefValueItemSortType()) {
            ItemTypeEnum.QUEST.value + first -> {
                sort.addAll(quest)
                sort.addAll(consumable)
                sort.addAll(potion)
                sort.addAll(scroll)
                sort.addAll(tool)
                sort.addAll(item)
            }
            ItemTypeEnum.CONSUMABLE.value + first -> {
                sort.addAll(consumable)
                sort.addAll(quest)
                sort.addAll(potion)
                sort.addAll(scroll)
                sort.addAll(tool)
                sort.addAll(item)
            }
            ItemTypeEnum.POTION.value + first -> {
                sort.addAll(potion)
                sort.addAll(quest)
                sort.addAll(consumable)
                sort.addAll(scroll)
                sort.addAll(tool)
                sort.addAll(item)
            }
            ItemTypeEnum.SCROLL.value + first -> {
                sort.addAll(scroll)
                sort.addAll(quest)
                sort.addAll(consumable)
                sort.addAll(potion)
                sort.addAll(tool)
                sort.addAll(item)
            }
            ItemTypeEnum.TOOL.value + first -> {
                sort.addAll(tool)
                sort.addAll(quest)
                sort.addAll(consumable)
                sort.addAll(potion)
                sort.addAll(scroll)
                sort.addAll(item)
            }
            ItemTypeEnum.ITEM.value + first -> {
                sort.addAll(item)
                sort.addAll(quest)
                sort.addAll(consumable)
                sort.addAll(potion)
                sort.addAll(scroll)
                sort.addAll(tool)
            }
            else -> {
                sort.addAll(quest)
                sort.addAll(consumable)
                sort.addAll(potion)
                sort.addAll(scroll)
                sort.addAll(tool)
                sort.addAll(item)
            }
        }

        items = sort
        updateItemDataset()
    }

    private fun updateItemDataset() {
        viewAdapterItem.mDataset = items
        viewAdapterItem.notifyDataSetChanged()
    }

    /**
     * Récupère la préférence filtre item
     */
    private fun getPrefValueItemSortType() = requireContext().getSharedPreferences(
        Preferences.PREF_ITEM_SORT_TYPE, Preferences.PRIVATE_MODE
    ).getString(Preferences.PREF_ITEM_SORT_TYPE, ItemTypeEnum.QUEST.value)

    /**
     * Met à jour la préférence de filtre item
     */
    fun updatePrefItemSortType(type: String) {
        val editor =
            requireContext().getSharedPreferences(
                Preferences.PREF_ITEM_SORT_TYPE,
                Preferences.PRIVATE_MODE
            ).edit()
        editor.putString(Preferences.PREF_ITEM_SORT_TYPE, type)
        editor.apply()
    }

    /**
     * Mise en place tri pour equipment
     */
    private fun setupSortEquipmentByType(view: View) {
        var spinner = view.findViewById<Spinner>(R.id.inventory_equipment_sort_spinner)
        val filters = EquipmentTypeEnum.getListStringEquipmentForFilter(requireContext())
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
                updatePrefEquipmentSortType(filters[position])
                sortEquipmentByType()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // write code to perform some action
            }
        }

        spinner.setSelection(filters.indexOf(getPrefValueItemSortType()))
    }

    /**
     * Tri la liste des equipment par type
     */
    private fun sortEquipmentByType() {
        val weapon = equipments.filter { e -> e.type == EquipmentTypeEnum.WEAPON }
        val weaponRange = equipments.filter { e -> e.type == EquipmentTypeEnum.WEAPON_RANGE }
        val gun = equipments.filter { e -> e.type == EquipmentTypeEnum.GUN }
        val shield = equipments.filter { e -> e.type == EquipmentTypeEnum.SHIELD }
        val armor = equipments.filter { e -> e.type == EquipmentTypeEnum.ARMOR }
        val other = equipments.filter { e -> e.type == EquipmentTypeEnum.OTHER }

        val sort = ArrayList<Equipment>()

        val first = getString(R.string.first)

        when (getPrefValueEquipmentSortType()) {
            EquipmentTypeEnum.WEAPON.value + first -> {
                sort.addAll(weapon)
                sort.addAll(weaponRange)
                sort.addAll(gun)
                sort.addAll(shield)
                sort.addAll(armor)
                sort.addAll(other)
            }
            EquipmentTypeEnum.WEAPON_RANGE.value + first -> {
                sort.addAll(weaponRange)
                sort.addAll(weapon)
                sort.addAll(gun)
                sort.addAll(shield)
                sort.addAll(armor)
                sort.addAll(other)
            }
            EquipmentTypeEnum.GUN.value + first -> {
                sort.addAll(gun)
                sort.addAll(weapon)
                sort.addAll(weaponRange)
                sort.addAll(shield)
                sort.addAll(armor)
                sort.addAll(other)
            }
            EquipmentTypeEnum.SHIELD.value + first -> {
                sort.addAll(shield)
                sort.addAll(weapon)
                sort.addAll(weaponRange)
                sort.addAll(gun)
                sort.addAll(armor)
                sort.addAll(other)
            }
            EquipmentTypeEnum.ARMOR.value + first -> {
                sort.addAll(armor)
                sort.addAll(weapon)
                sort.addAll(weaponRange)
                sort.addAll(gun)
                sort.addAll(shield)
                sort.addAll(other)
            }
            EquipmentTypeEnum.OTHER.value + first -> {
                sort.addAll(other)
                sort.addAll(weapon)
                sort.addAll(weaponRange)
                sort.addAll(gun)
                sort.addAll(shield)
                sort.addAll(armor)
            }
            else -> {
                sort.addAll(weapon)
                sort.addAll(weaponRange)
                sort.addAll(gun)
                sort.addAll(shield)
                sort.addAll(armor)
                sort.addAll(other)
            }
        }

        equipments = sort
        updateEquipmentDataset()
    }

    private fun updateEquipmentDataset() {
        viewAdapterEquipment.mDataset = equipments
        viewAdapterEquipment.notifyDataSetChanged()
    }

    /**
     * Récupère la préférence filtre equipment
     */
    private fun getPrefValueEquipmentSortType() = requireContext().getSharedPreferences(
        Preferences.PREF_EQUIPMENT_SORT_TYPE, Preferences.PRIVATE_MODE
    ).getString(Preferences.PREF_EQUIPMENT_SORT_TYPE, ItemTypeEnum.QUEST.value)

    /**
     * Met à jour la préférence de filtre equipment
     */
    fun updatePrefEquipmentSortType(type: String) {
        val editor =
            requireContext().getSharedPreferences(
                Preferences.PREF_EQUIPMENT_SORT_TYPE,
                Preferences.PRIVATE_MODE
            ).edit()
        editor.putString(Preferences.PREF_EQUIPMENT_SORT_TYPE, type)
        editor.apply()
    }

    private fun openItemDialog(position: Int): Dialog {
        val itemDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)
        itemDialog.setContentView(R.layout.dialog_item)

        val typeEditText =
            itemDialog.findViewById<AutoCompleteTextView>(R.id.item_edit_type_text)
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.list_item,
            ItemTypeEnum.getListStringItemType()
        )
        typeEditText.setAdapter(adapter)

        itemDialog.show()
        itemDialog.findViewById<ImageView>(R.id.item_edit_close)
            .setOnClickListener { itemDialog.dismiss() }

        itemDialog.findViewById<Button>(R.id.item_edit_save_button)
            .setOnClickListener {
                val newItem = Item()
                newItem.name =
                    itemDialog.findViewById<EditText>(R.id.item_edit_name_value).text.toString()
                if (itemDialog.findViewById<EditText>(R.id.item_edit_quantity_value).text.isNullOrBlank()) {
                    newItem.quantity = 1
                } else {
                    newItem.quantity =
                        itemDialog.findViewById<EditText>(R.id.item_edit_quantity_value).text.toString().toInt()
                }

                newItem.type =
                    ItemTypeEnum.getTypeByValue(typeEditText.text.toString())
                newItem.description =
                    itemDialog.findViewById<EditText>(R.id.item_edit_description_value).text.toString()

                if (position == -1) {
                    character.inventory.items.add(newItem)
                } else {
                    character.inventory.items[position] = newItem
                }

                MainActivity.viewModel.editCharacter(character)

                itemDialog.dismiss()
            }
        return itemDialog
    }

    /**
     * Rempli la dialog avec les infos de l'objet' à modifier
     */
    private fun fillItemDialog(itemDialog: Dialog, item: Item) {
        itemDialog.findViewById<EditText>(R.id.item_edit_name_value)
            .setText(item.name)
        itemDialog.findViewById<EditText>(R.id.item_edit_quantity_value)
            .setText(item.quantity.toString())
        itemDialog.findViewById<EditText>(R.id.item_edit_description_value)
            .setText(item.description)
        when (item.type) {
            ItemTypeEnum.QUEST -> itemDialog.findViewById<AutoCompleteTextView>(R.id.item_edit_type_text)
                .setText(ItemTypeEnum.QUEST.value, false)
            ItemTypeEnum.CONSUMABLE -> itemDialog.findViewById<AutoCompleteTextView>(R.id.item_edit_type_text)
                .setText(ItemTypeEnum.CONSUMABLE.value, false)
            ItemTypeEnum.POTION -> itemDialog.findViewById<AutoCompleteTextView>(R.id.item_edit_type_text)
                .setText(ItemTypeEnum.POTION.value, false)
            ItemTypeEnum.SCROLL -> itemDialog.findViewById<AutoCompleteTextView>(R.id.item_edit_type_text)
                .setText(ItemTypeEnum.SCROLL.value, false)
            ItemTypeEnum.TOOL -> itemDialog.findViewById<AutoCompleteTextView>(R.id.item_edit_type_text)
                .setText(ItemTypeEnum.TOOL.value, false)
            else -> itemDialog.findViewById<AutoCompleteTextView>(R.id.item_edit_type_text)
            .setText(ItemTypeEnum.ITEM.value, false)
        }
    }

    /**
     * Ouverture equipment dialog
     */
    private fun openEquipmentDialog(position: Int): Dialog {
        val equipmentDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)

        equipmentDialog.setContentView(R.layout.dialog_equipment)

        equipmentDialog.findViewById<TextView>(R.id.equipment_edit_title).text =
            getString(R.string.fight_equipment)

        equipmentDialog.show()

        equipmentDialog.findViewById<ImageView>(R.id.equipment_edit_close)
            .setOnClickListener { equipmentDialog.dismiss() }

        equipmentDialog.findViewById<Button>(R.id.equipment_edit_save_button).setOnClickListener {
            val newEquipment = Equipment()

            newEquipment.name = equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_name_value).text.toString()
            newEquipment.ca = equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_ca_value).text.toString().toInt()
            newEquipment.damage = equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_damage_value).text.toString()
            if (equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_cac).isChecked) {
                newEquipment.type = EquipmentTypeEnum.WEAPON
            }
            if (equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_range).isChecked) {
                newEquipment.type = EquipmentTypeEnum.WEAPON_RANGE
            }
            if (equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_gun).isChecked) {
                newEquipment.type = EquipmentTypeEnum.GUN
            }
            if (equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_shield).isChecked) {
                newEquipment.type = EquipmentTypeEnum.SHIELD
            }
            if (equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_armor).isChecked) {
                newEquipment.type = EquipmentTypeEnum.ARMOR
            }
            if (equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_other).isChecked) {
                newEquipment.type = EquipmentTypeEnum.OTHER
            }
            newEquipment.properties = equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_properties_value).text.toString()
            newEquipment.special = equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_special_value).text.toString()
            newEquipment.description = equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_description_value).text.toString()

            newEquipment.equiped = equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_equiped).isChecked

            if (position == -1) {
                character.inventory.equipments.add(newEquipment)
            } else {
                character.inventory.equipments[position] = newEquipment
            }

            MainActivity.viewModel.editCharacter(character)
            equipmentDialog.dismiss()
        }

        return equipmentDialog
    }

    private fun fillEquipmentDialog(equipmentDialog: Dialog, equipment: Equipment) {
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
        }

        if (equipment.type == EquipmentTypeEnum.WEAPON) equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_cac).isChecked = true
        if (equipment.type == EquipmentTypeEnum.WEAPON_RANGE) equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_range).isChecked = true
        if (equipment.type == EquipmentTypeEnum.GUN) equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_gun).isChecked = true
        if (equipment.type == EquipmentTypeEnum.SHIELD) equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_shield).isChecked = true
        if (equipment.type == EquipmentTypeEnum.ARMOR) equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_armor).isChecked = true
        if (equipment.type == EquipmentTypeEnum.OTHER) equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_weapon_type_other).isChecked = true

        equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_properties_value)
            .setText(equipment.properties)
        equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_special_value)
            .setText(equipment.special)
        equipmentDialog.findViewById<TextInputEditText>(R.id.equipment_edit_description_value)
            .setText(equipment.description)

        equipmentDialog.findViewById<CheckBox>(R.id.equipment_edit_equiped).isChecked = equipment.equiped
    }

    private fun getPositionCharacterItem(position: Int) =
        character.inventory.items.indexOf(items[position])

    private fun getPositionCharacterEquipment(position: Int) =
        character.inventory.equipments.indexOf(equipments[position])

    override fun onStuffItemClicked(item: Item, position: Int, id: Int) {
        when (id) {
            R.id.item_delete -> {
                val builder = AlertDialog.Builder(context)
                with(builder)
                {
                    setTitle(getString(R.string.item_delete))
                    setMessage(
                        getString(
                            R.string.item_delete_txt,
                            item.name
                        )
                    )
                    setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
                    setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                        character.inventory.items.remove(item)
                        MainActivity.viewModel.editCharacter(character)
                        dialog.cancel()
                    }
                    show()
                }
            }
            R.id.item_edit -> {
                val itemDialog = openItemDialog(getPositionCharacterItem(position))
                fillItemDialog(itemDialog, item)
            }
            R.id.item_minus_one -> {
                item.quantity --
                MainActivity.viewModel.editCharacter(character)
            }
            R.id.item_plus_one -> {
                item.quantity ++
                MainActivity.viewModel.editCharacter(character)
            }
        }
    }

    override fun onEquipmentClicked(equipment: Equipment, position: Int, id: Int) {
        when (id) {
            R.id.equipment_delete -> {
                val builder = AlertDialog.Builder(context)
                with(builder)
                {
                    setTitle(getString(R.string.equipment_delete))
                    setMessage(
                        getString(
                            R.string.equipment_delete_txt,
                            equipment.name
                        )
                    )
                    setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
                    setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                        character.inventory.equipments.remove(equipment)
                        MainActivity.viewModel.editCharacter(character)
                        dialog.cancel()
                    }
                    show()
                }
            }
            R.id.equipment_edit -> {
                val equipmentDialog = openEquipmentDialog(getPositionCharacterEquipment(position))
                fillEquipmentDialog(equipmentDialog, equipment)
            }
        }
    }
}