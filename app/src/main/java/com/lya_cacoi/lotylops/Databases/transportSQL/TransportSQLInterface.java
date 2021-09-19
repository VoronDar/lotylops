package com.lya_cacoi.lotylops.Databases.transportSQL;

import com.lya_cacoi.lotylops.cards.Card;

import java.util.ArrayList;

/*
Интерфейс ради реализации стретегии для всех transportSql

! Закомментированные методы должны быть в каждом классе, но так как они приватные, то здесь они не
    описываются
Да кому вообще нужны это наследование?

! также у всех классов есть еще абстрактный класс transportSQL, в котором есть некоторые
    общие функции

Здесь закомментированные функции помещены буквами
H - переведенные из public в private
P - приватные
U - на данный момент не используемые

 */


public interface TransportSQLInterface {
    //H public void createTables(Context context);                                                  // create tables keys
    void closeDatabases();                                                                          // close tables in the end of work
    void deleteCommon();                                                                            // delete the common table entries
    void deletePractise();                                                                          // delete the practice table entries
    void deleteStudy();                                                                             // delete the study table entries
    void deleteRepeat();                                                                            // delete the repeat table entries
    //P private Card getCard(Cursor cursor);                                                        // get card from study\repeat\common tables
    //P private Card getCardFromPractice(Cursor cursor);                                            // get card from the practice table
    Card getString(String id);                                                                      // get (abstract) card from database by ID
    Card getStringFromStudy(String id);                                                             // get (abstract) card from study database by ID
    Card getStringFromRepeat(String id);                                                            // get (abstract) card from repeat database by ID
    Card getStringFromPractiseById(String id);                                                      // get card from practise database by id
    public Card getStringFromPractise();                                                            // get card from practise database
    //P private void pushPracticeCardBack(Card unit);                                               // push card in the back of queue (practice) - when it is not allow to use
    //P private Card getPracticeCardBySettings(selection, selectionArgs);                           // get practise card by selection and selectionArgs
    Card getStringFromPractiseByLevel(int repeatLevel);                                             // get card from practise database by repeat level
    Card getTodayString(int day);                                                                   // get (abstract) card from database
    Card getRandomCard();                                                                           // get random card from common table
    //P private ContentValues fillContentValues(Card card);                                         // fill contentValues for common table template
    void addString(Card card);                                                                      // insert string in common table
    void addStringStudy(Card card);                                                                 // insert string in study table
    void addStringRepeat(Card card);                                                                // insert string in repeat table
    void addStringPractice(Card card);                                                              // insert string in practice table
    void deleteStringCommon(String id);                                                             // delete string from the common table
    void deleteStringFromStudy(String id);                                                          // delete string from the  study table
    void deleteStringFromRepeat(String id);                                                         // delete string from the repeat table
    void deleteStringFromPractice(String id);                                                       // delete string from the repeat table
    //U public void reloadString(Card card);                                                        // delete card and put changed back from the common table
    void reloadStudyString(Card card);                                                              // delete card and put changed back from the study table
    void reloadRepeatString(Card card);                                                             // delete card and put changed back from the repeat table
    ArrayList<Card> getAllCardsFromCommon();                                                        // get all cards from common table
    ArrayList<Card> getAllTodayRepeatCards();                                                       // get all today repeat card
    int fillTodayStudyDatabase();                                                                   // fill today study database in start of day
    int fillTodayRepeatDatabase();                                                                  // fill today repeat database in start of day
    void returnTodayStudyDatabase();                                                                // return card to the common table from study
    void returnTodayRepeatDatabase();                                                               // return card to the common table from repeat
    int getStudyCardCount();                                                                        // get study card count
    int getPracticeCardCount();                                                                     // get practice card count
    int getRepeatCardCount();                                                                       // get repeat card count
    int getCardCount();                                                                             // get all card count
    //U public int returnCardCount();                                                               // get card count
    public int fillPracticeDBForExams();                                                            // fill practice db for exams\or all cards train
    ArrayList<Integer> fillPracticeDBForNextTest();                                                 // fill practice db for the next test
    int getIndex();                                                                                 // get chapter (vocabulary/phraseology) index
    void reduceLeftStudyCount();                                                                    // set tranport preference studyCount --
    void reduceLeftRepeatCount();                                                                   // set tranport preference repeatCount --
    void reloadMem(String id, String mem);                                                          // reload card with a new mem
    ArrayList<String> getAllCardsId();                                                              // get all cards id
    ArrayList<String> getAllCardsWords();                                                           // get all cards wordsArrayList<Card> getAllCardsByWord(String word);                                                 // get all cards with the same word
    ArrayList<Card> getAllCardsByWord(String word);                                                 // get all cards with the same word
}

