package ru.rovkinmax.devvelatech.binding

import android.content.Context
import android.databinding.BindingAdapter
import android.view.View
import java.util.*

@BindingAdapter("clickAction", "actionHandler", "model", requireAll = false)
fun bindClickAction(view: View, clickAction: Int, actionHandler: ActionHandler, model: Any?) {
    view.setOnClickListener { view -> actionHandler.fireAction(view.context, clickAction, model) }
}

@BindingAdapter("action", "actionHandler", "model", requireAll = false)
fun bindAction(view: View, action: Int, actionHandler: ActionHandler, model: Any?) {
    actionHandler.fireAction(view.context, action, model)
}

class ActionHandler {
    private val actionMap = HashMap<Int, ContextAction>()

    fun addAction(actionId: Int, contextAction: ContextAction): ActionHandler {
        actionMap.put(actionId, contextAction)
        return this
    }

    fun fireAction(context: Context, actionId: Int, model: Any? = null) {
        actionMap[actionId]?.onAction(context, actionId, model)
    }
}
