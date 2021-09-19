package com.lya_cacoi.lotylops.Databases.transportSQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperChangesPhraseology;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperProgressPhraseology;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperStaticPhraseology;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.GET_RANDOM_FROM_STATIC_PHRASEOLOGY;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.PHRASEOLOGY_CHANGES_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.PHRASEOLOGY_PROGRESS_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.PHRASEOLOGY_STATIC_TABLE_NAME;


/*

сосбвтенно для подключения ДБ SQL

 */

public final class SqlPhraseology extends SqlVocaPhrasUnion{
    private static SqlPhraseology sqlPhraseology;


    //////// LIFE CYCLE ////////////////////////////////////////////////////////////////////////////
    public static SqlPhraseology getInstance(Context context){
        if (sqlPhraseology == null)
            sqlPhraseology = new SqlPhraseology(context);
        return sqlPhraseology;
    }
    private SqlPhraseology(Context context) {
            super(context);
        index = PHRASEOLOGY_INDEX;
        }
    public void openDbForUpdate(){
            dbHelperStatic          = new DBHelperStaticPhraseology(context);
            databaseStatic          = dbHelperStatic.getWritableDatabase();
            dbHelperProgress        = new DBHelperProgressPhraseology(context);
            databaseProgress        = dbHelperProgress.getWritableDatabase();
            dbHelperChange          = new DBHelperChangesPhraseology(context);
            databaseChanges         = dbHelperChange.getReadableDatabase();
            super.openDbForUpdate();
        }
    public void openDbForLearn(){
        dbHelperStatic          = new DBHelperStaticPhraseology(context);
        databaseStatic          = dbHelperStatic.getReadableDatabase();
        dbHelperProgress        = new DBHelperProgressPhraseology(context);
        databaseProgress        = dbHelperProgress.getReadableDatabase();
        super.openDbForLearn();
    }

    public void openDbForTrain(){
        dbHelperStatic          = new DBHelperStaticPhraseology(context);
        databaseStatic          = dbHelperStatic.getReadableDatabase();
        dbHelperProgress        = new DBHelperProgressPhraseology(context);
        databaseProgress        = dbHelperProgress.getReadableDatabase();
        super.openDbForTrain();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////// ДЛЯ СВЯЗИ С ОСНОВНОЙ ////////////////////////////////////////////////////////////////////
    @Override
    protected String getChangesTableName() {
        return PHRASEOLOGY_CHANGES_TABLE_NAME;
    }
    @Override
    protected String getStaticTableName() {
        return PHRASEOLOGY_STATIC_TABLE_NAME;
    }

    @Override
    protected String getProgressTableName() {
        return PHRASEOLOGY_PROGRESS_TABLE_NAME;
    }

    @Override
    protected SQLiteOpenHelper getDbHelper() {
        return new DBHelperProgressPhraseology(context);
    }

    @Override
    protected int getPace() {
        return transportPreferences.getPacePhraseology(context);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    ////// REDUCE CARD COUNT ///////////////////////////////////////////////////////////////////////
    public void reduceLeftStudyCount(){
        transportPreferences.setTodayLeftStudyCardQuantity(context, PHRASEOLOGY_INDEX,
         transportPreferences.getTodayLeftStudyCardQuantity(context, PHRASEOLOGY_INDEX) - 1);
    }
    public void reduceLeftRepeatCount(){
        transportPreferences.settTodayLeftRepeatCardQuantity(context, PHRASEOLOGY_INDEX,
        transportPreferences.getTodayLeftRepeatCardQuantity(context, PHRASEOLOGY_INDEX) - 1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public VocabularyCard getNextStudyCard(){
        if (transportPreferences.getTodayLeftStudyCardQuantity(context, PHRASEOLOGY_INDEX) == 0)
            return null;

        return super.getNextStudyCard();
    }

    public Card getNextRepeatCard(){
        if (transportPreferences.getTodayLeftRepeatCardQuantity(context, PHRASEOLOGY_INDEX) == 0)
            return null;
        
        return super.getNextRepeatCard();
    }

    public Card getRandomCardFromStatic() {
        Card card = null;
        Cursor cursor = databaseStatic.rawQuery(GET_RANDOM_FROM_STATIC_PHRASEOLOGY, null);
        if (cursor.moveToNext()){
            card = addStatic(cursor);
        }
        cursor.close();
        return card;
    }

}
