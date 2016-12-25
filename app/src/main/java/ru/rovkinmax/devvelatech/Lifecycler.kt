package ru.rovkinmax.devvelatech

import android.app.Activity
import android.app.Application
import android.os.Bundle
import ru.rovkinmax.ligataxi.rx.BundleFragment


@SuppressWarnings("squid:S1186")
class Lifecycler : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        initLifecycleProvider(activity)
    }

    private fun initLifecycleProvider(activity: Activity) {
        val fm = activity.fragmentManager
        if (fm.findFragmentByTag(BundleFragment::class.java.name) == null) {
            fm.beginTransaction().add(BundleFragment(), BundleFragment::class.java.name).commitAllowingStateLoss()
            fm.executePendingTransactions()
        }
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

    companion object {
        fun register(app: Application) {
            app.registerActivityLifecycleCallbacks(Lifecycler())
        }
    }

}