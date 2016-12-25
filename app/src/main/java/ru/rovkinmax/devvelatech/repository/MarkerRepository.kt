package ru.rovkinmax.devvelatech.repository

import io.reactivex.Completable
import io.reactivex.Single
import io.realm.RealmObject
import ru.rovkinmax.devvelatech.model.UserMarker

object MarkerRepository {
    fun saveMarker(userMarker: UserMarker): Single<UserMarker> {
        return RealmProvider.queryFromRealm { realm -> realm.copyToRealm(userMarker) }
    }

    fun loadMarkerList(): Single<List<UserMarker>> {
        return RealmProvider.queryListFromRealm { realm -> realm.where(UserMarker::class.java).findAll() }
    }

    fun deleteMarker(marker: UserMarker): Completable {
        return Completable.create { emitter ->
            RealmProvider.provideRealm().executeClosableTransaction { realm ->
                val tempt = realm.where(UserMarker::class.java).equalTo("id", marker.id).findFirst()
                tempt?.let { RealmObject.deleteFromRealm(tempt) }
            }
            emitter.onComplete()
        }
    }
}