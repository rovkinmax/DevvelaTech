package ru.rovkinmax.devvelatech.dialog

import android.app.Dialog
import android.app.DialogFragment
import android.app.Fragment
import android.app.FragmentManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AlertDialog
import ru.rovkinmax.devvelatech.R
import ru.rovkinmax.devvelatech.view.LoadingView
import java.lang.ref.Reference
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean

class LoadingDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity, R.style.AppTheme_Dialog)
                .setView(R.layout.fmt_loading)
                .create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isCancelable = false
    }


    companion object {

        private val HANDLER = Handler(Looper.getMainLooper())

        fun view(fm: FragmentManager): LoadingView {
            return object : LoadingView {

                private val mWaitForHide = AtomicBoolean()

                override fun showLoadingIndicator() {
                    if (mWaitForHide.compareAndSet(false, true)) {
                        LoadingDialog().show(fm, LoadingDialog::class.java.name)
                    }
                }

                override fun hideLoadingIndicator() {
                    if (mWaitForHide.compareAndSet(true, false)) {
                        HANDLER.post(HideTask(fm))
                    }
                }
            }
        }

        fun view(fragment: Fragment): LoadingView {
            return view(fragment.fragmentManager)
        }
    }

    private class HideTask(fm: FragmentManager) : Runnable {

        private val mFmRef: Reference<FragmentManager>

        private var mAttempts = 10

        init {
            mFmRef = WeakReference(fm)
        }

        override fun run() {
            HANDLER.removeCallbacks(this)
            val fm = mFmRef.get()
            if (fm != null) {
                val dialog = fm.findFragmentByTag(LoadingDialog::class.java.name) as LoadingDialog?
                if (dialog != null) {
                    dialog.dismissAllowingStateLoss()
                } else if (--mAttempts >= 0) {
                    HANDLER.postDelayed(this, 300)
                }
            }
        }

    }

}
