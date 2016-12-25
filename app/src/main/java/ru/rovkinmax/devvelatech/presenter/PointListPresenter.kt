package ru.rovkinmax.devvelatech.presenter

import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import ru.rovkinmax.devvelatech.model.UserMarker
import ru.rovkinmax.devvelatech.repository.MarkerRepository
import ru.rovkinmax.devvelatech.rx.RxDecor
import ru.rovkinmax.devvelatech.util.async
import ru.rovkinmax.devvelatech.view.PointListView
import ru.rovkinmax.ligataxi.rx.LifecycleProvider
import java.util.*

class PointListPresenter(private val view: PointListView, private val provider: LifecycleProvider) {

    fun loadData() {
        MarkerRepository.loadMarkerList()
                .async()
                .map { ArrayList(it) }
                .compose(provider.lifecycle())
                .compose(RxDecor.loading(view))
                .subscribe(Consumer { dispatchList(it) }, RxDecor.error(view))
    }

    private fun dispatchList(pointList: MutableList<UserMarker>) {
        if (pointList.isEmpty())
            view.showEmptyStub()
        else {
            view.hideEmptyStub()
            sortAscending(pointList)
        }
    }

    fun deleteMarker(marker: UserMarker) {
        MarkerRepository.deleteMarker(marker)
                .async()
                .compose(provider.lifecycle<Any>())
                .compose(RxDecor.loading<Any>(view))
                .subscribe(Action { view.deleteMarker(marker) }, RxDecor.error(view))
    }

    fun sortAscending(markerList: MutableList<UserMarker>) {
        markerList.sortBy { it.name }
        view.showMarkerList(markerList)
    }

    fun sortDescending(markerList: MutableList<UserMarker>) {
        markerList.sortByDescending { it.name }
        view.showMarkerList(markerList)
    }
}