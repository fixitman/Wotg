package com.fimappware.willofthegods.ui.numbers

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fimappware.willofthegods.databinding.NumbersFragmentBinding
import eltos.simpledialogfragment.SimpleDialog
import java.util.*

class NumbersFragment : Fragment() {

    companion object {
        const val STATE_FROM = "StateFrom"
        const val STATE_TO = "StateTo"
    }

    private var mFrom = 0
    private var mTo = 0

    private lateinit var bind : NumbersFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = NumbersFragmentBinding.inflate(inflater,container,false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let{
            val f = savedInstanceState.getInt(STATE_FROM,1).toString()
            val t = savedInstanceState.getInt(STATE_TO,10).toString()
            bind.etFrom.setText(f)
            bind.etTo.setText(t)
        }
        bind.button.setOnClickListener { _ ->
            onGoClick()
        }
        bind.etTo.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                mTo = s.toString().toInt()
            }
        })
        bind.etFrom.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                mFrom = s.toString().toInt()
            }
        })
    }

    private fun onGoClick() {
        if(mTo <= mFrom){
            SimpleDialog.build()
                .title("Error")
                .msg("From value must me greater than To value")
                .show(this,"ErrorDialog")
        }else {
            val result = Random().nextInt(mTo-mFrom+1) + mFrom
            SimpleDialog.build()
                .title("Result")
                .msg("I pick... $result")
                .show(this,"NumberResultDialog")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_FROM,mFrom)
        outState.putInt(STATE_TO,mTo)
        super.onSaveInstanceState(outState)
    }
}