package com.lya_cacoi.lotylops.Databases.transportSQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperDialogue;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperExamplesVocabulary;
import com.lya_cacoi.lotylops.Databases.transportSQL.DBHelpers.DBHelperTrainVocabulary;
import com.lya_cacoi.lotylops.adapters.units.VocabularyCardLibUnit;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.GrammarCard;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.ruler.CardRepeatManage;
import com.lya_cacoi.lotylops.ruler.Ruler;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.Entry.COLUMN_REPETITION_DAY;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.PhraseologyContract.PhraseologyEntry.COLUMN_LINKED;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_ANSWER;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_FOURTH;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_RIGHT;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_SECOND;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.COLUMN_THIRD;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.DIALOGUE_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlGrammarContract.GrammarEntry.SELECT_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_ANTONYM;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_ANTONYM_CHANGE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_COURSE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_GROUP;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_GROUP_CHANGE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_HELP;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_HELP_CHANGE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_MEANING;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_MEANING_CHANGE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_MEANING_NATIVE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_MEANING_NATIVE_CHANGE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_MEM;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_PART;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_PART_CHANGE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_SENTENCE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_SENTENCE_CHANGE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_SENTENCE_TRANSLATE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_SYNONYM;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_SYNONYM_CHANGE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_TRANSCRIPTION;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_TRANSCRIPTION_CHANGE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_TRANSLATE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_TRANSLATE_CHANGE;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_WORD;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN__LINKED_CARD_ID;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.EXAMPLES_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.GET_RANDOM_FROM_STATIC;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.STATIC_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.TRAIN_TABLE_NAME;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.VocabularyContract.VocabularyEntry.COLUMN_IS_MINE;
import static com.lya_cacoi.lotylops.declaration.consts.NOUN_LEVEL;


/*

сосбвтенно для подключения ДБ SQL

абстрактный - общий базовый для вокабуляра и фразеологии

 */

public abstract class SqlVocaPhrasUnion extends SqlMain {
    private SQLiteDatabase databaseExamples;
    protected SQLiteDatabase databaseChanges;
    protected SQLiteDatabase databaseDialogue;

    private SQLiteOpenHelper dbHelperExamples;
    protected SQLiteOpenHelper dbHelperChange;
    private SQLiteOpenHelper dbHelperDialogue;
    private static String introStudyVoc;
    private static String introRepeatVoc;
    private static String introStudyPhr;
    private static String introRepeatPhr;



    //////// ABSTRACT FUNC /////////////////////////////////////////////////////////////////////////
    protected abstract String getChangesTableName();
    ////////////////////////////////////////////////////////////////////////////////////////////////


    //////// LIFE CYCLE ////////////////////////////////////////////////////////////////////////////
    protected SqlVocaPhrasUnion(Context context) {
            super(context);
        }
    public void openDbForUpdate(){
            dbHelperExamples        = new DBHelperExamplesVocabulary(context);
            databaseExamples        = dbHelperExamples.getWritableDatabase();
            dbHelperTrain           = new DBHelperTrainVocabulary(context);
            databaseTrain           = dbHelperTrain.getWritableDatabase();
            dbHelperDialogue        = new DBHelperDialogue(context);
            databaseDialogue        = dbHelperDialogue.getWritableDatabase();
        }
    public void openDbForLearn(){
        dbHelperExamples        = new DBHelperExamplesVocabulary(context);
        databaseExamples        = dbHelperExamples.getReadableDatabase();
        dbHelperTrain           = new DBHelperTrainVocabulary(context);
        databaseTrain           = dbHelperTrain.getReadableDatabase();
        dbHelperDialogue        = new DBHelperDialogue(context);
        databaseDialogue        = dbHelperDialogue.getReadableDatabase();
    }

    public void openDbForTrain(){
        dbHelperTrain           = new DBHelperTrainVocabulary(context);
        databaseTrain           = dbHelperTrain.getReadableDatabase();
        dbHelperDialogue        = new DBHelperDialogue(context);
        databaseDialogue        = dbHelperDialogue.getReadableDatabase();
    }
    public void closeDbTrain(){
        closeDatabases();
    }

