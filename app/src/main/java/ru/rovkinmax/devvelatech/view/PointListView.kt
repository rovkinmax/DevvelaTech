package ru.rovkinmax.devvelatech.view

import ru.rovkinmax.devvelatech.model.UserMarker

interface PointListView : LoadingView, ErrorView, EmptyView {
    fun showMarkerList(markerList: List<UserMarker>)
    fun deleteMarker(marker: UserMarker)
}