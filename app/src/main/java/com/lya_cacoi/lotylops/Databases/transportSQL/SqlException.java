package com.lya_cacoi.lotylops.Databases.transportSQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperProgressExceptions;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperStaticExceptions;
import com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlPhoneticContract;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.ExceptionCard;
import com.lya_cacoi.lotylops.cards.PhoneticsCard;
import com.lya_cacoi.lotylops.ruler.Ruler;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static android.provider.BaseColumns._ID;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.EXCEPTION_INDEX;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.Entry.COLUMN_REPETITION_DAY;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_ANSWER;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_QUESTION;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.PROGRESS_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.STATIC_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_COURSE;
import static com.lya_cacoi.lotylops.declaration.consts.NOUN_LEVEL;


/*

сосбвтенно для подключения ДБ SQL

 */

public final class SqlException extends SqlMain {
    private static SqlException sqlException;
    private static String introStudy;
    private static String introRepeat;


    //////// LIFE CYCLE ////////////////////////////////////////////////////////////////////////////
    public static SqlException getInstance(Context context){
        if (sqlException == null)
            sqlException = new SqlException(context);
        return sqlException;
    }
    private SqlException(Context context) {
            super(context);
            index = EXCEPTION_INDEX;
        }
    public void openDbForUpdate(){
        dbHelperStatic          = new DBHelperStaticExceptions(context);
        databaseStatic          = dbHelperStatic.getWritableDatabase();
        dbHelperProgress        = new DBHelperProgressExceptions(context);
        databaseProgress        = dbHelperProgress.getWritableDatabase();
        }
    public void openDbForLearn(){
        dbHelperStatic          = new DBHelperStaticExceptions(context);
        databaseStatic          = dbHelperStatic.getReadableDatabase();
        dbHelperProgress        = new DBHelperProgressExceptions(context);
        databaseProgress        = dbHelperProgress.getReadableDatabase();
    }

    public void closeDatabases(){
        super.closeDatabases();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////// ДЛЯ СВЯЗИ С ОСНОВНОЙ ////////////////////////////////////////////////////////////////////
    @Override
    protected Card createCard(String id, String course, int repeatLevel, int practiceLevel, int repetitionDat) {
        return new ExceptionCard(course, id, repeatLevel, practiceLevel, repetitionDat, null, null);
    }

    @Override
    protected Card createCard() {
        return new ExceptionCard();
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
        return new DBHelperProgressExceptions(context);
    }
    @Override
    protected int getPace() {
        return transportPreferences.getPaceException(context);
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

    protected ContentValues fillContentValuesForStatic(Card getCard){
        ExceptionCard card = (ExceptionCard) getCard;
        ContentValues contentValues = new ContentValues();
        contentValues.put(_ID, card.getId());
        contentValues.put(COLUMN_COURSE, card.getCourse());
        contentValues.put(COLUMN_QUESTION, card.getQuestion());
        contentValues.put(COLUMN_ANSWER, card.getAnswer());
        return contentValues;
        }
    ////////////////////////////////////////////////////////////////////////////////////////////////






    ////// REDUCE CARD COUNT ///////////////////////////////////////////////////////////////////////
    public void reduceLeftStudyCount(){
        transportPreferences.setTodayLeftStudyCardQuantity(context, EXCEPTION_INDEX,
         transportPreferences.getTodayLeftStudyCardQuantity(context, EXCEPTION_INDEX) - 1);
    }
    public void reduceLeftRepeatCount(){
        transportPreferences.settTodayLeftRepeatCardQuantity(context, EXCEPTION_INDEX,
        transportPreferences.getTodayLeftRepeatCardQuantity(context, EXCEPTION_INDEX) - 1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////




    public Card getNextRepeatCard(){
        if (transportPreferences.getTodayLeftRepeatCardQuantity(context, EXCEPTION_INDEX) == 0)
            return null;
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
            break;
        }
        cursor.close();


        return card;
    }

    @Override
    public Card getNextStudyCard() {

        if (transportPreferences.getTodayLeftStudyCardQuantity(context, EXCEPTION_INDEX) == 0) {
            return null;
        }
        if (databaseProgress == null || !databaseProgress.isOpen()) openDbForLearn();

        Cursor cursor = databaseProgress.query(PROGRESS_TABLE_NAME, null, COLUMN_REPETITION_DAY + " <= ?", new String[]{Integer.toString(Ruler.getDay(context))},
                null, null, null);
        ExceptionCard card = null;
        while (cursor.moveToNext()) {
            card = (ExceptionCard) getCardFromProgress(cursor);
            if (card.getRepeatlevel() != NOUN_LEVEL && card.getRepeatlevel() != 0) {
                card = null;
                continue;
            }
            getCardFromStaticById(card.getId(), card);
            break;
        }
        cursor.close();

        return card;
    }

    @Override
    protected void getCardFromTrainsById(Card card) {
        // nothing)))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////




    //////ПОЛУЧЕНИЕ КАРТЫ ПО КУРСОРУ ///////////////////////////////////////////////////////////////
    protected void addStatic(Cursor cursor, Card card){
        ExceptionCard gCard = (ExceptionCard) card;
        gCard.setAnswer(cursor.getString(cursor.getColumnIndex(COLUMN_ANSWER)));
        gCard.setQuestion(cursor.getString(cursor.getColumnIndex(COLUMN_QUESTION)));
        if (gCard.getId() == null) {
            gCard.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            gCard.setCourse(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE)));
        }
    }

    protected Card addStatic(Cursor cursor){
        ExceptionCard card = new ExceptionCard();
        addStatic(cursor, card);
        return card;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////




    public void addNewCard(Card card) {
        addNewCardToStatic(card);
    }

    public void deleteDb() {
        super.deleteDb();
    }
}
