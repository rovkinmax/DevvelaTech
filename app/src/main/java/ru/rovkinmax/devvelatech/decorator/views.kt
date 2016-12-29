package ru.rovkinmax.devvelatech.decorator

import android.app.FragmentManager
import ru.rovkinmax.devvelatech.dialog.LoadingDialog
import ru.rovkinmax.devvelatech.rx.RxError

interface ErrorView {
    fun showErrorMessage(message: String, needCallback: Boolean = false)

    fun showNetworkError() {
    }

    fun showUnexpectedError() {
    }

    fun logout() {
    }
}

interface EmptyView {

    fun showEmptyStub()

    fun hideEmptyStub()
}

interface LoadingView {
    fun showLoadingIndicator()

    fun hideLoadingIndicator()
}

class ErrorLoadingDecorator(fragmentManager: FragmentManager) : ErrorView, LoadingView {

    private val errorView by lazy { RxError.view(fragmentManager) }
    private val loadingView by lazy { LoadingDialog.view(fragmentManager) }

    override fun showErrorMessage(message: String, needCallback: Boolean) {
        errorView.showErrorMessage(message, needCallback)
    }

    override fun showLoadingIndicator() = loadingView.showLoadingIndicator()

    override fun hideLoadingIndicator() = loadingView.hideLoadingIndicator()
}