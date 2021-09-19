package com.lya_cacoi.lotylops.Databases.transportSQL;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo.Course;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperCourse;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperCourseCulture;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperCourseException;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperCourseGrammar;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperCoursePhonetic;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperCoursePhraseology;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.CULTURE_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.EXCEPTION_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract.COLUMN_ACCESS;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract.COLUMN_ACCESSIBILITY;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract.COLUMN_DIFFICULTY;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract.COLUMN_ENABLED;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract.COLUMN_ID;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract.COLUMN_MISTAKE_ID;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract.COLUMN_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract.COLUMN_VERSION;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract.CULTURE_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract.EXCEPTION_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract.GRAMMAR_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract.PHONETIC_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract.PHRASEOLOGY_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.CourseContract.VOCABULARY_TABLE_NAME;

public final class TransportSQLCourse {


    private SQLiteDatabase database;
    private static TransportSQLCourse transportSQLCourse;
    private Context context;
    private int DbIndex;


    /////// ЖИЗНЕННЫЙ ЦИКЛ /////////////////////////////////////////////////////////////////////////
    public static TransportSQLCourse getInstance(Context context, int index){
        if (transportSQLCourse == null)
            transportSQLCourse = new TransportSQLCourse(context, index);
        if (transportSQLCourse.getDbIndex() != index){
            transportSQLCourse.closeDb();
            transportSQLCourse = new TransportSQLCourse(context, index);
        }
        return transportSQLCourse;
    }

    private TransportSQLCourse(Context context, int index) {
        this.context = context;
        this.DbIndex = index;
        openDb();
    }

