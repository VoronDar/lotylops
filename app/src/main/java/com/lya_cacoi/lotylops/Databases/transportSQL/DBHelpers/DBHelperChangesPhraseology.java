package com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

public class DBHelperChangesPhraseology extends SQLiteOpenHelper {
    public static final String NAME = "phrch.db";
    public static final int VERSION = 1;

    public DBHelperChangesPhraseology(@Nullable Context context) {
        super(context, transportPreferences.getNowLanguage(context) + NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlVocabularyContract.VocabularyEntry.CREATE_PHRASEOLOGY_CHANGES_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SqlVocabularyContract.VocabularyEntry.DROP_PHRASEOLOGY_CHANGES_TABLE);
        onCreate(db);
    }
}
