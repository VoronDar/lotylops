package com.lya_cacoi.lotylops.Databases.transportSQL;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;

import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperChangesVocabulary;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperProgressVocabulary;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperStaticVocabulary;
import com.lya_cacoi.lotylops.Databases.transportSQL.contracts.VocabularyContract;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.CHANGES_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_TRANSLATE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_WORD;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.GET_RANDOM_FROM_STATIC;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.PROGRESS_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.STATIC_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.TRAIN_TABLE_NAME;


/*

сосбвтенно для подключения ДБ SQL

 */

public final class SqlVocabulary extends SqlVocaPhrasUnion{
    private static SqlVocabulary sqlVocabulary;


    //////// LIFE CYCLE ////////////////////////////////////////////////////////////////////////////
    public static SqlVocabulary getInstance(Context context){
        if (sqlVocabulary == null)
            sqlVocabulary = new SqlVocabulary(context);
        return sqlVocabulary;
    }
    private SqlVocabulary(Context context) {
            super(context);
            index = VOCABULARY_INDEX;
        }
    public void openDbForUpdate(){
            dbHelperStatic          = new DBHelperStaticVocabulary(context);
            databaseStatic          = dbHelperStatic.getWritableDatabase();
            dbHelperProgress        = new DBHelperProgressVocabulary(context);
            databaseProgress        = dbHelperProgress.getWritableDatabase();
            dbHelperChange          = new DBHelperChangesVocabulary(context);
            databaseChanges         = dbHelperChange.getReadableDatabase();
            super.openDbForUpdate();
        }
    public void openDbForLearn(){
        dbHelperStatic          = new DBHelperStaticVocabulary(context);
        databaseStatic          = dbHelperStatic.getReadableDatabase();
        dbHelperProgress        = new DBHelperProgressVocabulary(context);
        databaseProgress        = dbHelperProgress.getWritableDatabase();
        super.openDbForLearn();
    }

    public void openDbForTrain(){
        dbHelperStatic          = new DBHelperStaticVocabulary(context);
        databaseStatic          = dbHelperStatic.getReadableDatabase();
        dbHelperProgress        = new DBHelperProgressVocabulary(context);
        databaseProgress        = dbHelperProgress.getReadableDatabase();
        super.openDbForTrain();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////// ДЛЯ СВЯЗИ С ОСНОВНОЙ ////////////////////////////////////////////////////////////////////
    @Override
    protected String getChangesTableName() {
        return CHANGES_TABLE_NAME;
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
        return new DBHelperProgressVocabulary(context);
    }

    @Override
    protected int getPace() {
        return transportPreferences.getPaceVocabulary(context);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    ////// REDUCE CARD COUNT ///////////////////////////////////////////////////////////////////////
    public void reduceLeftStudyCount(){
        transportPreferences.setTodayLeftStudyCardQuantity(context,VOCABULARY_INDEX,
         transportPreferences.getTodayLeftStudyCardQuantity(context, VOCABULARY_INDEX) - 1);
    }
    public void reduceLeftRepeatCount(){
        transportPreferences.settTodayLeftRepeatCardQuantity(context,VOCABULARY_INDEX,
        transportPreferences.getTodayLeftRepeatCardQuantity(context,VOCABULARY_INDEX) - 1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public VocabularyCard getNextStudyCard(){
        if (transportPreferences.getTodayLeftStudyCardQuantity(context,VOCABULARY_INDEX) == 0)
            return null;

        return super.getNextStudyCard();
    }

    public Card getNextRepeatCard(){
        if (transportPreferences.getTodayLeftRepeatCardQuantity(context,VOCABULARY_INDEX) == 0)
            return null;

        return super.getNextRepeatCard();
    }

    public Card getRandomCardFromStatic() {
        if (databaseStatic == null || !databaseStatic.isOpen()) {
            dbHelperStatic          = new DBHelperStaticVocabulary(context);
            databaseStatic          = dbHelperStatic.getReadableDatabase();
        }

        Card card = null;
        Cursor cursor = databaseStatic.rawQuery(GET_RANDOM_FROM_STATIC, null);
        if (cursor.moveToNext()){
            card = addStatic(cursor);
        }
        cursor.close();
        return card;
    }

    public VocabularyCard getRightWordAndTranslate(){
        VocabularyCard card = new VocabularyCard();
        //Cursor cursor = databaseStatic.rawQuery("SELECT " + COLUMN_WORD + " AND " + COLUMN_TRANSLATE
         //       + " FROM " + STATIC_TABLE_NAME + " ORDER BY RANDOM() LIMIT 1", null);

        Cursor cursor = databaseStatic.query(STATIC_TABLE_NAME, new String[]{COLUMN_WORD, COLUMN_TRANSLATE}, COLUMN_TRANSLATE + " IS NOT NULL", null, null, null, "RANDOM()", "1");

        while (cursor.moveToNext()){
            card.setWord(cursor.getString(cursor.getColumnIndex(VocabularyContract.VocabularyEntry.COLUMN_WORD)));
            card.setTranslate(cursor.getString(cursor.getColumnIndex(VocabularyContract.VocabularyEntry.COLUMN_TRANSLATE)));
            if (card.getTranslate() != null) break;
        }
        cursor.close();
        return card;
    }

    public VocabularyCard getWrongWordAndTranslate() {
        VocabularyCard card = getRightWordAndTranslate();
        if (card.getPart() == null) card.setPart("существительное");
        Cursor cursor = databaseStatic.query(STATIC_TABLE_NAME, null,
                COLUMN_TRANSLATE + " IS NOT NULL AND " + COLUMN_WORD + " != '" +
                        card.getWord() + "'",
                        //+ " AND "+ COLUMN_PART + " == '" + card.getPart() + "'",
                null, null, null, "RANDOM()", "1");
        while ((cursor.moveToNext())){
                String translate = cursor.getString(cursor.getColumnIndex(VocabularyContract.VocabularyEntry.COLUMN_TRANSLATE));
                if (!translate.equals(card.getTranslate())){
                    card.setTranslate(translate);
                    break;
                }
            }
        cursor.close();
        return card;
    }


    public void getGameAvailable(){
        if (DatabaseUtils.queryNumEntries(databaseStatic, getStaticTableName()) > 10) transportPreferences.setGameAllowed(context, true);
        if (DatabaseUtils.queryNumEntries(databaseTrain, TRAIN_TABLE_NAME) > 5) transportPreferences.setTrainAllowed(context, true);
    }

}
