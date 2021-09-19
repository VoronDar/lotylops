package com.lya_cacoi.lotylops.Databases.transportSQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperProgressPhonetics;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperSelectGrammar;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperStaticPhonetics;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.CultureCard;
import com.lya_cacoi.lotylops.cards.GrammarCard;
import com.lya_cacoi.lotylops.cards.PhoneticsCard;
import com.lya_cacoi.lotylops.ruler.Ruler;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.Entry.COLUMN_REPETITION_DAY;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_ANSWER;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_FOURTH;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_RIGHT;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_SECOND;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_THIRD;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.SELECT_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlPhoneticContract.PhoneticEntry.COLUMN_EXAMPLES;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlPhoneticContract.PhoneticEntry.COLUMN_SELECT_TRAINS;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlPhoneticContract.PhoneticEntry.COLUMN_THEORY;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlPhoneticContract.PhoneticEntry.COLUMN_TRAINS;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlPhoneticContract.PhoneticEntry.COLUMN_TRANSCRIPT;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlPhoneticContract.PhoneticEntry.PROGRESS_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlPhoneticContract.PhoneticEntry.STATIC_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_COURSE;
import static com.lya_cacoi.lotylops.declaration.consts.NOUN_LEVEL;


/*

сосбвтенно для подключения ДБ SQL

 */

public final class SqlPhonetics extends SqlMain {
    private SQLiteDatabase databaseSelect;
    private SQLiteOpenHelper dbHelperSelect;
    private static com.lya_cacoi.lotylops.Databases.transportSQL.SqlPhonetics SqlPhonetics;
    private static String introStudy;
    private static String introRepeat;


