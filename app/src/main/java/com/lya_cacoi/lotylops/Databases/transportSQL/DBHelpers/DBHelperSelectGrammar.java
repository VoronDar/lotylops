package com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.CREATE_SELECT_COMMAND;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.DROP_SELECT_TABLE;

public class DBHelperSelectGrammar extends SQLiteOpenHelper {
    public static final String NAME = "grsl.db";
    public static final int VERSION = 1;

    public DBHelperSelectGrammar(@Nullable Context context) {
        super(context, transportPreferences.getNowLanguage(context) + NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SELECT_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_SELECT_TABLE);
        onCreate(db);
    }
}
