package ru.rovkinmax.devvelatech.adapter

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import ru.rovkinmax.devvelatech.R
import ru.rovkinmax.devvelatech.binding.ActionHandler
import ru.rovkinmax.devvelatech.databinding.ItemUserBinding
import ru.rovkinmax.devvelatech.model.UserMarker
import java.util.*

class MarkerAdapter(var actionHandler: ActionHandler? = null) : RecyclerView.Adapter<MarkerHolder>() {

    var markerList: MutableList<UserMarker> = Collections.emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemUserBinding>(inflater, R.layout.item_user, parent, false)

        return MarkerHolder(binding)
    }

    override fun onBindViewHolder(holder: MarkerHolder?, position: Int) {
        val marker = markerList[position]
        holder?.binding?.actionHandler = actionHandler
        holder?.bindView(marker)
    }

    override fun getItemCount(): Int = markerList.size
}

class MarkerHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {

    private val name by lazy { itemView.findViewById(R.id.tvName) as TextView }
    val btnDelete by lazy { itemView.findViewById(R.id.btnDelete) as Button }

    fun bindView(marker: UserMarker) {
        binding.marker = marker
    }
}