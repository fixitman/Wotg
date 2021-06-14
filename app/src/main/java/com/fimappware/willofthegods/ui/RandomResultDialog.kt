package com.fimappware.willofthegods.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.fimappware.willofthegods.R
import eltos.simpledialogfragment.CustomViewDialog

class RandomResultDialog : CustomViewDialog<RandomResultDialog>() {

    override fun onCreateContentView(savedInstanceState: Bundle?): View {
        val view = inflate(R.layout.custom_result_dialog_layout)
        val textView : TextView = view.findViewById(R.id.custom_result_dialog_text)
        textView.text = requireArguments().getString(ARG_TEXT)
        textView.setBackgroundColor(requireArguments().getInt(ARG_COLOR))
        return view
    }

    fun text(text : String?) : RandomResultDialog{
       setArg(ARG_TEXT,text)
        return this
    }

    fun color(color : Int ) : RandomResultDialog{
        setArg(ARG_COLOR,color)
        return this
    }

    companion object{
        private const val TAG = "RandomResultDialog."
        private const val ARG_COLOR = TAG + "argColor"
        private const val ARG_TEXT = TAG + "argText"

        fun build() : RandomResultDialog{
            return RandomResultDialog()
        }
    }

}