package ru.rovkinmax.devvelatech.presenter

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import io.reactivex.Single
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import ru.rovkinmax.devvelatech.R
import ru.rovkinmax.devvelatech.adapter.MarkerAdapter
import ru.rovkinmax.devvelatech.binding.ActionHandler
import ru.rovkinmax.devvelatech.binding.ContextAction
import ru.rovkinmax.devvelatech.decorator.ErrorLoadingDecorator
import ru.rovkinmax.devvelatech.model.UserMarker
import ru.rovkinmax.devvelatech.repository.MarkerRepository
import ru.rovkinmax.devvelatech.rx.RxDecor
import ru.rovkinmax.devvelatech.util.async
import ru.rovkinmax.ligataxi.rx.LifecycleProvider

@BindingAdapter("markerList", "actionHandler")
fun bindMarkerList(recyclerView: RecyclerView, markerList: MutableList<UserMarker>, actionHandler: ActionHandler) {
    var adapter: MarkerAdapter? = recyclerView.adapter as MarkerAdapter?
    if (adapter == null) {
        adapter = MarkerAdapter(actionHandler)
        recyclerView.adapter = adapter
    }
    adapter.markerList = markerList
    adapter.notifyDataSetChanged()
}


class PointListPresenter(private val decorator: ErrorLoadingDecorator, private val provider: LifecycleProvider) : ContextAction {
    val markerList = ObservableArrayList<UserMarker>()

    val actionHandler = ActionHandler().apply {
        addAction(R.id.action_delete, this@PointListPresenter)
        addAction(R.id.menu_asc, this@PointListPresenter)
        addAction(R.id.menu_desc, this@PointListPresenter)
        addAction(R.id.action_load_data, this@PointListPresenter)
    }

    fun loadData() {
        MarkerRepository.loadMarkerList()
                .flatMap { list -> Thread.sleep(3000);Single.just(list) }
                .async()
                .compose(provider.lifecycle())
                .compose(RxDecor.loading(decorator))
                .subscribe(Consumer { it -> markerList.addAll(it) }, RxDecor.error(decorator))
    }

    override fun onAction(context: Context, actionId: Int, model: Any?) {
        when (actionId) {
            R.id.action_delete -> deleteMarker(model as UserMarker)
            R.id.menu_asc -> markerList.sortBy { it.name }
            R.id.menu_desc -> markerList.sortByDescending { it.name }
            R.id.action_load_data -> loadData()
        }
    }

    fun deleteMarker(marker: UserMarker) {
        MarkerRepository.deleteMarker(marker)
                .async()
                .compose(provider.lifecycle<Any>())
                .compose(RxDecor.loading<Any>(decorator))
                .subscribe(Action { markerList.remove(marker) }, RxDecor.error(decorator))
    }
}