package com.lya_cacoi.lotylops.Databases.transportSQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lya_cacoi.lotylops.ApproachManager.ApproachManager;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.ruler.CardRepeatManage;
import com.lya_cacoi.lotylops.ruler.Ruler;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static android.provider.BaseColumns._ID;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.CULTURE_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.EXCEPTION_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.Entry.COLUMN_PRACTICE_LEVEL;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.Entry.COLUMN_REPEAT_LEVEL;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.Entry.COLUMN_REPETITION_DAY;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_COURSE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_SENTENCE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_SENTENCE_CHANGE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_SENTENCE_TRANSLATE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN__LINKED_CARD_ID;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.GET_RANDOM_FROM_STATIC;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.TRAIN_TABLE_NAME;
import static com.lya_cacoi.lotylops.declaration.consts.NOUN_LEVEL;


/*

сосбвтенно для подключения ДБ SQL

 */

public abstract class SqlMain {
    protected SQLiteDatabase databaseStatic;
    protected SQLiteDatabase databaseTrain;
    protected SQLiteDatabase databaseProgress;
    protected SQLiteOpenHelper dbHelperStatic;
    protected SQLiteOpenHelper dbHelperProgress;
    protected SQLiteOpenHelper dbHelperTrain;
    protected Context context;

    protected int index;



    /////// ABSTRACT GET TABLE NAMES ///////////////////////////////////////////////////////////////
    protected abstract String getStaticTableName();
    protected abstract String getProgressTableName();
    protected abstract SQLiteOpenHelper getDbHelper();
    ////////////////////////////////////////////////////////////////////////////////////////////////



