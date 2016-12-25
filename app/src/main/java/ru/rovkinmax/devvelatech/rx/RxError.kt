package ru.rovkinmax.devvelatech.rx

import android.app.Fragment
import android.app.FragmentManager
import io.reactivex.functions.Consumer
import ru.rovkinmax.devvelatech.dialog.ErrorMessageDialog
import ru.rovkinmax.devvelatech.view.ErrorView
import timber.log.Timber

object RxError {

    fun view(fragment: Fragment): ErrorView {
        return view(fragment.fragmentManager)
    }

    fun view(fm: FragmentManager, func: (() -> Unit)? = null): ErrorView {
        return object : ErrorView {
            override fun showNetworkError() {
                //todo add implementation
            }

            override fun showUnexpectedError() {
                //todo add implementation
            }

            override fun showErrorMessage(message: String, needCallback: Boolean) {
                ErrorMessageDialog.show(fm, message, if (needCallback) func else null)
            }

            override fun logout() {

            }
        }
    }

    fun doNothing(): Consumer<Throwable> = Consumer { Timber.tag(RxError::class.java.name).d(it, "Something went wrong") }

}