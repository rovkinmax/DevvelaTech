package ru.rovkinmax.devvelatech.model

import io.realm.RealmModel
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class UserMarker : RealmModel {
    @PrimaryKey
    open var id = ""
    open var name = ""
    open var latitude = 0.0
    open var longitude = 0.0
}
