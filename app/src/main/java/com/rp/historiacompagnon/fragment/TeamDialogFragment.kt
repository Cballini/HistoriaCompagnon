package com.rp.historiacompagnon.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.rp.historiacompagnon.R


class TeamDialogFragment : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Material_Dialog)
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
        setupView(view)
        setupClickListeners(view)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    private fun setupView(view: View) {
        //view.findViewById<TextView>(R.id.tvTitle).text = arguments?.getString(KEY_TITLE)
    }

    private fun setupClickListeners(view: View) {
        view.findViewById<ImageView>(R.id.team_back).setOnClickListener {
            dismiss()
        }

        view.findViewById<ImageView>(R.id.team_invitation_code_copy).setOnClickListener {
            val cm = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setPrimaryClip(ClipData.newPlainText("text", view.findViewById<TextView>(R.id.team_invitation_code_code).text))
            Toast.makeText(context, getString(R.string.copy), Toast.LENGTH_SHORT).show()
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