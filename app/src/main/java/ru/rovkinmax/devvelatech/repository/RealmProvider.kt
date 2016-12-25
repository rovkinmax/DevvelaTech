package ru.rovkinmax.devvelatech.repository

import io.reactivex.Single
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmResults
import timber.log.Timber
import java.util.*

object RealmProvider {

    fun provideRealm(): Realm {
        return Realm.getDefaultInstance()
    }

    fun <T : RealmModel> queryFromRealm(func: (Realm) -> T?): Single<T> {
        return Single.defer<T> {
            val realm = provideRealm()
            try {
                realm.beginTransaction()
                val result = func(realm)
                var resultCloned: T? = null
                if (RealmObject.isValid(result) && result != null)
                    resultCloned = realm.copyFromRealm(result)
                realm.commitTransaction()

                if (resultCloned == null) {
                    return@defer Single.error(NoSuchElementException())
                } else
                    return@defer Single.just <T>(resultCloned)
            } catch (e: Exception) {
                realm.cancelTransaction()
                Single.error<T>(Exception("Something went wrong", e))
            } finally {
                realm.close()
            }
        }
    }

    fun <T : RealmModel> queryListFromRealm(func: (Realm) -> RealmResults<T>): Single<List<T>> {
        return Single.defer<List<T>> {
            val realm = provideRealm()
            try {
                realm.beginTransaction()
                val result = func(realm)
                val resultCloned = result.map { realm.copyFromRealm(it) }
                realm.commitTransaction()
                return@defer Single.just<List<T>>(resultCloned)
            } catch (e: Exception) {
                realm.cancelTransaction()
                Single.error<List<T>>(Exception("Something went wrong", e))
            } finally {
                realm.close()
            }
        }
    }
}

fun <T : RealmModel> Single<T>.queryFromRealm(func: (T, Realm) -> T?): Single<T> {
    return flatMap { result -> RealmProvider.queryFromRealm { realm -> func(result, realm) } }
}

fun Realm.executeClosableTransaction(transaction: (Realm) -> Unit): Unit {
    try {
        beginTransaction()
        transaction(this)
        commitTransaction()
    } catch(e: Exception) {
        Timber.tag(this.javaClass.simpleName).d(e, "Can't execute transaction")
        cancelTransaction()
    } finally {
        close()
    }

}