    public void closeDatabases(){
        super.closeDatabases();
        if (databaseExamples != null && databaseExamples.isOpen())
        databaseExamples.close();
        if (databaseDialogue != null && databaseDialogue.isOpen())
            databaseDialogue.close();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////// ДЛЯ СВЯЗИ С ОСНОВНОЙ ////////////////////////////////////////////////////////////////////

    @Override
    protected Card createCard(String id, String course, int repeatLevel, int practiceLevel, int repetitionDat) {
        return new VocabularyCard(course, id, repeatLevel, practiceLevel, repetitionDat);
    }
    @Override
    protected Card createCard() {
        return new VocabularyCard();
    }


    @Override
    public String getIntroStudy() {
        if (introStudyVoc != null && index == VOCABULARY_INDEX) return introStudyVoc;
        else if (introStudyPhr != null && index == PHRASEOLOGY_INDEX) return introStudyPhr;
        boolean isClosed = databaseProgress == null  || !databaseProgress.isOpen();
        if (isClosed)
            openDbForLearn();

        Log.i("main", getProgressTableName());
        Cursor cursor = databaseProgress.query(getProgressTableName(), null, COLUMN_REPETITION_DAY + " <= ?", new String[]{Integer.toString(Ruler.getDay(context))},
                null, null, null);
        VocabularyCard card;
        int count = 0;
        StringBuilder result = new StringBuilder();
        while (cursor.moveToNext() && (((count < 3 && index == VOCABULARY_INDEX) || (count < 1 && index == PHRASEOLOGY_INDEX)))) {
            card = (VocabularyCard) getCardFromProgress(cursor);
            if (card.getRepeatlevel() != NOUN_LEVEL && card.getRepeatlevel() != 0) {
                continue;
            }
            count++;
            getCardFromStaticById(card.getId(), card);
            result.append(card.getWord()).append(", ");
        }
        cursor.close();
        if (isClosed)
            closeDatabases();

        if (index == VOCABULARY_INDEX)
            introStudyVoc = result.substring(0, result.length()-2) + "...";
        else
            introStudyPhr = result.substring(0, result.length()-2) + "...";
        return getIntroStudy();

    }
    @Override
    public String getIntroRepeat() {
        if (introRepeatVoc != null && index == VOCABULARY_INDEX) return introRepeatVoc;
        else if (introRepeatPhr != null && index == PHRASEOLOGY_INDEX) return introRepeatPhr;
        boolean isClosed = databaseProgress == null  || !databaseProgress.isOpen();
        if (isClosed)
            openDbForLearn();
        Cursor cursor = databaseProgress.query(getProgressTableName(), null, COLUMN_REPETITION_DAY + " <= ?", new String[]{Integer.toString(Ruler.getDay(context))},
                null, null, null);
        VocabularyCard card;
        int count = 0;
        StringBuilder result = new StringBuilder();
        while (cursor.moveToNext() && (((count < 3 && index == VOCABULARY_INDEX) || (count < 1 && index == PHRASEOLOGY_INDEX)))) {
            card = (VocabularyCard) getCardFromProgress(cursor);
            if (card.getRepeatlevel() == NOUN_LEVEL) {
                continue;
            }
            count++;
            getCardFromStaticById(card.getId(), card);
            result.append(card.getWord()).append(", ");
        }
        cursor.close();

        if (isClosed)
            closeDatabases();

        if (index == VOCABULARY_INDEX)
            introRepeatVoc = result.substring(0, result.length()-2) + "...";
        else
            introRepeatPhr = result.substring(0, result.length()-2) + "...";
        return getIntroRepeat();

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////// FILL CONTENT VALUES /////////////////////////////////////////////////////////////////////
    protected ContentValues fillContentValuesForStatic(Card crd){
        VocabularyCard card = (VocabularyCard)crd;
            ContentValues contentValues = new ContentValues();
            contentValues.put(_ID, card.getId());
            contentValues.put(COLUMN_COURSE, card.getCourse());
            contentValues.put(COLUMN_WORD, card.getWord());
            contentValues.put(COLUMN_MEANING, card.getMeaning());
            contentValues.put(COLUMN_MEANING_NATIVE, card.getMeaningNative());
            contentValues.put(COLUMN_TRANSLATE, card.getTranslate());
            contentValues.put(COLUMN_LINKED, card.getLinked());
            contentValues.put(COLUMN_TRANSCRIPTION, card.getTranscription());
            contentValues.put(COLUMN_SYNONYM, card.getSynonym());
            contentValues.put(COLUMN_ANTONYM, card.getAntonym());
            contentValues.put(COLUMN_MEM, card.getMem());
            contentValues.put(COLUMN_HELP, card.getHelp());
            contentValues.put(COLUMN_PART, card.getPart());
            contentValues.put(COLUMN_GROUP, card.getGroup());
            return contentValues;
        }

    protected ContentValues fillContentValuesForChange(Card crd){
        VocabularyCard card = (VocabularyCard)crd;
        ContentValues contentValues = new ContentValues();
        contentValues.put(_ID, card.getId());
        contentValues.put(COLUMN_TRANSCRIPTION_CHANGE, card.getTranscriptionChange());
        contentValues.put(COLUMN_MEANING_CHANGE, card.getMeaningChange());
        contentValues.put(COLUMN_MEANING_NATIVE_CHANGE, card.getMeaningNativeChange());
        contentValues.put(COLUMN_TRANSLATE_CHANGE, card.getTranslateChange());
        contentValues.put(COLUMN_SYNONYM_CHANGE, card.getSynonymChange());
        contentValues.put(COLUMN_ANTONYM_CHANGE, card.getAntonymChange());
        contentValues.put(COLUMN_HELP_CHANGE, card.getHelpChange());
        contentValues.put(COLUMN_PART_CHANGE, card.getPartChange());
        contentValues.put(COLUMN_GROUP_CHANGE, card.getGroupChange());
        contentValues.put(COLUMN_IS_MINE, card.isMine);
        return contentValues;
    }
    protected ContentValues fillContentValuesForSentence(VocabularyCard.Sentence sentence){
        ContentValues contentValues = new ContentValues();
        contentValues.put(_ID, sentence.getId());
        contentValues.put(COLUMN__LINKED_CARD_ID, sentence.getLinked_id());
        contentValues.put(COLUMN_SENTENCE, sentence.getSentence());
        contentValues.put(COLUMN_SENTENCE_TRANSLATE, sentence.getTranslate());
        contentValues.put(COLUMN_SENTENCE_CHANGE, sentence.getChange());
        return contentValues;
    }
    protected ContentValues fillContentValuesForDialogue(GrammarCard.SelectTrain dialogue){
        ContentValues contentValues = new ContentValues();
        contentValues.put(_ID, dialogue.getId());
        contentValues.put(COLUMN_ANSWER, dialogue.getAnswer());
        contentValues.put(COLUMN_RIGHT, dialogue.getRight());
        contentValues.put(COLUMN_SECOND, dialogue.getSecond());
        contentValues.put(COLUMN_THIRD, dialogue.getThird());
        contentValues.put(COLUMN_FOURTH, dialogue.getFourth());
        return contentValues;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////// DELETE CARD /////////////////////////////////////////////////////////////////////////////
    private void deleteString(String id, SQLiteDatabase db, String tableName){
        String where = _ID + " = ?";
        String[] whereArgs = new String[]{id};
        db.delete(tableName, where, whereArgs);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /////// RELOAD /////////////////////////////////////////////////////////////////////////////////
    public void updateExamples(ArrayList<VocabularyCard.Sentence> senteces){
        for (VocabularyCard.Sentence s: senteces) {
            int success =  databaseExamples.update(EXAMPLES_TABLE_NAME, fillContentValuesForSentence(s),
                    _ID + " == ?", new String[]{s.getId()});
            if (success == 0)
            databaseExamples.insert(EXAMPLES_TABLE_NAME, null, fillContentValuesForSentence(s));
        }
    }

    public void updateChanges(Card card){
        if (databaseChanges.update(getChangesTableName(), fillContentValuesForChange(card),
                _ID + " == ?", new String[]{card.getId()}) <= 0)
            databaseChanges.insert(getChangesTableName(), null, fillContentValuesForChange(card));
    }
    public void updateDialogue(GrammarCard.SelectTrain s){
        if (s != null) {
                if (databaseDialogue.update(SELECT_TABLE_NAME, fillContentValuesForDialogue(s),
                        _ID + " == ?", new String[]{s.getId()}) == 0)
                    databaseDialogue.insert(SELECT_TABLE_NAME, null, fillContentValuesForDialogue(s));
            }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /////// GET CARDS //////////////////////////////////////////////////////////////////////////////
    public void getCardFromSelectById(VocabularyCard card){
        if (card.getDialogueTest() == null) return;
            Cursor cursor = databaseDialogue.query(DIALOGUE_TABLE_NAME, null, _ID + " == ?", new String[]{card.getId()},
                    null, null, null);
            if (cursor.moveToNext()) {
                card.setDialogueTest(getDialogue(cursor));
            }
            cursor.close();

    }

    public ArrayList<Card> getAllCardsFromCourse(String course) {
        Cursor cursor = databaseStatic.query(getStaticTableName(), null, COLUMN_COURSE + " == ?",
                new String[]{course},
                null, null, null);
        ArrayList<Card> cards = new ArrayList<>();
        while (cursor.moveToNext()) {
            cards.add(addStatic(cursor));
        }

        cursor.close();

        for (Card card :cards){
            getCardFromChangeById((VocabularyCard) card);
        }

        for (Card card :cards){
            getCardFromExamplesById((VocabularyCard) card);
        }

        for (Card card :cards){
            getCardFromTrainsById(card);
        }

        return cards;

    }


    public ArrayList<Card> getAllCardsForLib(String course) {
        Cursor cursor = databaseStatic.query(getStaticTableName(), null, COLUMN_COURSE + " == ?",
                new String[]{course},
                null, null, null);
        ArrayList<Card> cards = new ArrayList<>();
        while (cursor.moveToNext()) {
            cards.add(addStatic(cursor));
            Cursor cursor2 = databaseChanges.query(getChangesTableName(), null, _ID + " == ?",
                    new String[]{cards.get(cards.size()-1).getId()},
                    null, null, null);
            if (cursor2.moveToNext()){
                cards.get(cards.size()-1).isMine = cursor2.getInt(cursor2.getColumnIndex(COLUMN_IS_MINE)) > 0;
            }
            cursor2.close();
        }
        cursor.close();
        return cards;

    }


    public VocabularyCard getCard(String id) {
        VocabularyCard card = new VocabularyCard();

        if (!getCardFromStaticById(id, card))
            return null;
        if (databaseChanges != null)
            getCardFromChangeById(card);
        getCardFromExamplesById(card);
        getCardFromTrainsById(card);

        return card;
    }


    public VocabularyCard getNextStudyCard(){
        Cursor cursor = databaseProgress.query(getProgressTableName(), null, COLUMN_REPETITION_DAY + " <= ?", new String[]{Integer.toString(Ruler.getDay(context))},
                null, null, null);
        VocabularyCard card = null;
        while (cursor.moveToNext()) {
            card = (VocabularyCard) getCardFromProgress(cursor);
            if (card.getRepeatlevel() != NOUN_LEVEL && card.getRepeatlevel() != 0) {
                card = null;
                continue;
            }
            getCardFromStaticById(card.getId(), card);
            getCardFromExamplesById(card);
            getCardFromTrainsById(card);
            break;
        }
        cursor.close();

        return card;
    }

    public Card getNextRepeatCard(){
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
            getCardFromExamplesById((VocabularyCard) card);
            getCardFromTrainsById(card);
            break;
        }
        cursor.close();


        return card;
    }

    private void getCardFromChangeById(VocabularyCard card){
        Cursor cursor = databaseChanges.query(getChangesTableName(), null, _ID + " == ?", new String[]{card.getId()},
                null, null, null);
        if (cursor.moveToNext()) {
            addChanges(cursor, card);
        }
        cursor.close();
    }

    protected void getCardFromExamplesById(VocabularyCard card){
        Cursor cursor = databaseExamples.query(EXAMPLES_TABLE_NAME, null, COLUMN__LINKED_CARD_ID + " == ?", new String[]{card.getId()},
                null, null, null);
        if (cursor.moveToNext()) {
            addExamples(cursor, card);
        }

    }
    protected void getCardFromTrainsById(Card card){
        Cursor cursor = databaseTrain.query(TRAIN_TABLE_NAME, null, COLUMN__LINKED_CARD_ID + " == ?", new String[]{card.getId()},
                null, null, null);
        if (cursor.moveToNext()) {
            addTrains(cursor, card);
        }
        cursor.close();

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
            VocabularyCard card = (VocabularyCard) getCardFromProgress(cursor);
            if (CardRepeatManage.getLastDayOfRepeat(card.getRepetitionDat(), card.getRepeatlevel(), context, index) == Ruler.getDay(context)){
                getCardFromTrainsById(card);
                getCardFromStaticById(card.getId(), card);
                practiceCards.add(card);
                int index = card.getRepeatlevel() - consts.START_TEST;
                cardCount.set(index, cardCount.get(index) + 1);
            }
        }

        cursor.close();
        //closeDbTrain();

        return cardCount;
    }


    public int getCardsFromCourseForPractice(ArrayList<Card> practiceCards, String course) {
        int count = 0;
        Cursor cursor = databaseProgress.query(getProgressTableName(), null, COLUMN_COURSE + " == ?",
                new String[]{course},
                null, null, null);
        while (cursor.moveToNext()) {
            VocabularyCard card = (VocabularyCard) getCardFromProgress(cursor);
                getCardFromTrainsById(card);
                getCardFromStaticById(card.getId(), card);
                practiceCards.add(card);
                count++;
        }

        cursor.close();
        //closeDbTrain();
        return count;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////




    //////ПОЛУЧЕНИЕ КАРТЫ ПО КУРСОРУ ///////////////////////////////////////////////////////////////

    private void addExamples(Cursor cursor, VocabularyCard card) {
        String id = cursor.getString(cursor.getColumnIndex(_ID));
        String linked_id = cursor.getString(cursor.getColumnIndex(COLUMN__LINKED_CARD_ID));
        String sentence = cursor.getString(cursor.getColumnIndex(COLUMN_SENTENCE));
        String translate = cursor.getString(cursor.getColumnIndex(COLUMN_SENTENCE_TRANSLATE));
        Boolean changed = cursor.getInt(cursor.getColumnIndex(COLUMN_SENTENCE_CHANGE)) > 0;
        if (card.getExamples() != null)
            card.getExamples().add(new VocabularyCard.Sentence(sentence, translate, changed, id, linked_id));
        else {
            ArrayList<VocabularyCard.Sentence> sentences = new ArrayList<>();
            sentences.add(new VocabularyCard.Sentence(sentence, translate, changed, id, linked_id));
            card.setExamples(sentences);
        }
    }
    private void addTrains(Cursor cursor, VocabularyCard card) {
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

    protected void addStatic(Cursor cursor, Card card){
        String word = cursor.getString(cursor.getColumnIndex(COLUMN_WORD));
        String transcription = cursor.getString(cursor.getColumnIndex(COLUMN_TRANSCRIPTION));
        String meaning = cursor.getString(cursor.getColumnIndex(COLUMN_MEANING));
        String meaningNative = cursor.getString(cursor.getColumnIndex(COLUMN_MEANING_NATIVE));
        String translate = cursor.getString(cursor.getColumnIndex(COLUMN_TRANSLATE));
        String synonym = cursor.getString(cursor.getColumnIndex(COLUMN_SYNONYM));
        String antonym = cursor.getString(cursor.getColumnIndex(COLUMN_ANTONYM));
        String linked = cursor.getString(cursor.getColumnIndex(COLUMN_LINKED));
        String mem = cursor.getString(cursor.getColumnIndex(COLUMN_MEM));
        String help = cursor.getString(cursor.getColumnIndex(COLUMN_HELP));
        String group = cursor.getString(cursor.getColumnIndex(COLUMN_GROUP));
        String part = cursor.getString(cursor.getColumnIndex(COLUMN_PART));

        ((VocabularyCard)card).addStatic(word, transcription, meaning, meaningNative, translate, synonym, antonym, mem,
                help, group, part);

        ((VocabularyCard) card).setLinked(linked);

        if (card.getId() == null) {
            card.setId(cursor.getString(cursor.getColumnIndex(_ID)));
            card.setCourse(cursor.getString(cursor.getColumnIndex(COLUMN_COURSE)));
        }
    }

    protected Card addStatic(Cursor cursor){
        String id = cursor.getString(cursor.getColumnIndex(_ID));
        String course = cursor.getString(cursor.getColumnIndex(COLUMN_COURSE));
        Card card = new VocabularyCard(id, course);
        addStatic(cursor, card);
        return card;
    }


    private void addChanges(Cursor cursor, VocabularyCard card){
        boolean transcriptionC = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSCRIPTION_CHANGE)) > 0;
        boolean meaningC = cursor.getInt(cursor.getColumnIndex(COLUMN_MEANING_CHANGE)) > 0;
        boolean meaningNativeC = cursor.getInt(cursor.getColumnIndex(COLUMN_MEANING_NATIVE_CHANGE)) > 0;
        boolean translateC = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSLATE_CHANGE)) > 0;
        boolean synonymC = cursor.getInt(cursor.getColumnIndex(COLUMN_SYNONYM_CHANGE)) > 0;
        boolean antonymC = cursor.getInt(cursor.getColumnIndex(COLUMN_ANTONYM_CHANGE)) > 0;
        boolean helpC = cursor.getInt(cursor.getColumnIndex(COLUMN_HELP_CHANGE)) > 0;
        boolean groupC = cursor.getInt(cursor.getColumnIndex(COLUMN_GROUP_CHANGE)) > 0;
        boolean partC = cursor.getInt(cursor.getColumnIndex(COLUMN_PART_CHANGE)) > 0;
        boolean isMine = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_MINE)) > 0;

