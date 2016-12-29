package ru.rovkinmax.devvelatech.activity

import android.app.Activity
import android.databinding.DataBindingUtil
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
import ru.rovkinmax.devvelatech.binding.ActionHandler
import ru.rovkinmax.devvelatech.binding.ContextAction
import ru.rovkinmax.devvelatech.databinding.AcPointListBinding
import ru.rovkinmax.devvelatech.decorator.ErrorLoadingDecorator
import ru.rovkinmax.devvelatech.decorator.PointListView
import ru.rovkinmax.devvelatech.dialog.LoadingDialog
import ru.rovkinmax.devvelatech.model.UserMarker
import ru.rovkinmax.devvelatech.presenter.PointListPresenter
import ru.rovkinmax.devvelatech.rx.RxError
import ru.rovkinmax.ligataxi.rx.LifecycleProvider
import java.util.*

class PointListActivity : AppCompatActivity(), PointListView {

    private val errorView by lazy { RxError.view(fragmentManager) }
    private val loadingView by lazy { LoadingDialog.view(fragmentManager) }
    private val adapter = MarkerAdapter()

    private var actionHandler: ActionHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<AcPointListBinding>(this, R.layout.ac_point_list)
        setSupportActionBar(toolbar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        binding.viewModel = PointListPresenter(ErrorLoadingDecorator(fragmentManager), LifecycleProvider.get(fragmentManager))
        binding.viewModel.actionHandler.addAction(R.id.action_delete, ContextAction { context, actionId, model ->
            setResult(Activity.RESULT_OK)
        })
        actionHandler = binding.viewModel.actionHandler
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

    }

    override fun onBackPressed() {
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        actionHandler?.fireAction(this, item.itemId)
        return true
    }

    override fun showEmptyStub() {
        emptyStub.visibility = View.VISIBLE
    }

    override fun hideEmptyStub() {
        emptyStub.visibility = View.GONE
    }
}