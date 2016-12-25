package ru.rovkinmax.devvelatech.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.ac_point_list.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.rovkinmax.devvelatech.R
import ru.rovkinmax.devvelatech.adapter.MarkerAdapter
import ru.rovkinmax.devvelatech.dialog.LoadingDialog
import ru.rovkinmax.devvelatech.model.UserMarker
import ru.rovkinmax.devvelatech.presenter.PointListPresenter
import ru.rovkinmax.devvelatech.rx.RxError
import ru.rovkinmax.devvelatech.view.PointListView
import ru.rovkinmax.ligataxi.rx.LifecycleProvider
import java.util.*

class PointListActivity : AppCompatActivity(), PointListView {

    private lateinit var presenter: PointListPresenter
    private val errorView by lazy { RxError.view(fragmentManager) }
    private val loadingView by lazy { LoadingDialog.view(fragmentManager) }
    private val adapter = MarkerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_point_list)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        adapter.listener = { marker -> presenter.deleteMarker(marker) }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        presenter = PointListPresenter(this, LifecycleProvider.get(fragmentManager))
        presenter.loadData()
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

    override fun showMarkerList(markerList: List<UserMarker>) {
        adapter.markerList = ArrayList(markerList)
        adapter.notifyDataSetChanged()
    }

    override fun deleteMarker(marker: UserMarker) {
        val position = adapter.markerList.indexOf(marker)
        adapter.markerList.removeAt(position)
        adapter.notifyItemRemoved(position)
        if (adapter.markerList.isEmpty())
            showEmptyStub()
        setResult(Activity.RESULT_OK)
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_asc -> {
                presenter.sortAscending(adapter.markerList)
            }
            R.id.menu_desc -> {
                presenter.sortDescending(adapter.markerList)
            }
        }
        return true
    }

    override fun showEmptyStub() {
        emptyStub.visibility = View.VISIBLE
    }

    override fun hideEmptyStub() {
        emptyStub.visibility = View.GONE
    }
}