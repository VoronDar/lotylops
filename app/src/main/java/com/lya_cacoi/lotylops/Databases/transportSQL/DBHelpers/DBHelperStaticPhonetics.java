package com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlPhoneticContract.PhoneticEntry.CREATE_STATIC_COMMAND;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlPhoneticContract.PhoneticEntry.DROP_STATIC_TABLE;

public class DBHelperStaticPhonetics extends SQLiteOpenHelper {
    public static final String NAME = "phonst.db";
    public static final int VERSION = 4;

    public DBHelperStaticPhonetics(@Nullable Context context) {
        super(context, transportPreferences.getNowLanguage(context) + NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STATIC_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_STATIC_TABLE);
        onCreate(db);
    }
}
