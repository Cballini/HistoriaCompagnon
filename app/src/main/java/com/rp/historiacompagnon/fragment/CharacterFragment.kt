package com.rp.historiacompagnon.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.rp.historiacompagnon.MainActivity
import com.rp.historiacompagnon.R
import com.rp.historiacompagnon.Services
import com.rp.historiacompagnon.component.JobComponent
import com.rp.historiacompagnon.entity.Character
import com.rp.historiacompagnon.entity.Job


class CharacterFragment : Fragment() {
    lateinit var character : Character
    lateinit var pathRef: StorageReference
    private val READ_EXTERNAL_STORAGE_CODE = 2
    private val REQUEST_CODE = 1000

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_character, container, false)

        MainActivity.viewModel.currentCharacter.observe(viewLifecycleOwner, Observer {
            character = it

           pathRef =  Firebase.storage.reference.child("character_picture/${character.key}")

            // TODO message si essaie modifier alors que aucun perso current (pas de key perso) → faire fnction utils

            // Base
            fillBasePanel(view, it)
            
            // Job
            // Infos 1er job
            if (character.jobs.size >= 1) {
                fillJob(view, R.id.character_job1, 0)
            } else {
                view.findViewById<JobComponent>(R.id.character_job1).visibility = View.GONE
            }

            // Infos 2nd job
            if (character.jobs.size == 2) {
                fillJob(view, R.id.character_job2, 1)
                // Cacher ajout si déjà 2 jobs
                view.findViewById<Button>(R.id.character_job_add).visibility = View.GONE
            } else {
                view.findViewById<Button>(R.id.character_job_add).visibility = View.VISIBLE
                view.findViewById<JobComponent>(R.id.character_job2).visibility = View.GONE
            }

            // Career
            fillCareerPanel(view, it)

            // Destiny
            fillDestinyPanel(view, it)

            // Description
            view.findViewById<TextView>(R.id.character_description_txt).text = it.description
        })

        // Edit
        openEditBase(view)

        openEditPicture(view)

        openEditCareer(view)

        openEditDestiny(view)

        openEditDescription(view)

        view.findViewById<Button>(R.id.character_job_add).setOnClickListener {
            character.jobs.add(Job())
            MainActivity.viewModel.editCharacter(character)
        }

        return view
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
            getString(R.string.job_specialty_lvl, job.specialty, job.level)
        component.jobModifier.text = getString(R.string.job_plus_something, job.modifier)
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

        joDialog.setContentView(R.layout.dialog_job)
        joDialog.show()
        joDialog.findViewById<ImageView>(R.id.job_edit_close)
            .setOnClickListener { joDialog.dismiss() }

        fillJobDialog(joDialog, jobPos)

        joDialog.findViewById<Button>(R.id.job_edit_save_button).setOnClickListener {
            character.jobs[jobPos].name =
                joDialog.findViewById<EditText>(R.id.job_edit_name_value).text.toString()
            character.jobs[jobPos].specialty =
                joDialog.findViewById<EditText>(R.id.job_edit_specialty_value).text.toString()
            character.jobs[jobPos].level =
                joDialog.findViewById<EditText>(R.id.job_edit_level_value).text.toString()
                    .toInt()
            character.jobs[jobPos].modifier =
                joDialog.findViewById<EditText>(R.id.job_edit_modifier_value).text.toString()
                    .toInt()
            character.jobs[jobPos].lifeDiceByLvl =
                joDialog.findViewById<EditText>(R.id.job_edit_dice_lvl_value).text.toString()
            character.jobs[jobPos].typeArmor =
                joDialog.findViewById<EditText>(R.id.job_edit_armors_value).text.toString()
            character.jobs[jobPos].typeWeapon =
                joDialog.findViewById<EditText>(R.id.job_edit_weapons_value).text.toString()
            character.jobs[jobPos].save =
                joDialog.findViewById<EditText>(R.id.job_edit_throw_save_value).text.toString()

            MainActivity.viewModel.editCharacter(character)
            joDialog.dismiss()
        }
    }

    /**
     * Rempli la dialog avec les infos du job
     */
    private fun fillJobDialog(joDialog: Dialog, jobPos: Int) {
        joDialog.findViewById<EditText>(R.id.job_edit_name_value)
            .setText(character.jobs[jobPos].name)
        joDialog.findViewById<EditText>(R.id.job_edit_specialty_value)
            .setText(character.jobs[jobPos].specialty)
        joDialog.findViewById<EditText>(R.id.job_edit_level_value)
            .setText(character.jobs[jobPos].level.toString())
        joDialog.findViewById<EditText>(R.id.job_edit_modifier_value)
            .setText(character.jobs[jobPos].modifier.toString())
        joDialog.findViewById<EditText>(R.id.job_edit_dice_lvl_value)
            .setText(character.jobs[jobPos].lifeDiceByLvl)
        joDialog.findViewById<EditText>(R.id.job_edit_armors_value)
            .setText(character.jobs[jobPos].typeArmor)
        joDialog.findViewById<EditText>(R.id.job_edit_weapons_value)
            .setText(character.jobs[jobPos].typeWeapon)
        joDialog.findViewById<EditText>(R.id.job_edit_throw_save_value)
            .setText(character.jobs[jobPos].save)
    }

    private fun openEditDescription(view: View) {
        view.findViewById<ImageView>(R.id.character_description_edit).setOnClickListener {
            val descriptionDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)

            descriptionDialog.setContentView(R.layout.dialog_character_description)
            descriptionDialog.show()
            descriptionDialog.findViewById<ImageView>(R.id.character_description_edit_close)
                .setOnClickListener { descriptionDialog.dismiss() }

            descriptionDialog.findViewById<EditText>(R.id.character_description_edit_value)
                .setText(character.description)

            descriptionDialog.findViewById<TextView>(R.id.character_description_edit_save_button)
                .setOnClickListener {
                    character.description =
                        descriptionDialog.findViewById<EditText>(R.id.character_description_edit_value).text.toString()

                    Services.editCharacter(character, MainActivity.viewModel.user.value!!.key)

                    descriptionDialog.dismiss()
                }
        }
    }

    private fun openEditDestiny(view: View) {
        view.findViewById<ImageView>(R.id.character_destiny_edit).setOnClickListener {
            val destinyDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)

            destinyDialog.setContentView(R.layout.dialog_character_destiny)
            destinyDialog.show()
            destinyDialog.findViewById<ImageView>(R.id.character_destiny_edit_close)
                .setOnClickListener { destinyDialog.dismiss() }

            fillEditDestinyCharacter(destinyDialog)

            destinyDialog.findViewById<TextView>(R.id.character_destiny_edit_save_button)
                .setOnClickListener {
                    character.destiny.name =
                        destinyDialog.findViewById<EditText>(R.id.character_destiny_edit_name_value).text.toString()
                    character.destiny.value =
                        destinyDialog.findViewById<EditText>(R.id.character_destiny_edit_value_value).text.toString()
                    character.destiny.link =
                        destinyDialog.findViewById<EditText>(R.id.character_destiny_edit_link_value).text.toString()
                    character.destiny.ideal =
                        destinyDialog.findViewById<EditText>(R.id.character_destiny_edit_ideal_value).text.toString()
                    character.destiny.defect =
                        destinyDialog.findViewById<EditText>(R.id.character_destiny_edit_defect_value).text.toString()
                    character.destiny.trait =
                        destinyDialog.findViewById<EditText>(R.id.character_destiny_edit_trait_value).text.toString()

                    Services.editCharacter(character, MainActivity.viewModel.user.value!!.key)

                    destinyDialog.dismiss()
                }
        }
    }

    private fun openEditCareer(view: View) {
        view.findViewById<ImageView>(R.id.character_career_edit).setOnClickListener {
            val careerDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)

            careerDialog.setContentView(R.layout.dialog_character_career)
            careerDialog.show()
            careerDialog.findViewById<ImageView>(R.id.character_career_edit_close)
                .setOnClickListener { careerDialog.dismiss() }

            fillEditCareerCharacter(careerDialog)

            careerDialog.findViewById<TextView>(R.id.character_career_edit_save_button)
                .setOnClickListener {
                    character.career.name =
                        careerDialog.findViewById<EditText>(R.id.character_career_edit_name_value).text.toString()

                    if (careerDialog.findViewById<EditText>(R.id.character_career_edit_rank_value).text.toString().isNotBlank()){
                        character.career.rank =
                            careerDialog.findViewById<EditText>(R.id.character_career_edit_rank_value).text.toString()
                                .toInt()
                    }

                    character.career.description =
                        careerDialog.findViewById<EditText>(R.id.character_career_edit_description_value).text.toString()

                    Services.editCharacter(character, MainActivity.viewModel.user.value!!.key)

                    careerDialog.dismiss()
                }
        }
    }

    private fun openEditPicture(view: View) {
        view.findViewById<ImageView>(R.id.character_picture).setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_EXTERNAL_STORAGE_CODE
                )
            } else {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE)
            }
        }
    }

    private fun openEditBase(view: View) {
        view.findViewById<ImageView>(R.id.character_base_edit).setOnClickListener {
            val baseDialog = Dialog(requireContext(), android.R.style.Theme_NoTitleBar)

            baseDialog.setContentView(R.layout.dialog_character_base)
            baseDialog.show()
            baseDialog.findViewById<ImageView>(R.id.character_base_edit_close)
                .setOnClickListener { baseDialog.dismiss() }

            fillEditBaseCharacter(baseDialog)

            baseDialog.findViewById<TextView>(R.id.character_base_edit_save_button)
                .setOnClickListener {
                    character.name =
                        baseDialog.findViewById<EditText>(R.id.character_base_edit_name_value).text.toString()
                    character.familyName =
                        baseDialog.findViewById<EditText>(R.id.character_base_edit_family_name_value).text.toString()
                    character.race.familia =
                        baseDialog.findViewById<EditText>(R.id.character_base_edit_race_value).text.toString()
                    character.race.familiaInstinct =
                        baseDialog.findViewById<EditText>(R.id.character_base_edit_race_instinct_value).text.toString()
                    character.race.species =
                        baseDialog.findViewById<EditText>(R.id.character_base_edit_species_value).text.toString()
                    character.race.speciesInstinct =
                        baseDialog.findViewById<EditText>(R.id.character_base_edit_species_instinct_value).text.toString()

                    if (baseDialog.findViewById<EditText>(R.id.character_base_edit_age_value).text.toString().isNotBlank()) {
                        character.age =
                            baseDialog.findViewById<EditText>(R.id.character_base_edit_age_value).text.toString()
                                .toInt()
                    }

                    if (baseDialog.findViewById<EditText>(R.id.character_base_edit_size_value).text.toString().isNotBlank()) {
                        character.size =
                            baseDialog.findViewById<EditText>(R.id.character_base_edit_size_value).text.toString()
                                .toInt()
                    }

                    if (baseDialog.findViewById<EditText>(R.id.character_base_edit_weight_value).text.toString().isNotBlank()) {
                        character.weight =
                            baseDialog.findViewById<EditText>(R.id.character_base_edit_weight_value).text.toString()
                                .toInt()
                    }

                    Services.editCharacter(character, MainActivity.viewModel.user.value!!.key)

                    baseDialog.dismiss()
                }
        }
    }

    private fun fillDestinyPanel(
        view: View,
        it: Character
    ) {
        view.findViewById<TextView>(R.id.character_destiny).text = it.destiny.name
        view.findViewById<TextView>(R.id.character_destiny_value_txt).text = it.destiny.value
        view.findViewById<TextView>(R.id.character_destiny_ideal_txt).text = it.destiny.ideal
        view.findViewById<TextView>(R.id.character_destiny_link_txt).text = it.destiny.link
        view.findViewById<TextView>(R.id.character_destiny_defect_txt).text = it.destiny.defect
        view.findViewById<TextView>(R.id.character_destiny_trait_txt).text = it.destiny.trait
    }

    private fun fillCareerPanel(
        view: View,
        it: Character
    ) {
        view.findViewById<TextView>(R.id.character_career).text = it.career.name
        view.findViewById<TextView>(R.id.character_career_rank).text = getString(R.string.character_rank, it.career.rank)
        view.findViewById<TextView>(R.id.character_career_description).text = it.career.description
    }

    private fun fillBasePanel(view: View, it: Character) {
        view.findViewById<TextView>(R.id.character_full_name).text = it.concatFullName()
        view.findViewById<TextView>(R.id.character_familia).text =
            getString(R.string.tiret_2, it.race.familia, it.race.species)
        view.findViewById<TextView>(R.id.character_measures).text =
            getString(R.string.character_measures, it.age, it.size, it.weight)
        view.findViewById<TextView>(R.id.character_instinct_familia).text = it.race.familiaInstinct
        view.findViewById<TextView>(R.id.character_instinct_species).text = it.race.speciesInstinct
        pathRef.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this)
                .load(uri.toString())
                .into(view.findViewById<ImageView>(R.id.character_picture))
        }.addOnFailureListener {
            view.findViewById<ImageView>(R.id.character_picture).setImageResource(R.drawable.image)
        }
    }

    /**
     * Pré-remplie la fenêtre pour la base du perso
     */
    private fun fillEditBaseCharacter(baseDialog: Dialog) {
        baseDialog.findViewById<EditText>(R.id.character_base_edit_name_value)
            .setText(character.name)
        baseDialog.findViewById<EditText>(R.id.character_base_edit_family_name_value)
            .setText(character.familyName)
        baseDialog.findViewById<EditText>(R.id.character_base_edit_race_value)
            .setText(character.race.familia)
        baseDialog.findViewById<EditText>(R.id.character_base_edit_race_instinct_value)
            .setText(character.race.familiaInstinct)
        baseDialog.findViewById<EditText>(R.id.character_base_edit_species_value)
            .setText(character.race.species)
        baseDialog.findViewById<EditText>(R.id.character_base_edit_species_instinct_value)
            .setText(character.race.speciesInstinct)
        baseDialog.findViewById<EditText>(R.id.character_base_edit_age_value).setText(character.age.toString())
        baseDialog.findViewById<EditText>(R.id.character_base_edit_size_value)
            .setText(character.size.toString())
        baseDialog.findViewById<EditText>(R.id.character_base_edit_weight_value)
            .setText(character.weight.toString())
    }

    /**
     * Pré-remplie la fenêtre pour la carrière du perso
     */
    private fun fillEditCareerCharacter(baseDialog: Dialog) {
        baseDialog.findViewById<EditText>(R.id.character_career_edit_name_value)
            .setText(character.career.name)
        baseDialog.findViewById<EditText>(R.id.character_career_edit_rank_value)
            .setText(character.career.rank.toString())
        baseDialog.findViewById<EditText>(R.id.character_career_edit_description_value)
            .setText(character.career.description)
    }

    /**
     * Pré-remplie la fenêtre pour la destinée du perso
     */
    private fun fillEditDestinyCharacter(baseDialog: Dialog) {
        baseDialog.findViewById<EditText>(R.id.character_destiny_edit_name_value).setText(character.destiny.name)
        baseDialog.findViewById<EditText>(R.id.character_destiny_edit_ideal_value).setText(character.destiny.ideal)
        baseDialog.findViewById<EditText>(R.id.character_destiny_edit_link_value).setText(character.destiny.link)
        baseDialog.findViewById<EditText>(R.id.character_destiny_edit_defect_value).setText(character.destiny.defect)
        baseDialog.findViewById<EditText>(R.id.character_destiny_edit_value_value).setText(character.destiny.value)
        baseDialog.findViewById<EditText>(R.id.character_destiny_edit_trait_value).setText(character.destiny.trait)
    }

    private fun handlePictureResponse(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            var uri = data?.data

            requireView().findViewById<ImageView>(R.id.character_picture).setImageURI(data?.data) // affichage

            //Store in the cloud
            // [START upload_with_metadata]
            // Create file metadata including the content type
            var metadata = storageMetadata {
                contentType = "image/jpg"
            }

            var file = uri!!
            var uploadTask = pathRef.putFile(file, metadata)

            uploadTask.addOnProgressListener { taskSnapshot ->
                val progress = (100.0 * taskSnapshot.bytesTransferred) / taskSnapshot.totalByteCount
                println("Upload is $progress% done")
            }.addOnFailureListener {
                Log.e("upload", "fail")
            }.addOnSuccessListener {
                Log.e("upload", "success")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        handlePictureResponse(resultCode, data)
    }
}