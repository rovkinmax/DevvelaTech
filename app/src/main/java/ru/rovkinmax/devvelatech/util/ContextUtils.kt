package ru.rovkinmax.devvelatech.util

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun <reified T : Any> IntentFor(context: Context): Intent = Intent(context, T::class.java)

inline fun <reified T : Any> Context.startActivity() {
    startActivity(IntentFor<T>(this))
}

inline fun <reified T : Any> Activity.startActivityForResult(requestCode: Int) {
    startActivityForResult(IntentFor<T>(this), requestCode)
}