    /////// SOME ABSTRACT FUNC /////////////////////////////////////////////////////////////////////
    protected abstract Card createCard(String id, String course,
                                       int repeatLevel, int practiceLevel, int repetitionDat);
    protected abstract Card createCard();
    /** @return темп изучения конкретного раздела */
    protected abstract int getPace();
    /** на страничке дневного плана требуется информация о том, что конкретно будет изучаться в этот день
     * @return эта функция должна возвратить несколько слов, звуки или название темы для правил*/
    public abstract String getIntroStudy();
    public abstract String getIntroRepeat();
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /////// PUT VALUES IN TRANSPORTPREF ////////////////////////////////////////////////////////////
    /** @return осталось карт сегодня посмотреть*/
    public int getLearnCardLeft(){
        if (Ruler.dayState == consts.dayState.study)
            return transportPreferences.getTodayLeftStudyCardQuantity(context, index);
        else
            return transportPreferences.getTodayLeftRepeatCardQuantity(context, index);
    }
    /** @return всего карт нужно посмотреть сегодня*/
    public int getLearnCardToday(){
        if (Ruler.dayState == consts.dayState.study)
            return transportPreferences.getAllTodayStudyCount(context, index);
        else
            return transportPreferences.getAllTodayRepeatCount(context, index);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    //////// LIFE CYCLE ////////////////////////////////////////////////////////////////////////////
    protected SqlMain(Context context) {
            this.context = context;
            if (databaseStatic == null || databaseProgress == null || !databaseStatic.isOpen() || databaseProgress.isOpen()) openDbForLearn();
    }
    public void closeDatabases(){
        if (databaseStatic!= null && databaseStatic.isOpen())
            databaseStatic.close();
        if (databaseProgress!= null && databaseProgress.isOpen())
            databaseProgress.close();
        if (databaseTrain!= null && databaseTrain.isOpen())
            databaseTrain.close();
    }
    public abstract void openDbForUpdate();
    public abstract void openDbForLearn();

    public static SqlMain getInstance(Context context, int index){
        switch (index){
            case VOCABULARY_INDEX:
                return SqlVocabulary.getInstance(context);
            case GRAMMAR_INDEX:
                return SqlGrammar.getInstance(context);
            case PHRASEOLOGY_INDEX:
                return SqlPhraseology.getInstance(context);
            case EXCEPTION_INDEX:
                return SqlException.getInstance(context);
            case CULTURE_INDEX:
                return SqlCulture.getInstance(context);
            case PHONETIC_INDEX:
                return SqlPhonetics.getInstance(context);
        }
        throw new RuntimeException();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////




    ////// FILL CONTENT VALUES /////////////////////////////////////////////////////////////////////
    protected abstract ContentValues fillContentValuesForStatic(Card card);
    protected ContentValues fillContentValuesForProgress(Card card){
        ContentValues contentValues = new ContentValues();
        contentValues.put(_ID, card.getId());
        contentValues.put(COLUMN_COURSE, card.getCourse());
        contentValues.put(COLUMN_REPEAT_LEVEL, card.getRepeatlevel());
        contentValues.put(COLUMN_PRACTICE_LEVEL, card.getPracticeLevel());
        contentValues.put(COLUMN_REPETITION_DAY, card.getRepetitionDat());
        return contentValues;
    }
    protected ContentValues fillContentValuesForSentence(Card.Sentence sentence){
        ContentValues contentValues = new ContentValues();
        contentValues.put(_ID, sentence.getId());
        contentValues.put(COLUMN__LINKED_CARD_ID, sentence.getLinked_id());
        contentValues.put(COLUMN_SENTENCE, sentence.getSentence());
        contentValues.put(COLUMN_SENTENCE_TRANSLATE, sentence.getTranslate());
        contentValues.put(COLUMN_SENTENCE_CHANGE, sentence.getChange());
        return contentValues;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////






    ////// REDUCE CARD COUNT ///////////////////////////////////////////////////////////////////////
    public abstract void reduceLeftStudyCount();
    public abstract void reduceLeftRepeatCount();
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////// DELETE CARD /////////////////////////////////////////////////////////////////////////////
    /*
    private void deleteString(String id, SQLiteDatabase db, String tableName){
        String where = _ID + " = ?";
        String[] whereArgs = new String[]{id};
        db.delete(tableName, where, whereArgs);
    }
     */
    ////////////////////////////////////////////////////////////////////////////////////////////////





    /////// RELOAD /////////////////////////////////////////////////////////////////////////////////
    public void updateProgress(Card card){
        if (databaseProgress.update(getProgressTableName(), fillContentValuesForProgress(card),
                _ID + " == ?", new String[]{card.getId()}) <= 0) {
            databaseProgress.insert(getProgressTableName(), null, fillContentValuesForProgress(card));
        }
    }
    public void updateCard(Card card){
        if (databaseStatic.update(getStaticTableName(), fillContentValuesForStatic(card),
                _ID + " == ?", new String[]{card.getId()}) <= 0){
            databaseStatic.insert(getStaticTableName(), null, fillContentValuesForStatic(card));
        }
    }
    public void pushInProgress(Card vocabularyCard) { // обновляет прогресс только если его еще не загрузили
        vocabularyCard.setRepeatlevel(NOUN_LEVEL);
        vocabularyCard.setPracticeLevel(NOUN_LEVEL);
        vocabularyCard.setRepetitionDat(0);

        Cursor cursor = databaseProgress.query(getProgressTableName(), null, _ID + " == ?", new String[]{vocabularyCard.getId()}, null, null, null);
        if (!cursor.moveToNext()) {
            databaseProgress.insert(getProgressTableName(), null, fillContentValuesForProgress(vocabularyCard));
        }
        cursor.close();

        Log.i("main", "cards count " + DatabaseUtils.queryNumEntries(databaseProgress, getProgressTableName()));
    }
    public void updateTrains(ArrayList<VocabularyCard.Sentence> senteces){
        for (VocabularyCard.Sentence s: senteces) {
            if (databaseTrain.update(TRAIN_TABLE_NAME, fillContentValuesForSentence(s),
                    _ID + " == ?", new String[]{s.getId()}) <= 0)
                databaseTrain.insert(TRAIN_TABLE_NAME, null, fillContentValuesForSentence(s));
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /////// GET CARDS //////////////////////////////////////////////////////////////////////////////
    protected abstract void getCardFromTrainsById(Card card);

    public ArrayList<Card> getAllCardsFromCourse(String course) {
        Cursor cursor = databaseStatic.query(getStaticTableName(), null, COLUMN_COURSE + " == ?",
                new String[]{course},
                null, null, null);
        ArrayList<Card> cards = new ArrayList<>();
        while (cursor.moveToNext()) {
            cards.add(addStatic(cursor));
        }

        cursor.close();

        return cards;

    }



    public ArrayList<Card> getAllCardsForLib(String course) {
        Cursor cursor = databaseStatic.query(getStaticTableName(), null, COLUMN_COURSE + " == ?",
                new String[]{course},
                null, null, null);

        ArrayList<Card> cards = new ArrayList<>();
        while (cursor.moveToNext()) {
            cards.add(addStatic(cursor));
        }

            cursor.close();

        return cards;

    }


    public Card getCard(String id) {
        Card card = createCard();
        if (!getCardFromStaticById(id, card))
            return null;
        return card;
    }

    public abstract Card getNextRepeatCard();

    public abstract Card getNextStudyCard();

    protected boolean getCardFromStaticById(String id, Card card){
        Cursor cursor = databaseStatic.query(getStaticTableName(), null, _ID + " == ?", new String[]{id},
                null, null, null);
        if (cursor.moveToNext()) {
            addStatic(cursor, card);
            cursor.close();
            return true;
        } else
            cursor.close();
            return false;
    }


    // возвращает численное значение количества карт для каждого из 6 тестов
    public ArrayList<Integer> getCardsRepeatedToday(ArrayList<Card> practiceCards) {
        ArrayList<Integer> cardCount = new ArrayList<>();
        final int typesCount = 7;
        for (int i = 0; i < typesCount; i++) {
            cardCount.add(0);
        }

        Cursor cursor = databaseProgress.query(getProgressTableName(), null, COLUMN_REPETITION_DAY + " < ?",
                new String[]{Integer.toString(Ruler.getDay(context) + 100)},
                null, null, null);
        while (cursor.moveToNext()) {
            Card card = getCardFromProgress(cursor);
            if (CardRepeatManage.getLastDayOfRepeat(card.getRepetitionDat(), card.getRepeatlevel(), context, index) == Ruler.getDay(context)){
                getCardFromStaticById(card.getId(), card);
                practiceCards.add(card);
                int index = card.getRepeatlevel() - consts.START_TEST;
                cardCount.set(index, cardCount.get(index) + 1);
            }
        }

        cursor.close();
        //closeDbTrain();

        Log.i("it", "sizeNow: " + cardCount.size());

        return cardCount;
    }


    public int getCardsFromCourseForPractice(ArrayList<Card> practiceCards, String course) {
        int count = 0;
        Cursor cursor = databaseProgress.query(getProgressTableName(), null, COLUMN_COURSE + " == ?",
                new String[]{course},
                null, null, null);
        while (cursor.moveToNext()) {
            Card card = getCardFromProgress(cursor);
                getCardFromStaticById(card.getId(), card);
                practiceCards.add(card);
                count++;
        }

        cursor.close();
        //closeDbTrain();
        return count;
    }

    public Card isCardInProgress(String id) {

        Card card = null;

        Cursor cursor = databaseProgress.query(getProgressTableName(), null, _ID + " == ?",
                new String[]{id},
                null, null, null);
        if (cursor.moveToNext()) {
            card = getCardFromProgress(cursor);
            getCardFromStaticById(card.getId(), card);
        }

        cursor.close();
        return card;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////




    //////ПОЛУЧЕНИЕ КАРТЫ ПО КУРСОРУ ///////////////////////////////////////////////////////////////
    protected Card getCardFromProgress(Cursor cursor){

        String id = cursor.getString(cursor.getColumnIndex(_ID));
        String course = cursor.getString(cursor.getColumnIndex(COLUMN_COURSE));
        int repeatLevel = cursor.getInt(cursor.getColumnIndex(COLUMN_REPEAT_LEVEL));
        int practiceLevel = cursor.getInt(cursor.getColumnIndex(COLUMN_PRACTICE_LEVEL));
        int repetitionDat = cursor.getInt(cursor.getColumnIndex(COLUMN_REPETITION_DAY));
        return createCard(id,course,repeatLevel,practiceLevel,repetitionDat);
    }

    protected void addTrains(Cursor cursor, Card card) {
        String id = cursor.getString(cursor.getColumnIndex(_ID));
        String linked_id = cursor.getString(cursor.getColumnIndex(COLUMN__LINKED_CARD_ID));
        String sentence = cursor.getString(cursor.getColumnIndex(COLUMN_SENTENCE));
        String translate = cursor.getString(cursor.getColumnIndex(COLUMN_SENTENCE_TRANSLATE));
        Boolean changed = cursor.getInt(cursor.getColumnIndex(COLUMN_SENTENCE_CHANGE)) > 0;
        if (card.getTrains() != null)
            card.getTrains().add(new VocabularyCard.Sentence(sentence, translate, changed, id, linked_id));
        else {
            ArrayList<VocabularyCard.Sentence> sentences = new ArrayList<>();
            sentences.add(new VocabularyCard.Sentence(sentence, translate, changed, id, linked_id));
            card.setTrains(sentences);
        }
    }

    protected abstract void addStatic(Cursor cursor, Card card);
    protected abstract Card addStatic(Cursor cursor);

    ////////////////////////////////////////////////////////////////////////////////////////////////



    public void addNewCardToStatic(Card card) {
        databaseStatic.insert(getStaticTableName(), null, fillContentValuesForStatic(card));
    }

    public void addNewCard(Card card) {
        Log.i("main", card.toString());
        addNewCardToStatic(card);
    }

    public void deleteDb() {
        databaseProgress.delete(getProgressTableName(), null, null);
        databaseStatic.delete(getStaticTableName(), null, null);
        if (databaseTrain != null)
            databaseTrain.delete(TRAIN_TABLE_NAME, null, null);
    }


    // заносит в преференс значения для количества повторяющихся и новых карт
    public void getTodayCardsCount(){
        Log.i("main", "IT:" + index );
        boolean isOpen = (databaseProgress != null && databaseProgress.isOpen());
        if (!isOpen) {
            dbHelperProgress = getDbHelper();
            databaseProgress = dbHelperProgress.getReadableDatabase();
        }


        Cursor cursor = databaseProgress.query(getProgressTableName(), new String[]{COLUMN_REPEAT_LEVEL},
                COLUMN_REPETITION_DAY + " <= ?", new String[]{Integer.toString(Ruler.getDay(context))},
                null, null, null);
        int repeat = 0;
        int learn = 0;
        while (cursor.moveToNext()){
            if (cursor.getInt(cursor.getColumnIndex(COLUMN_REPEAT_LEVEL)) == NOUN_LEVEL) {
                if (getPace() > learn)
                    learn++;
            }
            else
                repeat++;
        }
        cursor.close();

        Log.i("main", "left: " + repeat + " " + learn);
        Log.i("main", "count " + (int) DatabaseUtils.queryNumEntries(databaseProgress, getProgressTableName()));
        transportPreferences.setTodayLeftStudyCardQuantity(context, index, learn);
        transportPreferences.settTodayLeftRepeatCardQuantity(context, index, repeat);

        if (!isOpen)
        databaseProgress.close();
    }

    /** заносит в преференс значения для количества повторяющихся и новых карт всего на сегодня */
    public void getAllTodayCardsCount(){
        transportPreferences.setAllTodayRepeatCount(context, index, 0);
        transportPreferences.setAllTodayStudyCount(context, index, 0);

        boolean isOpen = (databaseProgress != null && databaseProgress.isOpen());
        if (!isOpen) {
            dbHelperProgress = getDbHelper();
            databaseProgress = dbHelperProgress.getReadableDatabase();
        }


        Cursor cursor = databaseProgress.query(getProgressTableName(), new String[]{COLUMN_REPEAT_LEVEL},
                COLUMN_REPETITION_DAY + " <= ?", new String[]{Integer.toString(Ruler.getDay(context))},
                null, null, null);
        int repeat = 0;
        int learn = 0;
        while (cursor.moveToNext()){
            if (cursor.getInt(cursor.getColumnIndex(COLUMN_REPEAT_LEVEL)) == NOUN_LEVEL) {
                if (getPace() > learn)
                    learn++;
            }
            else
                repeat++;
        }
        cursor.close();

        Log.i("main", "all: " + repeat + " " + learn);
        Log.i("main", this.getClass().getName());


        transportPreferences.setAllTodayStudyCount(context, index, learn);
        transportPreferences.setAllTodayRepeatCount(context, index, repeat);

        if (!isOpen)
            databaseProgress.close();
    }


    // дополнительное обучение
    public void getTodayAddableCardsCount(){
        boolean isOpen = (databaseProgress != null && databaseProgress.isOpen());
        if (!isOpen) {
            dbHelperProgress = getDbHelper();
            databaseProgress = dbHelperProgress.getReadableDatabase();
        }


        Cursor cursor = databaseProgress.query(getProgressTableName(), new String[]{COLUMN_REPEAT_LEVEL},
                COLUMN_REPETITION_DAY + " <= ?", new String[]{Integer.toString(Ruler.getDay(context))},
                null, null, null);
        int learn = 0;
        while (cursor.moveToNext()){
            if (cursor.getInt(cursor.getColumnIndex(COLUMN_REPEAT_LEVEL)) == NOUN_LEVEL) {
                if (Objects.requireNonNull(ApproachManager.getManager(context, index)).getAddableCount() > learn)
                    learn++;
            }
        }
        cursor.close();

        Log.i("main", "count " + (int) DatabaseUtils.queryNumEntries(databaseProgress, getProgressTableName()));
        if (index == VOCABULARY_INDEX) {
            Log.i("main", "learn: " + learn);
        }
        transportPreferences.setTodayLeftStudyCardQuantity(context, index, learn);
        transportPreferences.settTodayLeftRepeatCardQuantity(context, index, 0);

        if (!isOpen)
            databaseProgress.close();
    }

    public Card getRandomCardFromStatic() {

        if (databaseStatic == null){
            openDbForLearn();
        }

        Card card = null;
        Cursor cursor = databaseStatic.rawQuery(GET_RANDOM_FROM_STATIC, null);
        if (cursor.moveToNext()){
            card = addStatic(cursor);
        }
        cursor.close();
        return card;
    }

    public void deleteCardsFromProgressByCourse(String id) {
        databaseProgress.delete(getProgressTableName(), COLUMN_COURSE + " == ?", new String[]{id});
    }

    public void addCardToProgress(ArrayList<Card> cards) {
        for (Card card: cards)
            databaseProgress.insert(getProgressTableName(), null, fillContentValuesForProgress(card));
    }


}
