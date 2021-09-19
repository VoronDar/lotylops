package com.lya_cacoi.lotylops.Databases.transportSQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.ruler.Ruler;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.Entry.COLUMN_REPEAT_LEVEL;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.Entry.COLUMN_REPETITION_DAY;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.VocabularyContract.VocabularyEntry.COLUMN_WORD;

/*

this class maintain link between databases and the Ruler class

DON'T USE THESE FUNCTIONS IN THE UPPER INTERFACE - USE RULER!

And! FUNCTIONS DOCS READ IN INTERFACE

 */

public abstract class TransportSQL{


    protected SQLiteDatabase                    database;
    protected SQLiteDatabase                    databaseStudy;
    protected SQLiteDatabase                    databaseRepeat;
    protected SQLiteDatabase                    databasePractice;
    protected SQLiteOpenHelper                  dbHelper;
    protected SQLiteOpenHelper                  dbHelperPractice;
    protected SQLiteOpenHelper                  dbHelperRepeat;
    protected SQLiteOpenHelper                  dbHelperStudy;
    protected Context                           context;




    //----------------------------------------------------------------------------------------------





    /////// ABSTRACT GET TABLE NAMES ///////////////////////////////////////////////////////////////
    protected abstract String getTableName();
    protected abstract String getStudyTableName();
    protected abstract String getRepeatTableName();
    protected abstract String getPracticeTableName();
    ////////////////////////////////////////////////////////////////////////////////////////////////



    /////// ABSTRACT GET CARD BY CURSOR ////////////////////////////////////////////////////////////
    protected abstract Card getCard(Cursor cursor);
    protected abstract Card getCardFromPractise(Cursor cursor);
    ////////////////////////////////////////////////////////////////////////////////////////////////



    /////// ABSTRACT FILL CONTENT VALUES ///////////////////////////////////////////////////////////
    protected abstract ContentValues fillContentValues(Card card);
    protected abstract ContentValues fillContentValuesForPractice(Card card);
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /////// ABSTRACT GET INDEX /////////////////////////////////////////////////////////////////////
    protected abstract int getIndex();



    //----------------------------------------------------------------------------------------------





