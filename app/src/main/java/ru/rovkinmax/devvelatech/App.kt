package ru.rovkinmax.devvelatech

import android.app.Application
import io.realm.Realm

open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        Lifecycler.register(this)
    }
}