package ru.rovkinmax.devvelatech.dialog

import android.app.Dialog
import android.app.DialogFragment
import android.app.FragmentManager
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.EditText
import ru.rovkinmax.devvelatech.R

class NameInputDialog : DialogFragment() {

    private var positiveClickListener: ((String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity.layoutInflater.inflate(R.layout.fmt_name_input, null)
        return AlertDialog.Builder(activity, R.style.AppTheme_Dialog)
                .setView(view)
                .setPositiveButton(android.R.string.ok, { d, i ->
                    val etName = view.findViewById(R.id.etName) as EditText
                    positiveClickListener?.invoke(etName.text.toString())
                })
                .setNegativeButton(R.string.button_close, null)
                .create()
    }

    companion object {
        fun show(fragmentManager: FragmentManager, listener: ((String) -> Unit)) {
            val dialog = NameInputDialog()
            dialog.positiveClickListener = listener
            dialog.show(fragmentManager, NameInputDialog::class.java.simpleName)
        }
    }
}