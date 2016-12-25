package ru.rovkinmax.devvelatech.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import ru.rovkinmax.devvelatech.R
import ru.rovkinmax.devvelatech.model.UserMarker
import java.util.*

class MarkerAdapter(var listener: ((UserMarker) -> Unit)? = null) : RecyclerView.Adapter<MarkerHolder>() {

    var markerList: MutableList<UserMarker> = Collections.emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarkerHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MarkerHolder(inflater.inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: MarkerHolder?, position: Int) {
        val marker = markerList[position]
        holder?.btnDelete?.setOnClickListener { listener?.invoke(marker) }
        holder?.bindView(marker)
    }

    override fun getItemCount(): Int = markerList.size
}

class MarkerHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val name by lazy { itemView.findViewById(R.id.tvName) as TextView }
    val btnDelete by lazy { itemView.findViewById(R.id.btnDelete) as Button }

    fun bindView(marker: UserMarker) {
        name.text = marker.name
    }
}