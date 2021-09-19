package com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlCultureContract.GrammarEntry.CREATE_PROGRESS_COMAND;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlCultureContract.GrammarEntry.DROP_PROGRESS_TABLE;

public class DBHelperProgressCulture extends SQLiteOpenHelper {
    public static final String NAME = "culpr.db";
    public static final int VERSION = 1;

    public DBHelperProgressCulture(@Nullable Context context) {
        super(context, transportPreferences.getNowLanguage(context) + NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROGRESS_COMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_PROGRESS_TABLE);
        onCreate(db);
    }
}
