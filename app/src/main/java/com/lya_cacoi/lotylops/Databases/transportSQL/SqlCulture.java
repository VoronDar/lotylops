package com.lya_cacoi.lotylops.Databases.transportSQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperProgressCulture;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperSelectGrammar;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperStaticCulture;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.CultureCard;
import com.lya_cacoi.lotylops.cards.GrammarCard;
import com.lya_cacoi.lotylops.ruler.Ruler;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static android.provider.BaseColumns._ID;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.CULTURE_INDEX;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.Entry.COLUMN_REPETITION_DAY;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlCultureContract.GrammarEntry.PROGRESS_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlCultureContract.GrammarEntry.STATIC_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_ANSWER;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_FOURTH;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_RIGHT;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_SECOND;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_THEORY;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_THIRD;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.SELECT_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_COURSE;
import static com.lya_cacoi.lotylops.declaration.consts.NOUN_LEVEL;


/*

сосбвтенно для подключения ДБ SQL

 */

public final class SqlCulture extends SqlMain {
    private SQLiteDatabase databaseSelect;
    private SQLiteOpenHelper dbHelperSelect;
    private static SqlCulture sqlCulture;
    private static String introStudy;
    private static String introRepeat;


    //////// LIFE CYCLE ////////////////////////////////////////////////////////////////////////////
    public static SqlCulture getInstance(Context context){
        if (sqlCulture == null)
            sqlCulture = new SqlCulture(context);
        return sqlCulture;
    }
    private SqlCulture(Context context) {
            super(context);
        index = CULTURE_INDEX;
        }
    public void openDbForUpdate(){
        dbHelperStatic          = new DBHelperStaticCulture(context);
        databaseStatic          = dbHelperStatic.getWritableDatabase();
        dbHelperProgress        = new DBHelperProgressCulture(context);
        databaseProgress        = dbHelperProgress.getWritableDatabase();
        dbHelperSelect          = new DBHelperSelectGrammar(context);
        databaseSelect          = dbHelperSelect.getWritableDatabase();
        }
    public void openDbForLearn(){
        dbHelperProgress        = new DBHelperProgressCulture(context);
        databaseProgress        = dbHelperProgress.getReadableDatabase();
        dbHelperSelect          = new DBHelperSelectGrammar(context);
        databaseSelect          = dbHelperSelect.getReadableDatabase();
        dbHelperStatic          = new DBHelperStaticCulture(context);
        databaseStatic          = dbHelperStatic.getReadableDatabase();
    }

