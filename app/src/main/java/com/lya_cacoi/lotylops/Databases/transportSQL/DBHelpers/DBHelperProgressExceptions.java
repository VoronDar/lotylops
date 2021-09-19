package com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.CREATE_EXCEPTION_PROGRESS_COMAND;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.DROP_EXCEPTION_PROGRESS_TABLE;

public class DBHelperProgressExceptions extends SQLiteOpenHelper {
    public static final String NAME = "expr.db";
    public static final int VERSION = 1;

    public DBHelperProgressExceptions(@Nullable Context context) {
        super(context, transportPreferences.getNowLanguage(context) + NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EXCEPTION_PROGRESS_COMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_EXCEPTION_PROGRESS_TABLE);
        onCreate(db);
    }
}
