package com.fimappware.willofthegods.ui.numbers

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fimappware.willofthegods.data.AppDb
import com.fimappware.willofthegods.databinding.NumbersFragmentBinding
import com.fimappware.willofthegods.hideKeyboard
import com.fimappware.willofthegods.isNumber
import com.fimappware.willofthegods.ui.group.AppViewModel
import eltos.simpledialogfragment.SimpleDialog
import kotlin.random.Random


class NumbersFragment : Fragment() {

    private val vm : AppViewModel by lazy{
        val appDb = AppDb.getInstance(requireContext())
        val factory = AppViewModel.Factory(appDb)
        ViewModelProvider(requireActivity(),factory).get(AppViewModel::class.java)
    }

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

        bind.etFrom.setText(vm.from.toString())
        bind.etTo.setText(vm.to.toString())
        bind.button.setOnClickListener { _ -> onGoClick()  }

        bind.etTo.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                vm.to = if(s.toString().isNumber()) s.toString().toLong() else 0
            }
        })

        bind.etTo.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onGoClick()
            }
            true
        }

        bind.etFrom.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                vm.from = if(s.toString().isNumber()) s.toString().toLong() else 0
            }
        })
    }

    private fun onGoClick() {

        if(!bind.etFrom.text.toString().isNumber() ||
            !bind.etTo.text.toString().isNumber()  ){
            SimpleDialog.build()
                .title("Error")
                .msg("Value must a number")
                .show(this,"ErrorDialog")
           return
        }

        hideKeyboard()

        if(vm.to < vm.from){
            val temp = vm.from
            vm.from = vm.to
            vm.to = temp
        }

        val result = Random.nextLong(vm.to - vm.from + 1) + vm.from
        SimpleDialog.build()
            .title("Result")
            .msg("I pick... $result")
            .show(this,"NumberResultDialog")
    }
}