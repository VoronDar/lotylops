package com.lya_cacoi.lotylops.activities.statistics.command;

import android.content.Context;

public interface Command {
    void action(Context context);
    void drop(Context context);
    String getName();

}
