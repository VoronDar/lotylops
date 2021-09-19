package com.lya_cacoi.lotylops.Databases.transportSQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperProgressGrammar;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperSelectGrammar;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperStaticGrammar;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperTrainVocabulary;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.CultureCard;
import com.lya_cacoi.lotylops.cards.GrammarCard;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.ruler.Ruler;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.Entry.COLUMN_REPETITION_DAY;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_ANSWER;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_BLOCK_ID;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_FOURTH;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_RIGHT;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_SECOND;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_SELECT_ID;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_THEORY;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_THIRD;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_TRAIN_ID;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.PROGRESS_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.SELECT_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.STATIC_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_COURSE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_SENTENCE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_SENTENCE_CHANGE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_SENTENCE_TRANSLATE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN__LINKED_CARD_ID;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.TRAIN_TABLE_NAME;
import static com.lya_cacoi.lotylops.declaration.consts.NOUN_LEVEL;


/*

сосбвтенно для подключения ДБ SQL

 */

public final class SqlGrammar extends SqlMain {
    private SQLiteDatabase databaseSelect;
    private SQLiteOpenHelper dbHelperSelect;
    private static com.lya_cacoi.lotylops.Databases.transportSQL.SqlGrammar SqlGrammar;
    private static String introStudy;
    private static String introRepeat;

    //////// LIFE CYCLE ////////////////////////////////////////////////////////////////////////////
    public static com.lya_cacoi.lotylops.Databases.transportSQL.SqlGrammar getInstance(Context context){
        if (SqlGrammar == null)
            SqlGrammar = new SqlGrammar(context);
        return SqlGrammar;
    }
    private SqlGrammar(Context context) {
            super(context);
        index = GRAMMAR_INDEX;
        }
    public void openDbForUpdate(){
        dbHelperStatic          = new DBHelperStaticGrammar(context);
        databaseStatic          = dbHelperStatic.getWritableDatabase();
        dbHelperProgress        = new DBHelperProgressGrammar(context);
        databaseProgress        = dbHelperProgress.getWritableDatabase();
        dbHelperSelect          = new DBHelperSelectGrammar(context);
        databaseSelect          = dbHelperSelect.getWritableDatabase();
        dbHelperTrain           = new DBHelperTrainVocabulary(context);
        databaseTrain           = dbHelperTrain.getWritableDatabase();
        }
    public void openDbForLearn(){
        dbHelperProgress        = new DBHelperProgressGrammar(context);
        databaseProgress        = dbHelperProgress.getReadableDatabase();
        dbHelperSelect          = new DBHelperSelectGrammar(context);
        databaseSelect          = dbHelperSelect.getReadableDatabase();
        dbHelperTrain           = new DBHelperTrainVocabulary(context);
        databaseTrain           = dbHelperTrain.getReadableDatabase();
        dbHelperStatic          = new DBHelperStaticGrammar(context);
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
        return new GrammarCard(course, id, repeatLevel, practiceLevel, repetitionDat);
    }

    @Override
    protected Card createCard() {
        return new GrammarCard();
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
        return new DBHelperProgressGrammar(context);
    }

