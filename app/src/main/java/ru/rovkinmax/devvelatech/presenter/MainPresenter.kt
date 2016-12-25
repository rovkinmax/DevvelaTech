package ru.rovkinmax.devvelatech.presenter

import com.google.android.gms.maps.model.LatLng
import io.reactivex.functions.Consumer
import ru.rovkinmax.devvelatech.model.UserMarker
import ru.rovkinmax.devvelatech.repository.MarkerRepository
import ru.rovkinmax.devvelatech.rx.RxDecor
import ru.rovkinmax.devvelatech.util.async
import ru.rovkinmax.devvelatech.view.MainView
import ru.rovkinmax.ligataxi.rx.LifecycleProvider
import java.util.*

class MainPresenter(private val view: MainView, private val lifecycleProvider: LifecycleProvider) {
    fun createMarker(position: LatLng, name: String) {
        val marker = buildUserMarker(name, position)
        MarkerRepository.saveMarker(marker)
                .async()
                .compose(lifecycleProvider.lifecycle())
                .compose(RxDecor.loading(view))
                .subscribe(Consumer { view.showMarker(it) }, RxDecor.error(view))
    }

    private fun buildUserMarker(name: String, position: LatLng): UserMarker {
        val marker = UserMarker()
        marker.latitude = position.latitude
        marker.longitude = position.longitude
        marker.name = name
        marker.id = UUID.randomUUID().toString()
        return marker
    }

    fun loadAllMarker() {
        MarkerRepository.loadMarkerList()
                .async()
                .compose(lifecycleProvider.lifecycle())
                .compose(RxDecor.loading(view))
                .subscribe(Consumer { view.showMarkerList(it) }, RxDecor.error(view))
    }

}