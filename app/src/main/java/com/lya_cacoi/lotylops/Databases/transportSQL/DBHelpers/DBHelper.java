package com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.Databases.transportSQL.contracts.VocabularyContract;

public class DBHelper extends SQLiteOpenHelper {

    public static final String NAME = "vocabularyRUS.db";
    public static final int VERSION = 3;

    public DBHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(VocabularyContract.VocabularyEntry.CREATE_COMMON_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(VocabularyContract.VocabularyEntry.DROP_TABLE);
        onCreate(db);
    }
}