    @Override
    protected int getPace() {
        return transportPreferences.getPaceGrammar(context);
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
        GrammarCard card = (GrammarCard)getCard;
        ContentValues contentValues = new ContentValues();
        contentValues.put(_ID, card.getId());
        contentValues.put(COLUMN_COURSE, card.getCourse());
        contentValues.put(COLUMN_THEORY, card.getTheory());
        //contentValues.put(COLUMN_MISTAKE_ID, card.getMistakeId());
        contentValues.put(COLUMN_BLOCK_ID, convertArrayToString(card.getBlockTrainsId()));
        contentValues.put(COLUMN_SELECT_ID, convertArrayToString(card.getSelectTrainsId()));
        contentValues.put(COLUMN_TRAIN_ID, convertArrayToString(card.getTrainsId()));
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
                        _ID + " == ?", new String[]{s.getId()}) == 0)
                    databaseSelect.insert(SELECT_TABLE_NAME, null, fillContentValuesForSelectTrain(s));
            }
        }

    }public void updateBlockTrains(ArrayList<Card.Sentence> senteces){
        if (senteces != null) {
            for (VocabularyCard.Sentence sentence: senteces) {
                if (databaseTrain.update(TRAIN_TABLE_NAME, fillContentValuesForSentence(sentence), _ID+ " == ?", new String[]{sentence.getId()}) == 0)
                    databaseTrain.insert(TRAIN_TABLE_NAME, null, fillContentValuesForSentence(sentence));
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /////// GET CARDS //////////////////////////////////////////////////////////////////////////////
    private void getCardFromSelectById(GrammarCard card){
        if (card.getSelectTrainsId() == null) return;
        ArrayList<GrammarCard.SelectTrain> trains = new ArrayList<>();
        for(String train : card.getSelectTrainsId()) {
            Cursor cursor = databaseSelect.query(SELECT_TABLE_NAME, null, _ID + " == ?", new String[]{train},
                    null, null, null);
            if (cursor.moveToNext()) {
                trains.add(getSelectTrain(cursor));
            }
            cursor.close();
        }
        card.setSelectTrains(trains);
    }

    private void getCardFromBlockTrainsById(GrammarCard card){
        if (card.getBlockTrainsId() == null) return;
        ArrayList<Card.Sentence> trains = new ArrayList<>();
        for(String train : card.getBlockTrainsId()) {
            Cursor cursor = databaseTrain.query(TRAIN_TABLE_NAME, null, _ID + " == ?", new String[]{train},
                    null, null, null);
            if (cursor.moveToNext()) {
                    String id = cursor.getString(cursor.getColumnIndex(_ID));
                    String linked_id = cursor.getString(cursor.getColumnIndex(COLUMN__LINKED_CARD_ID));
                    String sentence = cursor.getString(cursor.getColumnIndex(COLUMN_SENTENCE));
                    String translate = cursor.getString(cursor.getColumnIndex(COLUMN_SENTENCE_TRANSLATE));
                    Boolean changed = cursor.getInt(cursor.getColumnIndex(COLUMN_SENTENCE_CHANGE)) > 0;
                    trains.add(new Card.Sentence(sentence, translate, changed, id, linked_id));
                cursor.close();
            }
        }
        card.setBlockTrains(trains);
    }

    @Override
    protected void getCardFromTrainsById(Card card){
        GrammarCard gCard = (GrammarCard)card;
        if (gCard.getTrainsId() == null) return;
        for(String train : gCard.getTrainsId()) {
            Cursor cursor = databaseTrain.query(TRAIN_TABLE_NAME, null, _ID + " == ?", new String[]{train},
                    null, null, null);
            if (cursor.moveToNext()) {
                addTrains(cursor, card);
            }
            cursor.close();
        }
    }

    public Card getNextRepeatCard(){
        if (transportPreferences.getTodayLeftRepeatCardQuantity(context, GRAMMAR_INDEX) == 0)
            return null;

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
            getCardFromTrainsById(card);
            getCardFromSelectById((GrammarCard) card);
            getCardFromBlockTrainsById((GrammarCard) card);


            break;
        }
        cursor.close();

        if (isClosed)
            closeDatabases();

        return card;
    }

    @Override
    public Card getNextStudyCard() {

        Log.i("main", " cards left " + transportPreferences.getTodayLeftStudyCardQuantity(context, GRAMMAR_INDEX) );
        if (transportPreferences.getTodayLeftStudyCardQuantity(context, GRAMMAR_INDEX) == 0) {
            Log.i("main", "NONE");
            return null;
        }

        boolean isClosed = databaseProgress == null  || !databaseProgress.isOpen();
        if (isClosed)
            openDbForLearn();

        Cursor cursor = databaseProgress.query(PROGRESS_TABLE_NAME, null, COLUMN_REPETITION_DAY + " <= ?", new String[]{Integer.toString(Ruler.getDay(context))},
                null, null, null);
        GrammarCard card = null;
        while (cursor.moveToNext()) {
            card = (GrammarCard) getCardFromProgress(cursor);
            if (card.getRepeatlevel() != NOUN_LEVEL && card.getRepeatlevel() != 0) {
                card = null;
                continue;
            }
            getCardFromStaticById(card.getId(), card);
            getCardFromTrainsById(card);
            getCardFromSelectById(card);
            getCardFromBlockTrainsById(card);
            break;
        }
        cursor.close();
        if (isClosed)
            closeDatabases();

        return card;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////




    //////ПОЛУЧЕНИЕ КАРТЫ ПО КУРСОРУ ///////////////////////////////////////////////////////////////
    private void addBlockTrains(Cursor cursor, Card card) {
        String id = cursor.getString(cursor.getColumnIndex(_ID));
        String linked_id = cursor.getString(cursor.getColumnIndex(COLUMN__LINKED_CARD_ID));
        String sentence = cursor.getString(cursor.getColumnIndex(COLUMN_SENTENCE));
        String translate = cursor.getString(cursor.getColumnIndex(COLUMN_SENTENCE_TRANSLATE));
        Boolean changed = cursor.getInt(cursor.getColumnIndex(COLUMN_SENTENCE_CHANGE)) > 0;
        GrammarCard gCard = (GrammarCard) card;
        if (gCard.getBlockTrains() != null)
            gCard.getBlockTrains().add(new VocabularyCard.Sentence(sentence, translate, changed, id, linked_id));
        else {
            ArrayList<VocabularyCard.Sentence> sentences = new ArrayList<>();
            sentences.add(new VocabularyCard.Sentence(sentence, translate, changed, id, linked_id));
            gCard.setBlockTrains(sentences);
        }
    }

    protected void addStatic(Cursor cursor, Card card){
        GrammarCard gCard = (GrammarCard) card;
        gCard.setTheory(cursor.getString(cursor.getColumnIndex(COLUMN_THEORY)));
        //gCard.setMistakeId(cursor.getString(cursor.getColumnIndex(COLUMN_MISTAKE_ID)));
        gCard.setSelectTrainsId(convertStringToArray(cursor.getString(cursor.getColumnIndex(COLUMN_SELECT_ID))));
        gCard.setBlockTrainsId(convertStringToArray(cursor.getString(cursor.getColumnIndex(COLUMN_BLOCK_ID))));
        gCard.setTrainsId(convertStringToArray(cursor.getString(cursor.getColumnIndex(COLUMN_TRAIN_ID))));
        if (gCard.getId() == null) {
            gCard.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            gCard.setCourse(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE)));
        }
    }

    protected Card addStatic(Cursor cursor){
        GrammarCard card = new GrammarCard();
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
    ////////////////////////////////////////////////////////////////////////////////////////////////




    public void addNewCard(Card card) {
        addNewCardToStatic(card);
        GrammarCard gCard = (GrammarCard)card;
        updateBlockTrains(gCard.getBlockTrains());
        updateSelect(gCard.getSelectTrains());
        updateTrains(gCard.getTrains());
    }

    public void deleteDb() {
        super.deleteDb();
        databaseSelect.delete(SELECT_TABLE_NAME, null, null);
    }
}
