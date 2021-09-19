package com.lya_cacoi.lotylops.Databases.transportSQL.contracts;

import android.provider.BaseColumns;

public class VocabularyContract {
    public static final class VocabularyEntry extends Entry implements BaseColumns {

        // поддерживает 4 вида даз банных:
        // 1 - общая, 2- обучение, 3 - повторение, - хранят всю таблицу целиком
        // 4 тип хранит только информацию для тестов и + 1 - состояние прохождения практики
        // с началом сессии свободной практики она заполняется при завершении результаты практики анализируются
        // и снова закидываются в общую таблицу (для некоторых)


        public static final String TABLE_NAME                       = "vocabulary";                 // common table (vocabulary)
        public static final String TODAY_TABLE_STUDY_NAME           = "vocabularystudytoday";       // study (vocabulary)
        public static final String TODAY_REPEAT_TABLE_NAME          = "vocabularyrepeattoday";      // repeat (vocabulary)
        public static final String PRACTICE_TABLE_NAME              = "vocabularypractice";         // practice (vocabulary)

        public static final String COLUMN_WORD                      = "word";
        public static final String COLUMN_TRANSCRIPTION             = "transcription";
        public static final String COLUMN_MEANING                   = "meaning";
        public static final String COLUMN_MEANING_NATIVE            = "meaningnative";
        public static final String COLUMN_TRANSLATE                 = "translate";
        public static final String COLUMN_SYNONYM                   = "synonym";
        public static final String COLUMN_ANTONYM                   = "antonym";
        public static final String COLUMN_MEM                       = "mem";                        // association
        public static final String COLUMN_HELP                      = "help";
        public static final String COLUMN_GROUP                     = "groupword";                  // formal\humorous..
        public static final String COLUMN_PART                      = "part";                       // part of speech
        public static final String COLUMN_EXAMPLE                   = "example";
        public static final String COLUMN_EXAMPLE_TRANSLATE         = "exampletranslate";


        public static final String COLUMN_WRITE_SENTENCE            = "writesentence";
        public static final String COLUMN_WRITE_SENTENCE_NATIVE     = "writesentencenative";

        public static final String COLUMN_IS_MINE                   = "iamine";


        private static final String CREATE_HEADER_COMMAND = " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN_WORD + " " + TYPE_TEXT + ", " +
                COLUMN_TRANSCRIPTION + " " + TYPE_TEXT + ", " +
                COLUMN_MEANING + " " + TYPE_TEXT + ", " +
                COLUMN_MEANING_NATIVE + " " + TYPE_TEXT + ", " +
                COLUMN_TRANSLATE + " " + TYPE_TEXT + ", " +
                COLUMN_SYNONYM + " " + TYPE_TEXT + ", " +
                COLUMN_ANTONYM + " " + TYPE_TEXT + ", " +
                COLUMN_MEM + " " + TYPE_TEXT + ", " +
                COLUMN_HELP + " " + TYPE_TEXT + ", " +
                COLUMN_GROUP + " " + TYPE_TEXT + ", " +
                COLUMN_PART + " " + TYPE_TEXT + ", " +
                COLUMN_EXAMPLE + " " + TYPE_TEXT + ", " +
                COLUMN_EXAMPLE_TRANSLATE + " " + TYPE_TEXT + ", " +
                COLUMN_WRITE_SENTENCE + " " + TYPE_TEXT + ", " +
                COLUMN_WRITE_SENTENCE_NATIVE + " " + TYPE_TEXT + ", " +
                COLUMN_REPEAT_LEVEL + " " + TYPE_INTEGER + ", " +
                COLUMN_PRACTICE_LEVEL + " " + TYPE_INTEGER + ", " +
                COLUMN_IS_MINE + " " + TYPE_INTEGER + ", " +
                COLUMN_REPETITION_DAY + " " + TYPE_INTEGER +  ")";

        public static final String CREATE_COMMON_COMMAND = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + CREATE_HEADER_COMMAND;
        public static final String CREATE_VOCABULARY_STUDY_COMMAND = "CREATE TABLE IF NOT EXISTS " + TODAY_TABLE_STUDY_NAME + CREATE_HEADER_COMMAND;
        public static final String CREATE_VOCABULARY_REPEAT_COMMAND = "CREATE TABLE IF NOT EXISTS " + TODAY_REPEAT_TABLE_NAME + CREATE_HEADER_COMMAND;


        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

        public static final String DROP_TODAY_STUDY_TABLE = "DROP TABLE IF EXISTS " + TODAY_TABLE_STUDY_NAME;
        public static final String DROP_TODAY_REPEAT_TABLE = "DROP TABLE IF EXISTS " + TODAY_REPEAT_TABLE_NAME;

        public static final String GET_RANDOM_FROM_STUDY = "SELECT * FROM " + TABLE_NAME + " ORDER BY RANDOM() LIMIT 1";


        public static final String CREATE_PRACTICE_TABLE = "CREATE TABLE IF NOT EXISTS " + PRACTICE_TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN_WORD + " " + TYPE_TEXT + ", " +
                COLUMN_MEANING + " " + TYPE_TEXT + ", " +
                COLUMN_MEANING_NATIVE + " " + TYPE_TEXT + ", " +
                COLUMN_TRANSLATE + " " + TYPE_TEXT + ", " +
                COLUMN_WRITE_SENTENCE + " " + TYPE_TEXT + ", " +
                COLUMN_WRITE_SENTENCE_NATIVE + " " + TYPE_TEXT + ", " +
                COLUMN_REPEAT_LEVEL + " " + TYPE_INTEGER + ", " +
                COLUMN_PRACTICE_LEVEL + " " + TYPE_INTEGER + ")";


        public static final String DROP_PRACTICE_TABLE = "DROP TABLE IF EXISTS " + PRACTICE_TABLE_NAME;

    }


}