package com.lya_cacoi.lotylops.Databases.transportSQL.contracts;

import android.provider.BaseColumns;

public abstract class Entry implements BaseColumns {

    public static final String COLUMN_REPEAT_LEVEL              = "repeat";
    public static final String COLUMN_PRACTICE_LEVEL            = "practice";
    public static final String COLUMN_REPETITION_DAY            = "day";

    public static final String TYPE_TEXT                        = "TEXT";
    public static final String TYPE_INTEGER                     = "INTEGER";
    public static final String TYPE_BOOL                        = "BIT";
}