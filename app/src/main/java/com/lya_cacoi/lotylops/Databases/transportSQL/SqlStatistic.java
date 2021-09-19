package com.lya_cacoi.lotylops.Databases.transportSQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperStatistic;
import com.lya_cacoi.lotylops.Statistics.OneDayInfo;

import java.util.ArrayList;
import java.util.List;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlStatisticContract.DatabaseEntry.COLUMN_DAY;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlStatisticContract.DatabaseEntry.COLUMN_FORGOTTEN;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlStatisticContract.DatabaseEntry.COLUMN_PACE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlStatisticContract.DatabaseEntry.COLUMN_REMEMBERED;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlStatisticContract.DatabaseEntry.COLUMN_STUDIED;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlStatisticContract.DatabaseEntry.COLUMN_TIME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlStatisticContract.DatabaseEntry.TABLE_NAME;


/*

база данных для статистики (общая для всех - только дб хелперы разные)

 */

public final class SqlStatistic{
    private static SqlStatistic sqlStatistic;
    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;
    private int index = -1;


    //////// LIFE CYCLE ////////////////////////////////////////////////////////////////////////////
    public static SqlStatistic getInstance(Context context, int index){
        if (sqlStatistic == null)
            sqlStatistic = new SqlStatistic(context, index);
        if (sqlStatistic.index != index){
            sqlStatistic.closeDatabases();
            sqlStatistic = new SqlStatistic(context, index);
        }
        return sqlStatistic;
    }
    private SqlStatistic(Context context, int index) {
        this.context = context;
        this.index = index;
        }
    public void openDbForUpdate(){
        dbHelper          = getDbHelper();
        database          = dbHelper.getWritableDatabase();
        }
    public void openDbForWatch(){
        dbHelper          = getDbHelper();
        database          = dbHelper.getReadableDatabase();
    }
    public void closeDatabases(){
        database.close();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////// ДЛЯ СВЯЗИ С ОСНОВНОЙ ////////////////////////////////////////////////////////////////////
    protected String getTableName() {
        return TABLE_NAME;
    }

    protected SQLiteOpenHelper getDbHelper() {
        switch (index){
            case VOCABULARY_INDEX:
                return new DBHelperStatistic(context);
                default:
                    return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////// ДОБАВЛЕНИЕ КАРТЫ ////////////////////////////////////////////////////////////////////////
    public void addNew(OneDayInfo card){
        database.insert(getTableName(), null, fillContentValues(card));
    }

    private ContentValues fillContentValues(OneDayInfo card){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DAY, card.getDay());
        contentValues.put(COLUMN_PACE, card.getPace());
        contentValues.put(COLUMN_TIME, card.getTime());
        contentValues.put(COLUMN_REMEMBERED, card.getRemember_count());
        contentValues.put(COLUMN_STUDIED, card.getStudied_count());
        contentValues.put(COLUMN_FORGOTTEN, card.getForget_count());
        return contentValues;
        }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    //////ПОЛУЧЕНИЕ КАРТЫ///////////////////////////////////////////////////////////////////////////

    public static final int pickCount = 7;
    public List<OneDayInfo> getLastSeven(){
        List<OneDayInfo> oneDayInfos = new ArrayList<>();

        Cursor cursor = database.query(getTableName(), null, null,
               null,
                null, null, COLUMN_DAY + " DESC");

        int count = pickCount;

        while (cursor.moveToNext() && count-- > 0) {
            oneDayInfos.add(getFromCursor(cursor));
            Log.i("main", oneDayInfos.get(oneDayInfos.size()-1).toString());
        }
        return oneDayInfos;
    }

    public List<OneDayInfo> getSecondLastSeven(){
        List<OneDayInfo> oneDayInfos = new ArrayList<>();

        Cursor cursor = database.query(getTableName(), null, null,
                null,
                null, null, COLUMN_DAY + " DESC");

        int count = pickCount;

        while (cursor.moveToNext() && count-- > 0);
        while (cursor.moveToNext() && count++ < pickCount) {
            oneDayInfos.add(getFromCursor(cursor));
            Log.i("main", oneDayInfos.get(oneDayInfos.size()-1).toString());
        }
        return oneDayInfos;
    }

    private OneDayInfo getFromCursor(Cursor cursor){
         return new OneDayInfo(cursor.getInt(cursor.getColumnIndex(COLUMN_DAY)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_REMEMBERED)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_FORGOTTEN)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_STUDIED)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_PACE)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_TIME)));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    public void deleteDB(){
        database.delete(getTableName(), null, null);
    }
}