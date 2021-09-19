package com.lya_cacoi.lotylops.Databases.transportSQL.contracts;

import android.provider.BaseColumns;

import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.PhraseologyContract.PhraseologyEntry.COLUMN_LINKED;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.SqlVocabularyContract.VocabularyEntry.COLUMN_COURSE;

public class SqlPhoneticContract {
    public static final class PhoneticEntry extends Entry implements BaseColumns {
        /*

        виды баз данных:

        статическая - туда складывается вся информация о карте, представленная в еденичном экземпляре,
        которая не предполагается к изменению

        прогресс - курс - уровень повторения/практики + день повторения. сюда складываются только активные карты, вброшенные в обучение

        тренировочная одна база с вокабуляром на двоих

        selectTrain - тесты с вариантами ответов

            у культуры тоже есть selectTrain, так что
         */

        public static final String STATIC_TABLE_NAME = "sqlphontst";
        public static final String PROGRESS_TABLE_NAME = "sqlphonst";


        public static final String COLUMN_THEORY = "theory";
        public static final String COLUMN_EXAMPLES = "examples";
        public static final String COLUMN_TRAINS = "trains";
        public static final String COLUMN_TRANSCRIPT = "transcription";
        public static final String COLUMN_SELECT_TRAINS = "selectTrains";

        public static final String CREATE_STATIC_COMMAND = "CREATE TABLE IF NOT EXISTS " + STATIC_TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN_COURSE + " " + TYPE_TEXT + ", " +
                COLUMN_EXAMPLES + " " + TYPE_TEXT + ", " +
                COLUMN_TRANSCRIPT + " " + TYPE_TEXT + ", " +
                COLUMN_LINKED + " " + TYPE_TEXT + ", " +
                COLUMN_TRAINS + " " + TYPE_TEXT + ", " +
                COLUMN_SELECT_TRAINS + " " + TYPE_TEXT + ", " +
                COLUMN_THEORY + " " + TYPE_TEXT + ")";

        public static final String CREATE_PROGRESS_COMAND = "CREATE TABLE IF NOT EXISTS " + PROGRESS_TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN_COURSE + " " + TYPE_TEXT + ", " +
                COLUMN_REPEAT_LEVEL + " " + TYPE_INTEGER + ", " +
                COLUMN_PRACTICE_LEVEL + " " + TYPE_INTEGER + ", " +
                COLUMN_REPETITION_DAY + " " + TYPE_INTEGER + ")";

        public static final String DROP_STATIC_TABLE = "DROP TABLE IF EXISTS " + STATIC_TABLE_NAME;
        public static final String DROP_PROGRESS_TABLE = "DROP TABLE IF EXISTS " + PROGRESS_TABLE_NAME;

    }

}