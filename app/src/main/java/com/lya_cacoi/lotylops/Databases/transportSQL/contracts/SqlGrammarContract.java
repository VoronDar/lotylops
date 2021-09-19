package com.lya_cacoi.lotylops.Databases.transportSQL.contracts;

import android.provider.BaseColumns;

import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.PhraseologyContract.PhraseologyEntry.COLUMN_LINKED;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_COURSE;

public class SqlGrammarContract {
    public static final class GrammarEntry extends Entry implements BaseColumns {
        /*

        виды баз данных:

        статическая - туда складывается вся информация о карте, представленная в еденичном экземпляре,
        которая не предполагается к изменению

        прогресс - курс - уровень повторения/практики + день повторения. сюда складываются только активные карты, вброшенные в обучение

        тренировочная одна база с вокабуляром на двоих

        selectTrain - тесты с вариантами ответов



        ВАЖНО - practiceLevel в грамматике означает номер задания, на котором пользователь остановился в прошлый раз - задания идут циклично

         */

        public static final String STATIC_TABLE_NAME = "sqlqrst";
        public static final String PROGRESS_TABLE_NAME = "sqlqrpr";
        public static final String SELECT_TABLE_NAME = "sqlqrsl";
        public static final String DIALOGUE_TABLE_NAME = "sqldialogue";


        public static final String EXCEPTION_STATIC_TABLE_NAME = "sqlqrst";
        public static final String EXCEPTION_PROGRESS_TABLE_NAME = "sqlqrpr";
        public static final String COLUMN_QUESTION = "question";

        public static final String COLUMN_THEORY = "theory";
        public static final String COLUMN_SELECT_ID = "selectId";
        public static final String COLUMN_MISTAKE_ID = "mistakeId";
        public static final String COLUMN_BLOCK_ID = "blockId";
        public static final String COLUMN_TRAIN_ID = "trainId";


        public static final String COLUMN_RIGHT = "right";
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_SECOND = "second";
        public static final String COLUMN_THIRD = "third";
        public static final String COLUMN_FOURTH = "fourth";

        public static final String CREATE_STATIC_COMMAND = "CREATE TABLE IF NOT EXISTS " + STATIC_TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN_COURSE + " " + TYPE_TEXT + ", " +
                COLUMN_TRAIN_ID + " " + TYPE_TEXT + ", " +
                COLUMN_BLOCK_ID + " " + TYPE_TEXT + ", " +
                COLUMN_LINKED + " " + TYPE_TEXT + ", " +
                COLUMN_SELECT_ID + " " + TYPE_TEXT + ", " +
                COLUMN_THEORY + " " + TYPE_TEXT + ")";

        public static final String CREATE_PROGRESS_COMAND = "CREATE TABLE IF NOT EXISTS " + PROGRESS_TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN_COURSE + " " + TYPE_TEXT + ", " +
                COLUMN_REPEAT_LEVEL + " " + TYPE_INTEGER + ", " +
                COLUMN_PRACTICE_LEVEL + " " + TYPE_INTEGER + ", " +
                COLUMN_REPETITION_DAY + " " + TYPE_INTEGER + ")";




        public static final String CREATE_SELECT_COMMAND = "CREATE TABLE IF NOT EXISTS " + SELECT_TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN_RIGHT + " " + TYPE_TEXT + ", " +
                COLUMN_ANSWER + " " + TYPE_TEXT + ", " +
                COLUMN_SECOND + " " + TYPE_TEXT + ", " +
                COLUMN_THIRD + " " + TYPE_TEXT + ", " +
                COLUMN_FOURTH + " " + TYPE_TEXT  + ")";

        public static final String CREATE_DIALOGUE = "CREATE TABLE IF NOT EXISTS " + DIALOGUE_TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN_RIGHT + " " + TYPE_TEXT + ", " +
                COLUMN_ANSWER + " " + TYPE_TEXT + ", " +
                COLUMN_SECOND + " " + TYPE_TEXT + ", " +
                COLUMN_THIRD + " " + TYPE_TEXT + ", " +
                COLUMN_FOURTH + " " + TYPE_TEXT  + ")";


        public static final String CREATE_EXCEPTION_STATIC_COMMAND = "CREATE TABLE IF NOT EXISTS " + EXCEPTION_STATIC_TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN_COURSE + " " + TYPE_TEXT + ", " +
                COLUMN_QUESTION + " " + TYPE_TEXT + ", " +
                COLUMN_ANSWER + " " + TYPE_TEXT + ")";

        public static final String CREATE_EXCEPTION_PROGRESS_COMAND = "CREATE TABLE IF NOT EXISTS " + EXCEPTION_PROGRESS_TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN_COURSE + " " + TYPE_TEXT + ", " +
                COLUMN_REPEAT_LEVEL + " " + TYPE_INTEGER + ", " +
                COLUMN_PRACTICE_LEVEL + " " + TYPE_INTEGER + ", " +
                COLUMN_REPETITION_DAY + " " + TYPE_INTEGER + ")";


        public static final String DROP_STATIC_TABLE = "DROP TABLE IF EXISTS " + STATIC_TABLE_NAME;
        public static final String DROP_DIALOGUE_TABLE = "DROP TABLE IF EXISTS " + DIALOGUE_TABLE_NAME;
        public static final String DROP_PROGRESS_TABLE = "DROP TABLE IF EXISTS " + PROGRESS_TABLE_NAME;
        public static final String DROP_SELECT_TABLE = "DROP TABLE IF EXISTS " + SELECT_TABLE_NAME;


        public static final String DROP_EXCEPTION_STATIC_TABLE = "DROP TABLE IF EXISTS " + EXCEPTION_STATIC_TABLE_NAME;
        public static final String DROP_EXCEPTION_PROGRESS_TABLE = "DROP TABLE IF EXISTS " + EXCEPTION_PROGRESS_TABLE_NAME;

    }

}