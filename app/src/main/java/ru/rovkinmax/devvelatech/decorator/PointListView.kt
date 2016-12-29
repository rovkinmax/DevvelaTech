package ru.rovkinmax.devvelatech.decorator

import ru.rovkinmax.devvelatech.model.UserMarker

interface PointListView : LoadingView, ErrorView, EmptyView {
    fun showMarkerList(markerList: List<UserMarker>)
    fun deleteMarker(marker: UserMarker)
}