    /////// THE MAIN LIFECYCLE-MANAGERS ////////////////////////////////////////////////////////////
    protected TransportSQL(Context context) {
        this.context = context;
    }
    public void closeDatabases(){
        database.close();
        databasePractice.close();
        databaseRepeat.close();
        databaseStudy.close();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    //////// DELETE TABLES /////////////////////////////////////////////////////////////////////////
    public void deleteCommon(){
        database.delete(getTableName(), null, null);
    }
    public void deletePractise(){
        databasePractice.delete(getPracticeTableName(), null, null);
    }
    public void deleteStudy(){
        databaseStudy.delete(getStudyTableName(), null, null);
    }
    public void deleteRepeat(){
        databaseRepeat.delete(getRepeatTableName(), null, null);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    //////// INSERT STRING IN TABLES ///////////////////////////////////////////////////////////////
    public void addString(Card card){
        database.insert(getTableName(), null, fillContentValues(card));
    }
    public void addStringStudy(Card card){
        databaseStudy.insert(getStudyTableName(), null, fillContentValues(card));
    }
    public void addStringRepeat(Card card){
        databaseRepeat.insert(getRepeatTableName(), null, fillContentValues(card));
    }
    public void addStringPractice(Card card){
        databasePractice.insert(getPracticeTableName(), null, fillContentValuesForPractice(card));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    //////// DELETE STRING FROM TABLES /////////////////////////////////////////////////////////////
    private void deleteString(String id, SQLiteDatabase db, String tableName){
        String where = _ID + " = ?";
        String[] whereArgs = new String[]{id};
        db.delete(tableName, where, whereArgs);
    }
    public  void deleteStringCommon(String id) {
        deleteString(id, database, getTableName());
    }
    public  void deleteStringFromStudy(String id) {
        deleteString(id, databaseStudy, getStudyTableName());
    }
    public  void deleteStringFromRepeat(String id) {
        deleteString(id, databaseRepeat, getRepeatTableName());
    }
    public  void deleteStringFromPractice(String id){
        deleteString(id, databasePractice, getPracticeTableName());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    //////// GET CARDS /////////////////////////////////////////////////////////////////////////////
    protected Card getCardByID(String ID, SQLiteDatabase database, String TableName){
        Card result = null;
        Cursor cursor = database.query(TableName, null,
                getByIdSelection(), getSingleSelectionArg(ID),
                null, null, null);
        if (cursor.moveToNext()) {
            result = getCard(cursor);
        }
        cursor.close();

        return result;
    }
    protected Card getRandomCard(SQLiteDatabase database, String getRandomCommand){
        Card card = null;
        Cursor cursor = database.rawQuery(getRandomCommand, null);
        if (cursor.moveToNext()){
            card = getCard(cursor);
        }
        cursor.close();
        return card;
    }
    public ArrayList<Card> getAllCards(SQLiteDatabase database, String TableName){
        Cursor cursor = database.query(TableName, null, null, null,
                null, null, null);
        ArrayList<Card> units = getAllCardsFromTable(cursor);
        cursor.close();
        return units;
    }

    public ArrayList<String> getAllCardsId(){
        Cursor cursor = database.query(getTableName(), new String[]{_ID}, null, null,
                null, null, null);
        ArrayList<String> units = getAllCardId(cursor);
        cursor.close();
        return units;
    }
    public ArrayList<String> getAllCardsWords(){
        Cursor cursor = database.query(getTableName(), new String[]{COLUMN_WORD}, null, null,
                null, null, null);
        ArrayList<String> units = getAllCardsWord(cursor);
        cursor.close();
        return units;
    }
    public ArrayList<Card> getAllCardsByWord(String word){
        Cursor cursor = database.query(getTableName(), null, COLUMN_WORD + " == ?", getSingleSelectionArg(word),
                null, null, null);
        ArrayList<Card> units = getAllCardsFromTable(cursor);
        cursor.close();
        return units;
    }

    public Card getTodayString(int day){
        Card result = null;

        String selection       = COLUMN_REPETITION_DAY + " <= ?";
        String[] selectionArgs = new String[]{Integer.toString(day)};
        Cursor cursor;

        if (Ruler.dayState == consts.dayState.study)
            cursor = databaseStudy.query(getStudyTableName(),null, selection, selectionArgs,
                    null, null, null);
        else
            cursor = databaseRepeat.query(getRepeatTableName(),null, selection, selectionArgs,
                    null, null, null);

        if (cursor.moveToNext()) {
            result = getCard(cursor);
        }
        cursor.close();

        return result;
    }
    public ArrayList<Card> getAllTodayRepeatCards(){
        Cursor cursor = databaseRepeat.query(getRepeatTableName(), null, null,
                null,null, null, null);

        ArrayList<Card> units = getAllCardsFromTable(cursor);
        cursor.close();

        return units;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    //////// GET SELECTIONS ////////////////////////////////////////////////////////////////////////
    protected String getByIdSelection(){
        return (_ID + " == ?");
    }
    protected String getByRepeatLevelSelection(){
        return (COLUMN_REPEAT_LEVEL + " == ?");
    }
    protected String[] getSingleSelectionArg(String arg){
        return new String[]{arg};
    }
    protected String[] getSingleSelectionArg(int arg){
        return new String[]{Integer.toString(arg)};
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    //////// GET PRACTICE CARD /////////////////////////////////////////////////////////////////////
    private Card getPracticeCardBySettings(String selection, String[] selectionArgs){

        Cursor cursor = databasePractice.query(getPracticeTableName(), null, selection,
                selectionArgs,null, null, null);

        Card unit = null;
        if (cursor.moveToNext()) {
            unit = (getCardFromPractise(cursor));
        }

        cursor.close();

        if (unit != null){
            deleteStringFromPractice(unit.getId());
            addStringPractice(unit);
        }

        return unit;
    }
    public Card getStringFromPractiseByLevel(int repeatLevel){
        return getPracticeCardBySettings(getByRepeatLevelSelection(),
                getSingleSelectionArg(repeatLevel));
    }
    public Card getStringFromPractise(){
        return getPracticeCardBySettings(null, null);
    }
    public Card getStringFromPractiseById(String id){
        return getPracticeCardBySettings(getByIdSelection(), getSingleSelectionArg(id));
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    //////// PUT CARDS BACH IN TABLES (RELOAD) /////////////////////////////////////////////////////
    public void reloadStudyString(Card card){
        deleteStringFromStudy(card.getId());
        //CardRepeatManage.upDay(card, context, getIndex());
        addStringStudy(card);
    }
    public void reloadRepeatString(Card card){
        deleteStringFromRepeat(card.getId());
        //CardRepeatManage.upDay(card, context, getIndex());
        addStringRepeat(card);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    //////// GET CARDS COUNT ///////////////////////////////////////////////////////////////////////
    public int getCardCount(){
        return (int)DatabaseUtils.queryNumEntries(database, getTableName());
    }
    public int getStudyCardCount(){
        return (int)DatabaseUtils.queryNumEntries(databaseStudy,getStudyTableName());
    }
    public int getRepeatCardCount(){
        return (int)DatabaseUtils.queryNumEntries(databaseRepeat, getRepeatTableName());
    }
    public int getPracticeCardCount(){
        return (int)DatabaseUtils.queryNumEntries(databasePractice, getPracticeTableName());
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////// FILL TODAY TABLES ///////////////////////////////////////////////////////////////////////
    protected int fillTodayStudyDatabase(int pace){

        ArrayList<Card> cards = new ArrayList<>();
        Cursor cursor = database.query(getTableName(),
                null, getByRepeatLevelSelection(), getSingleSelectionArg(consts.NOUN_LEVEL),
                null, null, null);

        int n = 0;

        while (cursor.moveToNext() && (n++ < pace))
            cards.add(getCard(cursor));
        cursor.close();


        for (int i = 0; i < cards.size(); i++){
            addStringStudy(cards.get(i));
        }
        return cards.size();
    }
    protected int fillTodayRepeatDatabaseIs(){

        String selection = COLUMN_REPETITION_DAY + " <= ? AND "
                + COLUMN_REPETITION_DAY + " > ?";
        String[] selectionArgs = new String[]{Integer.toString(Ruler.getDay(context)), "0"};

        Cursor cursor = database.query(getTableName(),
                null, selection, selectionArgs, null, null, null);

        ArrayList<Card> cards = getAllCardsFromTable(cursor);
        cursor.close();


        for (int i = 0; i < cards.size(); i++) {
            addStringRepeat(cards.get(i));
        }

        return cards.size();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    ////// LET TODAY TABLES ////////////////////////////////////////////////////////////////////////
    protected void letTodayStudyDatabase(boolean isStudy){

        Cursor cursor;
        if (isStudy){
            cursor = databaseStudy.query(getStudyTableName(),
                null, null, null, null, null,
                    null);
        }
        else{
            cursor = databaseRepeat.query(getRepeatTableName(),
                    null, null, null, null, null,
                    null);
        }

        ArrayList<Card> cards = getAllCardsFromTable(cursor);
        cursor.close();

        for (int i = 0; i < cards.size(); i++){
            deleteStringCommon(cards.get(i).getId());
            addString(cards.get(i));
        }}


    ////// FILL PRACTICE TABLES ////////////////////////////////////////////////////////////////////
    public int fillPracticeDBForExams(){
        deletePractise();
        Cursor cursor = database.query(getTableName(),null, null,
                null, null, null, null);
        while (cursor.moveToNext()){
            addStringPractice(getCard(cursor));
        }
        cursor.close();
        return getPracticeCardCount();

    }
    public ArrayList<Integer> fillPracticeDBForNextTest(){
        deletePractise();

        ArrayList<Integer> cardCount = new ArrayList<>();
        final int typesCount = 5;
        for (int i = 0; i < typesCount; i++) {
            cardCount.add(new Integer(0));
        }

        for (int d = 0; d < 2; d++) {
            Cursor cursor;
            if (d == 0)
                cursor = databaseRepeat.query(getRepeatTableName(), null, null,
                        null, null, null, null);
            else
                cursor = databaseStudy.query(getStudyTableName(), null, null,
                        null, null, null, null);

            while (cursor.moveToNext()) {
                Card card = getCard(cursor);
                if (card.getRepeatlevel() <= consts.END_TEST) {
                    addStringPractice(getCard(cursor));
                    int index = card.getRepeatlevel() - consts.START_TEST;
                    cardCount.set(index, cardCount.get(index) + 1);
                }
            }
            cursor.close();
        }
        closeDatabases();


        return cardCount;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    ////// GET ALL CARDS FROM TABLE BY CURSOR //////////////////////////////////////////////////////
    private ArrayList<Card> getAllCardsFromTable(Cursor cursor){
        ArrayList<Card> cards = new ArrayList<>();
        while (cursor.moveToNext()) {
            cards.add(getCard(cursor));
        }
        return cards;
    }
    private ArrayList<String> getAllCardsWord(Cursor cursor){
        ArrayList<String> cards = new ArrayList<>();
        while (cursor.moveToNext()) {
            cards.add(cursor.getString(cursor.getColumnIndex(COLUMN_WORD)));
        }
        return cards;
    }
    private ArrayList<String> getAllCardId(Cursor cursor){
        ArrayList<String> cards = new ArrayList<>();
        while (cursor.moveToNext()) {
            cards.add(cursor.getString(cursor.getColumnIndex(_ID)));
        }
        return cards;
    }


    ////// PUT ASSOCIATION BACK ////////////////////////////////////////////////////////////////////
    public void reloadMem(String id, String mem){
        Card card = getCardByID(id, database, getTableName());
        card.setMem(mem);
        deleteStringCommon(id);
        addString(card);
    }


}
