package ru.rovkinmax.devvelatech.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.rovkinmax.devvelatech.R
import ru.rovkinmax.devvelatech.dialog.LoadingDialog
import ru.rovkinmax.devvelatech.dialog.NameInputDialog
import ru.rovkinmax.devvelatech.model.UserMarker
import ru.rovkinmax.devvelatech.presenter.MainPresenter
import ru.rovkinmax.devvelatech.rx.RxError
import ru.rovkinmax.devvelatech.util.startActivityForResult
import ru.rovkinmax.devvelatech.view.MainView
import ru.rovkinmax.ligataxi.rx.LifecycleProvider

class MainActivity : AppCompatActivity(), MainView {
    companion object {
        private const val REQUEST_CODE_LIST = 1
    }

    private val errorView by lazy { RxError.view(fragmentManager) }
    private val loadingView by lazy { LoadingDialog.view(fragmentManager) }

    private var presenter: MainPresenter? = null
    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        presenter = MainPresenter(this, LifecycleProvider.get(fragmentManager))
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            googleMap = map
            setUpMap(map)
            presenter?.loadAllMarker()
        }
    }

    private fun setUpMap(map: GoogleMap) {
        map.setOnMapLongClickListener { position ->
            NameInputDialog.show(fragmentManager) { name ->
                presenter?.createMarker(position, name)
            }
        }
    }

    override fun showMarkerList(markerList: List<UserMarker>) {
        googleMap?.clear()
        markerList.forEach { showMarker(it) }
    }

    override fun showMarker(userMarker: UserMarker) {
        googleMap?.addMarker(MarkerOptions()
                .position(LatLng(userMarker.latitude, userMarker.longitude))
                .icon(BitmapDescriptorFactory.defaultMarker())
                .title(userMarker.name))
    }

    override fun showLoadingIndicator() {
        loadingView.showLoadingIndicator()
    }

    override fun hideLoadingIndicator() {
        loadingView.hideLoadingIndicator()
    }

    override fun showErrorMessage(message: String, needCallback: Boolean) {
        errorView.showErrorMessage(message, needCallback)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuItemList) {
            startActivityForResult<PointListActivity>(REQUEST_CODE_LIST)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LIST && resultCode == Activity.RESULT_OK)
            presenter?.loadAllMarker()
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        mapView.onLowMemory()
        super.onLowMemory()
    }
}