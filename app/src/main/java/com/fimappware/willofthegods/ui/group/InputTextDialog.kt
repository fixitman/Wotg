package com.fimappware.willofthegods.ui.group

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.fimappware.willofthegods.R

class InputTextDialog : DialogFragment() {

    interface EventListener{
        fun onDlgPositiveEvent(dialog: DialogFragment)
        fun onDlgNegativeEvent(dialog: DialogFragment)
    }
    private lateinit var listener : EventListener
    private lateinit var inputText : EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        activity?.let{
            val b = AlertDialog.Builder(it)
            val v = LayoutInflater.from(it).inflate(R.layout.input_dialog_layout,null,false)
            //todo make a better layout
            b.setView(v)
            b.setTitle("New group name:")
            b.setPositiveButton("OK"){_,_ ->
                listener.onDlgPositiveEvent(this)
            }
            b.setNegativeButton("Cancel"){_,_ ->
                listener.onDlgNegativeEvent(this)
            }
            inputText = v.findViewById(R.id.et_group_name)
            return b.create()
        }?: throw IllegalStateException("No Activity")


    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        try{
            listener = context as EventListener
        }catch(e: ClassCastException){
            try {
                listener = parentFragment as EventListener
            }catch (e: java.lang.ClassCastException){
                throw java.lang.ClassCastException("Neither activity nor parent fragment implements EventListener")
            }
        }
    }

    fun getInputText(): String{
        return inputText.text.toString()
    }
}