    //////// LIFE CYCLE ////////////////////////////////////////////////////////////////////////////
    public static com.lya_cacoi.lotylops.Databases.transportSQL.SqlPhonetics getInstance(Context context){
        if (SqlPhonetics == null)
            SqlPhonetics = new SqlPhonetics(context);
        return SqlPhonetics;
    }
    private SqlPhonetics(Context context) {
            super(context);
        index = PHONETIC_INDEX;
        }
    public void openDbForUpdate(){
        dbHelperStatic          = new DBHelperStaticPhonetics(context);
        databaseStatic          = dbHelperStatic.getWritableDatabase();
        dbHelperProgress        = new DBHelperProgressPhonetics(context);
        databaseProgress        = dbHelperProgress.getWritableDatabase();
        dbHelperSelect          = new DBHelperSelectGrammar(context);
        databaseSelect          = dbHelperSelect.getWritableDatabase();
        }
    public void openDbForLearn(){
        dbHelperStatic          = new DBHelperStaticPhonetics(context);
        databaseStatic          = dbHelperStatic.getReadableDatabase();
        dbHelperProgress        = new DBHelperProgressPhonetics(context);
        databaseProgress        = dbHelperProgress.getReadableDatabase();
        dbHelperSelect          = new DBHelperSelectGrammar(context);
        databaseSelect          = dbHelperSelect.getReadableDatabase();
    }
    public void closeDatabases(){
        super.closeDatabases();
        databaseSelect.close();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////// ДЛЯ СВЯЗИ С ОСНОВНОЙ ////////////////////////////////////////////////////////////////////
    @Override
    protected Card createCard(String id, String course, int repeatLevel, int practiceLevel, int repetitionDat) {
        return new PhoneticsCard(course, id, repeatLevel, practiceLevel, repetitionDat);
    }

    @Override
    protected Card createCard() {
        return new PhoneticsCard();
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
        return new DBHelperProgressPhonetics(context);
    }

    @Override
    protected int getPace() {
        return transportPreferences.getPacePhonetics(context);
    }

    @Override
    public String getIntroStudy() {
        if (introStudy != null) return introStudy;
        boolean isClosed = databaseProgress == null  || !databaseProgress.isOpen();
        if (isClosed)
            openDbForLearn();
        Cursor cursor = databaseProgress.query(getProgressTableName(), null, COLUMN_REPETITION_DAY + " <= ?", new String[]{Integer.toString(Ruler.getDay(context))},
                null, null, null);
        PhoneticsCard card;
        int count = 0;
        StringBuilder result = new StringBuilder();
        while (cursor.moveToNext() && count < 5) {
            count++;
            card = (PhoneticsCard) getCardFromProgress(cursor);
            if (card.getRepeatlevel() != NOUN_LEVEL && card.getRepeatlevel() != 0) {
                continue;
            }
            getCardFromStaticById(card.getId(), card);
            result.append(card.getTranscription()).append(", ");
        }
        cursor.close();
        if (isClosed)
            closeDatabases();

        if (result.length() == 0)
            return "";
        introStudy =  result.substring(0, result.length()-2) + "...";
        return introStudy;

    }
    @Override
    public String getIntroRepeat() {
        if (introRepeat != null) return introRepeat;
        boolean isClosed = databaseProgress == null  || !databaseProgress.isOpen();
        if (isClosed)
            openDbForLearn();
        Cursor cursor = databaseProgress.query(getProgressTableName(), null, COLUMN_REPETITION_DAY + " <= ?", new String[]{Integer.toString(Ruler.getDay(context))},
                null, null, null);
        PhoneticsCard card;
        int count = 0;
        StringBuilder result = new StringBuilder();
        while (cursor.moveToNext() && count < 5) {
            card = (PhoneticsCard) getCardFromProgress(cursor);
            if (card.getRepeatlevel() == NOUN_LEVEL) {
                continue;
            }
            count++;
            getCardFromStaticById(card.getId(), card);
            result.append(card.getTranscription()).append(", ");
        }
        cursor.close();
        if (isClosed)
            closeDatabases();
        if (result.length() == 0)
            return "";
        introRepeat =  result.substring(0, result.length()-2) + "...";
        return introRepeat;

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
        PhoneticsCard card = (PhoneticsCard) getCard;
        ContentValues contentValues = new ContentValues();
        contentValues.put(_ID, card.getId());
        contentValues.put(COLUMN_COURSE, card.getCourse());
        contentValues.put(COLUMN_THEORY, card.getTheory());
        contentValues.put(COLUMN_TRANSCRIPT, card.getTranscription());
        contentValues.put(COLUMN_SELECT_TRAINS, convertArrayToString(card.getSelectTrainsId()));
        contentValues.put(COLUMN_EXAMPLES, convertArrayToString(card.getWordExamples()));
        contentValues.put(COLUMN_TRAINS, convertArrayToString(card.getWordTrains()));
        return contentValues;
        }

        private String convertArrayToString(List<String> array){
        if (array == null) return null;
            StringBuilder builder = new StringBuilder();
            for (String a: array){
                builder.append(a).append(",");
            }
            if (builder.length() > 1)
                builder.deleteCharAt(builder.length()-1);
            return builder.toString();
        }
    ////////////////////////////////////////////////////////////////////////////////////////////////






    ////// REDUCE CARD COUNT ///////////////////////////////////////////////////////////////////////
    public void reduceLeftStudyCount(){
        transportPreferences.setTodayLeftStudyCardQuantity(context, GRAMMAR_INDEX,
         transportPreferences.getTodayLeftStudyCardQuantity(context, GRAMMAR_INDEX) - 1);
    }
    public void reduceLeftRepeatCount(){
        transportPreferences.settTodayLeftRepeatCardQuantity(context, GRAMMAR_INDEX,
        transportPreferences.getTodayLeftRepeatCardQuantity(context, GRAMMAR_INDEX) - 1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////





    /////// RELOAD /////////////////////////////////////////////////////////////////////////////////
    public void updateSelect(ArrayList<GrammarCard.SelectTrain> senteces){
        if (senteces != null) {
            for (GrammarCard.SelectTrain s : senteces) {
                if (databaseSelect.update(SELECT_TABLE_NAME, fillContentValuesForSelectTrain(s),
                        _ID + " == ?", new String[]{s.getId()}) <= 0)
                    databaseSelect.insert(SELECT_TABLE_NAME, null, fillContentValuesForSelectTrain(s));
            }
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /////// GET CARDS //////////////////////////////////////////////////////////////////////////////
    private void getCardFromSelectById(PhoneticsCard card){
        Log.i("main", DatabaseUtils.queryNumEntries(databaseSelect, SELECT_TABLE_NAME) + " - count");

        if (card.getSelectTrainsId() == null) return;
        ArrayList<GrammarCard.SelectTrain> trains = new ArrayList<>();
        for(String train : card.getSelectTrainsId()) {
            Cursor cursor = databaseSelect.query(SELECT_TABLE_NAME, null, _ID + " == ?", new String[]{train},
                    null, null, null);
            if (cursor.moveToNext()) {
                trains.add(getSelectTrain(cursor));
                Log.i("main", "add " + trains.get(trains.size()-1));
            }
            cursor.close();
        }
        card.setSelectTrains(trains);
    }

    public Card getNextRepeatCard(){
        if (transportPreferences.getTodayLeftRepeatCardQuantity(context, GRAMMAR_INDEX) == 0)
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
            getCardFromSelectById((PhoneticsCard) card);
            break;
        }
        cursor.close();


        return card;
    }

    @Override
    public Card getNextStudyCard() {
        if (transportPreferences.getTodayLeftStudyCardQuantity(context, GRAMMAR_INDEX) == 0) {
            Log.i("main", "NONE");
            return null;
        }

        Cursor cursor = databaseProgress.query(PROGRESS_TABLE_NAME, null, COLUMN_REPETITION_DAY + " <= ?", new String[]{Integer.toString(Ruler.getDay(context))},
                null, null, null);
        PhoneticsCard card = null;
        while (cursor.moveToNext()) {
            card = (PhoneticsCard) getCardFromProgress(cursor);
            if (card.getRepeatlevel() != NOUN_LEVEL && card.getRepeatlevel() != 0) {
                card = null;
                continue;
            }
            getCardFromStaticById(card.getId(), card);
            getCardFromSelectById(card);
            break;
        }
        cursor.close();

        return card;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////




    //////ПОЛУЧЕНИЕ КАРТЫ ПО КУРСОРУ ///////////////////////////////////////////////////////////////
    protected void addStatic(Cursor cursor, Card card){
        PhoneticsCard gCard = (PhoneticsCard) card;
        gCard.setTheory(cursor.getString(cursor.getColumnIndex(COLUMN_THEORY)));
        gCard.setTranscription(cursor.getString(cursor.getColumnIndex(COLUMN_TRANSCRIPT)));
        gCard.setSelectTrainsId(convertStringToArray(cursor.getString(cursor.getColumnIndex(COLUMN_SELECT_TRAINS))));
        gCard.setWordTrains(convertStringToArray(cursor.getString(cursor.getColumnIndex(COLUMN_TRAINS))));
        gCard.setWordExamples(convertStringToArray(cursor.getString(cursor.getColumnIndex(COLUMN_EXAMPLES))));
        if (gCard.getId() == null) {
            gCard.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            gCard.setCourse(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE)));
        }
    }

    protected Card addStatic(Cursor cursor){
        PhoneticsCard card = new PhoneticsCard();
        addStatic(cursor, card);
        return card;
    }
    private List<String> convertStringToArray(String convert){
        if (convert == null) return null;
        String[] a =  convert.split(",");
        List<String> list = new ArrayList<>();
        for (String s: a)
            list.add(s);
        return list;
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

    @Override
    protected void getCardFromTrainsById(Card card) {
        // nothing)))
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////




    public void addNewCard(Card card) {
        addNewCardToStatic(card);
        PhoneticsCard gCard = (PhoneticsCard) card;
        updateSelect(gCard.getSelectTrains());
    }

    public void deleteDb() {
        super.deleteDb();
        databaseSelect.delete(SELECT_TABLE_NAME, null, null);
    }
}