    public void openDb() {
        SQLiteOpenHelper dbHelper = getDbHelper();
        database = dbHelper.getWritableDatabase();
    }
    public void closeDb(){
        database.close();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    /////// ДЛЯ КОНТРОЛЯ ЗА РАЗНЫМИ РАЗДЕЛАМИ //////////////////////////////////////////////////////
    private SQLiteOpenHelper getDbHelper(){
        switch (DbIndex){
            case VOCABULARY_INDEX:
                return new DBHelperCourse(context);
            case GRAMMAR_INDEX:
                return new DBHelperCourseGrammar(context);
            case PHRASEOLOGY_INDEX:
                return new DBHelperCoursePhraseology(context);
            case EXCEPTION_INDEX:
                return new DBHelperCourseException(context);
            case CULTURE_INDEX:
                return new DBHelperCourseCulture(context);
            case PHONETIC_INDEX:
                return new DBHelperCoursePhonetic(context);
            default:
                return null;
        }
    }

    public int getDbIndex() {
        return DbIndex;
    }

    private String getTableName(){
        switch (DbIndex){
            case VOCABULARY_INDEX:
                return VOCABULARY_TABLE_NAME;
            case GRAMMAR_INDEX:
                return GRAMMAR_TABLE_NAME;
            case PHRASEOLOGY_INDEX:
                return PHRASEOLOGY_TABLE_NAME;
            case EXCEPTION_INDEX:
                return EXCEPTION_TABLE_NAME;
            case CULTURE_INDEX:
                return CULTURE_TABLE_NAME;
            case PHONETIC_INDEX:
                return PHONETIC_TABLE_NAME;
            default:
                return null;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    /////// ГЕТТЕРЫ ////////////////////////////////////////////////////////////////////////////////
    public Course getCourse(String id){
        Course course = null;

        boolean isOpen =  (database == null || !database.isOpen());
        if (isOpen)
            openDb();

        Cursor cursor = database.query(getTableName(),null,
                COLUMN_ID + " == ?", new String[]{id},
                null, null, null, "1");


        if (cursor.moveToNext()) {
            course = getCourse(cursor);
        }
        cursor.close();

        if (isOpen)
            closeDb();

        return course;
    }


    public Course isCourseInProgress(String id){
        Course course = null;

        Cursor cursor = database.query(getTableName(),null,
                COLUMN_ID + " == '" + id + "' AND " + COLUMN_ENABLED + " > '0'", null,
                null, null, null, "1");


        if (cursor.moveToNext()) {
            course = getCourse(cursor);
        }
        cursor.close();

        return course;
    }



    public ArrayList<Course> getAllCources(){
        if (database == null || !database.isOpen())
            openDb();

        Cursor cursor = database.query(getTableName(),null,
                null, null,
                null, null, COLUMN_DIFFICULTY);


        ArrayList<Course> cources = new ArrayList<>();
        while (cursor.moveToNext()) {
            cources.add(getCourse(cursor));
        }
        cursor.close();
        return cources;
    }

    public ArrayList<String> getAllCourcesName() {
        Cursor cursor = database.query(getTableName(),new String[]{COLUMN_NAME},
                null, null,
                null, null, COLUMN_DIFFICULTY);


        ArrayList<String> cources = new ArrayList<>();
        while (cursor.moveToNext()) {
            cources.add(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        }
        cursor.close();
        return cources;
    }

    public ArrayList<Course> getAllActivated(){

        Cursor cursor = database.query(getTableName(),null,
                COLUMN_ENABLED + " > ?", new String[]{"0"},
                null, null, COLUMN_DIFFICULTY);


        ArrayList<Course> cources = new ArrayList<>();
        while (cursor.moveToNext()) {
            cources.add(getCourse(cursor));
        }

        cursor.close();
        return cources;
    }

    private Course getCourse(Cursor cursor){
        String id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        int version = cursor.getInt(cursor.getColumnIndex(COLUMN_VERSION));
        int difficulty = cursor.getInt(cursor.getColumnIndex(COLUMN_DIFFICULTY));
        boolean access = cursor.getInt(cursor.getColumnIndex(COLUMN_ACCESS)) > 0 ;
        boolean enabled = cursor.getInt(cursor.getColumnIndex(COLUMN_ENABLED)) > 0 ;
        boolean accessibility = cursor.getInt(cursor.getColumnIndex(COLUMN_ACCESSIBILITY)) > 0 ;
        return new Course(id, version, access, enabled, difficulty, accessibility, name);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    /////// НАПОЛНИТЕЛИ ////////////////////////////////////////////////////////////////////////////
    private ContentValues fillContentValues(Course course){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, course.getId());
        contentValues.put(COLUMN_VERSION, course.getVersion());
        contentValues.put(COLUMN_DIFFICULTY, course.getDifficulty());
        contentValues.put(COLUMN_ENABLED, course.isEnabled());
        contentValues.put(COLUMN_ACCESS, course.isHasAccess());
        contentValues.put(COLUMN_ACCESSIBILITY, course.isAccessibility());
        contentValues.put(COLUMN_NAME, course.getName());
        return contentValues;
    }


    public void AddCourse(Course course){
        if (!course.isAccessibility())
            course.setHasAccess(false);
        database.insert(getTableName(), null, fillContentValues(course));
    }

    public void updateCourse(Course fromFb, Course fromSql) {
        Course put = fromSql;
        put.setVersion(fromFb.getVersion());
        put.setAccessibility(fromFb.isAccessibility());
        put.setDifficulty(fromFb.getDifficulty());
        put.setAccessibility(fromFb.isAccessibility());
        updateCourse(put);

    }


    public void updateCourse(Course course) {
        Log.i("main", course.toString());
        database.update(getTableName(), fillContentValues(course), COLUMN_ID + " == ?", new String[]{course.getId()});
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteDb() {
        database.delete(getTableName(), null, null);
    }

    public ArrayList<Course> getAllByDifficulty(int level) {
        if (database == null || !database.isOpen()){
            openDb();
        }


        Cursor cursor = database.query(getTableName(), null,
                COLUMN_DIFFICULTY + " == ?", new String[]{Integer.toString(level)},
                null, null, null);


        ArrayList<Course> cources = new ArrayList<>();
        while (cursor.moveToNext()) {
            cources.add(getCourse(cursor));
        }
        cursor.close();

        return cources;

    }

    // курсы грамматики, доступные для перехода по ним
    public ArrayList<Course> getAvailableGrammarCourse(){
        Cursor cursor = database.query(getTableName(), null,
                COLUMN_MISTAKE_ID + " IS NOT NULL", null, null,
                null, null, null);


        ArrayList<Course> cources = new ArrayList<>();
        while (cursor.moveToNext()) {
            cources.add(getCourse(cursor));
        }
        cursor.close();

        return cources;
    }

}
