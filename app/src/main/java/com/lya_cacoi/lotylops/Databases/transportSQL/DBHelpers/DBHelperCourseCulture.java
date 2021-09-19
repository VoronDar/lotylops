package com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

public class DBHelperCourseCulture extends SQLiteOpenHelper {

    private static final String NAME = "coursecul.db";
    private static final int VERSION = 2;

    public DBHelperCourseCulture(@Nullable Context context) {
        super(context, transportPreferences.getNowLanguage(context) + NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CourseContract.CREATE_CULTURE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CourseContract.DROP_CULTURE_TABLE);
        onCreate(db);
    }
}
