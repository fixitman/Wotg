package com.fimappware.willofthegods.ui.group

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.fimappware.willofthegods.R

class InputTextDialog(
    private val initialText: String = "",
    private val title: String = "",
    private val listener: EventListener
) : DialogFragment() {

    interface EventListener {
        fun onDlgPositiveEvent(dialog: DialogFragment)
        fun onDlgNegativeEvent(dialog: DialogFragment)
    }

    private lateinit var inputText: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        activity?.let {
            val b = AlertDialog.Builder(it)
            val v = LayoutInflater.from(it).inflate(R.layout.input_dialog_layout, null, false)
            //todo make a better layout
            b.setView(v)
            if (title.isNotBlank()) {
                b.setTitle(title)
            }
            b.setPositiveButton("OK") { _, _ ->
                listener.onDlgPositiveEvent(this)
            }
            b.setNegativeButton("Cancel") { _, _ ->
                listener.onDlgNegativeEvent(this)
            }
            inputText = v.findViewById(R.id.et_group_name)
            inputText.setText(initialText)
            return b.create()
        } ?: throw IllegalStateException("No Activity")


    }


    fun getInputText(): String {
        return inputText.text.toString()
    }
}