package ru.rovkinmax.devvelatech.decorator

import ru.rovkinmax.devvelatech.model.UserMarker

interface MainView : LoadingView, ErrorView {

    fun showMarker(userMarker: UserMarker)

    fun showMarkerList(markerList: List<UserMarker>)
}