package com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

public class DBHelperStaticVocabulary  extends SQLiteOpenHelper {
    public static final String NAME = "vcbst.db";
    public static final int VERSION = 2;

    public DBHelperStaticVocabulary(@Nullable Context context) {
        super(context, transportPreferences.getNowLanguage(context) + NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlVocabularyContract.VocabularyEntry.CREATE_STATIC_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SqlVocabularyContract.VocabularyEntry.DROP_STATIC_TABLE);
        onCreate(db);
    }
}
