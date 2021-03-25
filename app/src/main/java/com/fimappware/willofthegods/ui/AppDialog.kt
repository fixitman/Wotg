package com.fimappware.willofthegods.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.fimappware.willofthegods.R

const val DIALOG_ID = "dialog_id"
const val DIALOG_MESSAGE = "dialog_message"
const val DIALOG_TITLE = "dialog_title"
const val DIALOG_ICON = "dialog_icon"
const val DIALOG_POSITIVE_RID = "positive_pid"
const val DIALOG_NEGATIVE_RID = "negative_pid"

class AppDialog : AppCompatDialogFragment() {

    private var dlgEvents : DlgEvents? = null

    internal interface DlgEvents{
        fun onDialogPositiveResult( dialogId: String, args: Bundle) {}
        fun onDialogNegativeResult( dialogId: String, args: Bundle) {}
        fun onDialogCancelResult(dialogId: String, args: Bundle) {}
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dlgEvents = try{
            parentFragment as DlgEvents
        }catch(e: NullPointerException){
            try{
                context as DlgEvents
            }catch(e: ClassCastException){
                throw ClassCastException("$context must implement DlgEvents")
            }
        }catch(e: ClassCastException){
            throw java.lang.ClassCastException("$parentFragment must implement DlgEvents")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val args = requireArguments()
        val dialogId = args.getString(DIALOG_ID)
        val message = args.getString(DIALOG_MESSAGE)
        val title = args.getString(DIALOG_TITLE)
        val icon = args.getInt(DIALOG_ICON)
        var positiveRID = args.getInt(DIALOG_POSITIVE_RID)
        var negativeRID = args.getInt(DIALOG_NEGATIVE_RID)

        if(positiveRID == 0) positiveRID = R.string.ok
        if(negativeRID == 0) negativeRID = R.string.cancel


        if(dialogId != null && message != null){
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage(message)
                .setTitle(title?:"")
                .setIcon(icon)
                .setPositiveButton(positiveRID){ _, _ ->
                    dlgEvents?.onDialogPositiveResult(dialogId,args)
                }.setNegativeButton(negativeRID) { _, _ ->
                        dlgEvents?.onDialogNegativeResult(dialogId, args)
                        dismiss()
                }
            if(icon != 0) builder.setIcon(icon)
            if(title != null) builder.setTitle(title)
            return builder.create()
        }else{
            throw AssertionError("dialogId and message are required")
        }

    }

    override fun onDetach() {
        super.onDetach()
        dlgEvents = null
    }
}