        card.addChanges(transcriptionC, meaningC, meaningNativeC, translateC, synonymC, antonymC, helpC,
                groupC, partC, isMine);
    }

    private GrammarCard.SelectTrain getDialogue(Cursor cursor){
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


    public void addNewCard(VocabularyCard card) {
        addNewCardToStatic(card);
        updateExamples(card.getExamples());
        updateTrains(card.getTrains());
    }

    public void deleteDb() {
        super.deleteDb();
        databaseExamples.delete(EXAMPLES_TABLE_NAME, null, null);
        databaseChanges.delete(getChangesTableName(), null, null);
    }


    /*
    public boolean getOverWrite(String id) {
        Cursor cursor = databaseChanges.query(CHANGES_TABLE_NAME, null, _ID + " == ?", new String[]{id},
                    null, null, null);
            boolean isOverWrite = cursor.moveToNext();
            cursor.close();
            return isOverWrite;
    }

     */


/*
    public void recountCardCount() {
        int leftStudy = transportPreferences.getTodayLeftVocabularyStudyCardQuantityPreference(context);

        dbHelperProgress        = new DBHelperProgressVocabulary(context);
        databaseProgress        = dbHelperProgress.getReadableDatabase();


        Cursor cursor = databaseProgress.query(PROGRESS_TABLE_NAME, new String[]{COLUMN_REPEAT_LEVEL}, COLUMN_REPETITION_DAY + " <= ?", new String[]{Integer.toString(Ruler.getDay(context))},
                null, null, null);
        int repeat = 0;
        //int learn = transportPreferences.getPaceVocabulary(context) - leftStudy;
        while (cursor.moveToNext()){
            if (cursor.getInt(cursor.getColumnIndex(COLUMN_REPEAT_LEVEL)) == NOUN_LEVEL) {
                if (transportPreferences.getPaceVocabulary(context) > learn)
                    learn++;
            }
            else
                repeat++;
        }
        cursor.close();

        transportPreferences.setTodayLeftVocabularyStudyCardQuantityPreference(context, learn);
        transportPreferences.setTodayLeftVocabularyRepeatCardQuantityPreference(context, repeat);

        databaseProgress.close();
    }
 */


    public Card getRandomCardFromStatic() {
        Card card = null;
        Cursor cursor = databaseStatic.rawQuery(GET_RANDOM_FROM_STATIC, null);
        if (cursor.moveToNext()){
            card = addStatic(cursor);
        }
        cursor.close();
        return card;
    }


    public String getRandomExample() {
        String sentence = null;
        Cursor cursor = databaseExamples.query(EXAMPLES_TABLE_NAME, new String[]{COLUMN_SENTENCE}, null, null, null, null, "RANDOM()", "1");

        while (cursor.moveToNext()){
            sentence = cursor.getString(cursor.getColumnIndex(COLUMN_SENTENCE));
            if (sentence != null) break;
        }
        cursor.close();
        return sentence;

    }

    public String getRandomTrain() {
        if (DatabaseUtils.queryNumEntries(databaseTrain, TRAIN_TABLE_NAME) < 3) return null;

        String sentence = null;
        Cursor cursor = databaseTrain.query(TRAIN_TABLE_NAME, new String[]{COLUMN_SENTENCE}, null, null, null, null, "RANDOM()", "1");

        while (cursor.moveToNext()){
            sentence = cursor.getString(cursor.getColumnIndex(COLUMN_SENTENCE));
            if (sentence != null) break;
        }
        cursor.close();
        return sentence;
    }

    public boolean isMine(String id){
        boolean isMine = false;
        Cursor cursor2 = databaseChanges.query(getChangesTableName(), null, _ID + " == ?",
                new String[]{id},
                null, null, null);
        if (cursor2.moveToNext()){
            isMine = cursor2.getInt(cursor2.getColumnIndex(COLUMN_IS_MINE)) > 0;
        }
        cursor2.close();

        return isMine;
    }


    public void setAllSentences(VocabularyCard card){
        Cursor cursor = databaseTrain.query(TRAIN_TABLE_NAME, null, COLUMN__LINKED_CARD_ID + " == ?",  new String[]{card.getId()}, null, null, null, null);
        if (cursor.moveToNext()){
            addTrains(cursor, card);
        }
        cursor.close();

        cursor = databaseExamples.query(EXAMPLES_TABLE_NAME, null, COLUMN__LINKED_CARD_ID + " == ?",  new String[]{card.getId()}, null, null, null, null);
        if (cursor.moveToNext()){
            addExamples(cursor, card);
        }
        cursor.close();
    }

    public boolean checkForNear(VocabularyCardLibUnit vocabularyCardLibUnit, String course) {
        boolean isNear;
        Cursor cursor = databaseStatic.query(STATIC_TABLE_NAME, null, COLUMN_WORD + " == '" + vocabularyCardLibUnit.getWord() + "' AND " + COLUMN_TRANSLATE + " == '" + vocabularyCardLibUnit.getTranslate()+ "' AND " + COLUMN_COURSE + " != '" +  course + "'",  null, null, null, null, null);

        isNear = cursor.moveToNext();
        cursor.close();

        return  isNear;
    }
}
