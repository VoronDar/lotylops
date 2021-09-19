package com.lya_cacoi.lotylops.Databases.Union;

import android.content.Context;

public interface TransportRunnable {
    void onSomethingGoesWrong(int messageId);
    void onUserChangesWillOverWrite();
    void onSuccess();
    void onCourseStatChange(int index);             // возвращает курс, состояние которого изменилось
    void getMessage(String message);
    Context getContext();
    enum ActionTypes{
        error,
        checkOverWrite,
        sendMessage,
        updateIndex,
        nowCourseWontBeUpdated
    }
}
