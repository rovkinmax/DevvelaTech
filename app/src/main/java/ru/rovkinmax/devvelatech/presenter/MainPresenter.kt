package ru.rovkinmax.devvelatech.presenter

import android.app.Fragment
import android.app.FragmentManager
import android.databinding.BindingAdapter
import android.databinding.ObservableArrayList
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.functions.Consumer
import ru.rovkinmax.devvelatech.dialog.LoadingDialog
import ru.rovkinmax.devvelatech.dialog.NameInputDialog
import ru.rovkinmax.devvelatech.model.UserMarker
import ru.rovkinmax.devvelatech.repository.MarkerRepository
import ru.rovkinmax.devvelatech.rx.RxDecor
import ru.rovkinmax.devvelatech.rx.RxError
import ru.rovkinmax.devvelatech.util.async
import ru.rovkinmax.ligataxi.rx.BundleFragment
import java.util.*

@BindingAdapter("markerList")
fun bindMarkerList(mapView: MapView, list: List<UserMarker>) {
    mapView.getMapAsync { googleMap ->
        googleMap.clear()
        list.forEach { addMarker(googleMap, it) }
    }
}

private fun addMarker(map: GoogleMap, marker: UserMarker): Unit {
    map.addMarker(MarkerOptions()
            .position(LatLng(marker.latitude, marker.longitude))
            .icon(BitmapDescriptorFactory.defaultMarker())
            .title(marker.name))
}

@BindingAdapter("onMapReady")
fun bindOnMapReady(mapView: MapView, callback: OnMapReadyCallback) {
    mapView.getMapAsync(callback)
}

class MainPresenter() : BundleFragment() {

    private val errorView by lazy { RxError.view(fragmentManager) }
    private val loadingView by lazy { LoadingDialog.view(fragmentManager) }

    companion object {
        fun create(fm: FragmentManager): MainPresenter {
            val tag = MainPresenter::class.java.simpleName
            var viewModel: Fragment? = fm.findFragmentByTag(tag)
            if (viewModel == null) {
                viewModel = MainPresenter()
                fm.beginTransaction().add(viewModel, tag).commitAllowingStateLoss()
            }
            return viewModel as MainPresenter
        }
    }

    val onMapReadyCallback: OnMapReadyCallback = OnMapReadyCallback { map ->
        loadAllMarker()
        map.setOnMapLongClickListener { position ->
            NameInputDialog.show(fragmentManager) { name -> createMarker(position, name) }
        }
    }

    val markerList = ObservableArrayList<UserMarker>()

    fun createMarker(position: LatLng, name: String) {
        val marker = buildUserMarker(name, position)
        MarkerRepository.saveMarker(marker)
                .async()
                .compose(provider.lifecycle())
                .compose(RxDecor.loading(loadingView))
                .subscribe(Consumer { markerList.add(it) }, RxDecor.error(errorView))
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
                .compose(provider.lifecycle())
                .compose(RxDecor.loading(loadingView))
                .subscribe(Consumer { it ->
                    markerList.addAll(it)
                }, RxDecor.error(errorView))
    }
}