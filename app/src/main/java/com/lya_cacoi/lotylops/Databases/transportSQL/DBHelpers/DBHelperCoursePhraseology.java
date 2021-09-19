package com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

public class DBHelperCoursePhraseology extends SQLiteOpenHelper {

    private static final String NAME = "courseph.db";
    private static final int VERSION = 2;

    public DBHelperCoursePhraseology(@Nullable Context context) {
        super(context, transportPreferences.getNowLanguage(context) +  NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CourseContract.CREATE_PHRASEOLOGY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CourseContract.DROP_PHRASEOLOGY_TABLE);
        onCreate(db);
    }
}
