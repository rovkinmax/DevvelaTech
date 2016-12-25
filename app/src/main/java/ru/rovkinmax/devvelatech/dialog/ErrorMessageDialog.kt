package ru.rovkinmax.devvelatech.dialog

import android.app.Dialog
import android.app.DialogFragment
import android.app.FragmentManager
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import ru.rovkinmax.devvelatech.R

class ErrorMessageDialog() : DialogFragment() {

    companion object {
        fun show(fragmentManager: FragmentManager, message: String, func: (() -> Unit)?) {
            ErrorMessageDialog().apply {
                this.message = message
                dismissCallback = func

            }.show(fragmentManager, ErrorMessageDialog::class.java.simpleName)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private var dismissCallback: (() -> Unit)? = null
    private var message = ""
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity, R.style.AppTheme_Dialog)
                .setMessage(message)
                .setPositiveButton(R.string.button_close, null)
                .create()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        dismissCallback?.invoke()
        super.onDismiss(dialog)
    }
}
