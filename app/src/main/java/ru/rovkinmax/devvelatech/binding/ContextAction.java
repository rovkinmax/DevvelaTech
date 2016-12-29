package ru.rovkinmax.devvelatech.binding;

import android.content.Context;

public interface ContextAction {
    void onAction(Context context, int actionId, Object model);
}
