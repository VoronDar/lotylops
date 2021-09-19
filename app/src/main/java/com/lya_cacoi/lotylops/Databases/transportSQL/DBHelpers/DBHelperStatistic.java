package com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlStatisticContract.DatabaseEntry.CREATE_TABLE_COMMAND;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlStatisticContract.DatabaseEntry.DROP_TABLE;

public class DBHelperStatistic extends SQLiteOpenHelper {
    public static final String NAME = "statvoc.db";
    public static final int VERSION = 1;

    public DBHelperStatistic(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