    public void closeDatabases(){
        super.closeDatabases();
        databaseSelect.close();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////// ДЛЯ СВЯЗИ С ОСНОВНОЙ ////////////////////////////////////////////////////////////////////
    @Override
    protected Card createCard(String id, String course, int repeatLevel, int practiceLevel, int repetitionDat) {
        return new CultureCard(course, id, repeatLevel, practiceLevel, repetitionDat);
    }

    @Override
    protected Card createCard() {
        return new CultureCard();
    }

    @Override
    protected String getStaticTableName() {
        return STATIC_TABLE_NAME;
    }

    @Override
    protected String getProgressTableName() {
        return PROGRESS_TABLE_NAME;
    }

    @Override
    protected SQLiteOpenHelper getDbHelper() {
        return new DBHelperProgressCulture(context);
    }

    @Override
    protected int getPace() {
        return transportPreferences.getPaceCulture(context);
    }

    @Override
    public String getIntroStudy() {
        if (introStudy != null) return introStudy;
        Card card = getNextStudyCard();
        introStudy = getCourseName(card);
        return getIntroStudy();
    }
    @Override
    public String getIntroRepeat() {
        if (introRepeat != null) return introRepeat;
        Card card = getNextRepeatCard();
        introRepeat = getCourseName(card);
        return getIntroRepeat();
    }
    private String getCourseName(Card card){
        if (card == null) return "";
        TransportSQLCourse tp =  TransportSQLCourse.getInstance(context, index);
        String info = tp.getCourse(card.getCourse()).getName();
        tp.closeDb();
        return info;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////// FILL CONTENT VALUES /////////////////////////////////////////////////////////////////////
    protected ContentValues fillContentValuesForSelectTrain(GrammarCard.SelectTrain train){
        ContentValues contentValues = new ContentValues();
        contentValues.put(_ID, train.getId());
        contentValues.put(COLUMN_ANSWER, train.getAnswer());
        contentValues.put(COLUMN_RIGHT, train.getRight());
        contentValues.put(COLUMN_SECOND, train.getSecond());
        contentValues.put(COLUMN_THIRD, train.getThird());
        contentValues.put(COLUMN_FOURTH, train.getFourth());
        return contentValues;
    }

    protected ContentValues fillContentValuesForStatic(Card getCard){
        CultureCard card = (CultureCard) getCard;
        ContentValues contentValues = new ContentValues();
        contentValues.put(_ID, card.getId());
        contentValues.put(COLUMN_COURSE, card.getCourse());
        contentValues.put(COLUMN_THEORY, card.getTheory());
        return contentValues;
        }
    ////////////////////////////////////////////////////////////////////////////////////////////////






    ////// REDUCE CARD COUNT ///////////////////////////////////////////////////////////////////////
    public void reduceLeftStudyCount(){
        transportPreferences.setTodayLeftStudyCardQuantity(context, index,
         transportPreferences.getTodayLeftStudyCardQuantity(context, index) - 1);
    }
    public void reduceLeftRepeatCount(){
        transportPreferences.settTodayLeftRepeatCardQuantity(context, index,
        transportPreferences.getTodayLeftRepeatCardQuantity(context, index) - 1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////





    /////// RELOAD /////////////////////////////////////////////////////////////////////////////////
    public void updateSelect(GrammarCard.SelectTrain sentece){
        if (sentece != null) {
                if (databaseSelect.update(SELECT_TABLE_NAME, fillContentValuesForSelectTrain(sentece),
                        _ID + " == ?", new String[]{sentece.getId()}) == 0)
                    databaseSelect.insert(SELECT_TABLE_NAME, null, fillContentValuesForSelectTrain(sentece));
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /////// GET CARDS //////////////////////////////////////////////////////////////////////////////
    private void getCardFromSelectById(CultureCard card){
            Cursor cursor = databaseSelect.query(SELECT_TABLE_NAME, null, _ID + " == ?", new String[]{card.getId()},
                    null, null, null);
            if (cursor.moveToNext()) {
                card.setSelectTrain(getSelectTrain(cursor));
            }
            cursor.close();
    }

    public Card getNextRepeatCard(){
        if (transportPreferences.getTodayLeftRepeatCardQuantity(context, index) == 0) {
            Log.i("main", "repeat - null");
            return null;
        }
        boolean isClosed = databaseProgress == null  || !databaseProgress.isOpen();
        if (isClosed)
            openDbForLearn();
        Cursor cursor = databaseProgress.query(getProgressTableName(), null, COLUMN_REPETITION_DAY + " <= ?", new String[]{Integer.toString(Ruler.getDay(context))},
                null, null, null);
        Card card = null;
        while (cursor.moveToNext()) {
            card = getCardFromProgress(cursor);
            if (card.getRepeatlevel() == NOUN_LEVEL) {
                card = null;
                continue;
            }
            getCardFromStaticById(card.getId(), card);
            getCardFromSelectById((CultureCard) card);


            break;
        }
        cursor.close();
        if (isClosed)
            closeDatabases();


        return card;
    }

    @Override
    public Card getNextStudyCard() {
        if (transportPreferences.getTodayLeftStudyCardQuantity(context, index) == 0) {
            Log.i("main", "NONE");
            return null;
        }
        boolean isClosed = databaseProgress == null  || !databaseProgress.isOpen();
        if (isClosed)
            openDbForLearn();

        Cursor cursor = databaseProgress.query(PROGRESS_TABLE_NAME, null, COLUMN_REPETITION_DAY + " <= ?", new String[]{Integer.toString(Ruler.getDay(context))},
                null, null, null);
        CultureCard card = null;
        while (cursor.moveToNext()) {
            card = (CultureCard) getCardFromProgress(cursor);
            if (card.getRepeatlevel() != NOUN_LEVEL && card.getRepeatlevel() != 0) {
                card = null;
                continue;
            }
            getCardFromStaticById(card.getId(), card);
            getCardFromSelectById(card);
            break;
        }
        cursor.close();
        if (isClosed)
            closeDatabases();

        return card;
    }

    @Override
    protected void getCardFromTrainsById(Card card) {
        // nothing)))
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////




    //////ПОЛУЧЕНИЕ КАРТЫ ПО КУРСОРУ ///////////////////////////////////////////////////////////////
    protected void addStatic(Cursor cursor, Card card){
        CultureCard gCard = (CultureCard) card;
        gCard.setTheory(cursor.getString(cursor.getColumnIndex(COLUMN_THEORY)));
        if (gCard.getId() == null) {
            gCard.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            gCard.setCourse(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE)));
        }
    }

    protected Card addStatic(Cursor cursor){
        CultureCard card = new CultureCard();
        addStatic(cursor, card);
        return card;
    }

    private GrammarCard.SelectTrain getSelectTrain(Cursor cursor){
        GrammarCard.SelectTrain selectTrain = new GrammarCard.SelectTrain();
        selectTrain.setAnswer(cursor.getString(cursor.getColumnIndex(COLUMN_ANSWER)));
        selectTrain.setSecond(cursor.getString(cursor.getColumnIndex(COLUMN_SECOND)));
        selectTrain.setRight(cursor.getString(cursor.getColumnIndex(COLUMN_RIGHT)));
        selectTrain.setThird(cursor.getString(cursor.getColumnIndex(COLUMN_THIRD)));
        selectTrain.setFourth(cursor.getString(cursor.getColumnIndex(COLUMN_FOURTH)));
        selectTrain.setId(cursor.getString(cursor.getColumnIndex(_ID)));
        return selectTrain;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////




    public void addNewCard(Card card) {
        addNewCardToStatic(card);
        CultureCard gCard = (CultureCard) card;
        updateSelect(gCard.getSelectTrain());
    }

    public void deleteDb() {
        super.deleteDb();
        databaseSelect.delete(SELECT_TABLE_NAME, null, null);
    }
}
