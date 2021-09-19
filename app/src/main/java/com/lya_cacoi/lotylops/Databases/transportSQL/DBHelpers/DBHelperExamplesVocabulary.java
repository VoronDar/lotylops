package com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

public class DBHelperExamplesVocabulary extends SQLiteOpenHelper {
    public static final String NAME = "vcbex.db";
    public static final int VERSION = 1;

    public DBHelperExamplesVocabulary(@Nullable Context context) {
        super(context, transportPreferences.getNowLanguage(context) + NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlVocabularyContract.VocabularyEntry.CREATE_EXAMPLES_COMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SqlVocabularyContract.VocabularyEntry.DROP_EXAMPLES_TABLE);
        onCreate(db);
    }
}
