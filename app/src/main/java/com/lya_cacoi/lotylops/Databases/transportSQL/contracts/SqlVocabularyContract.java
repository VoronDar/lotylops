package com.lya_cacoi.lotylops.Databases.transportSQL.contracts;

import android.provider.BaseColumns;

import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.PhraseologyContract.PhraseologyEntry.COLUMN_LINKED;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.VocabularyContract.VocabularyEntry.COLUMN_IS_MINE;

public class SqlVocabularyContract {
    public static final class VocabularyEntry extends Entry implements BaseColumns {
        /*

        виды баз данных:

        статическая - туда складывается вся информация о карте, представленная в еденичном экземпляре,
        которая не предполагается к изменению

        прогресс - курс - уровень повторения/практики + день повторения. сюда складываются только активные карты, вброшенные в обучение

        примеры - примеры - их может быть несколько у одного и того же слова

        тренировочные - тренировки написания - их может быть несколько


         */



        public static final String STATIC_TABLE_NAME = "sqlvcbst";
        public static final String PROGRESS_TABLE_NAME = "sqlvcbpr";
        public static final String TRAIN_TABLE_NAME = "sqlvcbtr";
        public static final String EXAMPLES_TABLE_NAME = "sqlvcbex";
        public static final String CHANGES_TABLE_NAME = "sqlvcbch";


        public static final String PHRASEOLOGY_STATIC_TABLE_NAME = "sqlphrst";
        public static final String PHRASEOLOGY_PROGRESS_TABLE_NAME = "sqlphrpr";
        public static final String PHRASEOLOGY_CHANGES_TABLE_NAME = "sqlphrch";

        public static final String COLUMN_COURSE = "course";
        public static final String COLUMN__LINKED_CARD_ID = "lnkid";                                // для примеров и тренировок - linked - это мать
        public static final String COLUMN_WORD = "word";
        public static final String COLUMN_TRANSCRIPTION = "transcription";
        public static final String COLUMN_MEANING = "meaning";
        public static final String COLUMN_MEANING_NATIVE = "meaningnative";
        public static final String COLUMN_TRANSLATE = "translate";
        public static final String COLUMN_SYNONYM = "synonym";
        public static final String COLUMN_ANTONYM = "antonym";
        public static final String COLUMN_MEM = "mem";                        // association
        public static final String COLUMN_HELP = "help";
        public static final String COLUMN_GROUP = "groupword";                  // formal\humorous..
        public static final String COLUMN_PART = "part";                       // part of speech
        public static final String COLUMN_SENTENCE = "example";
        public static final String COLUMN_SENTENCE_TRANSLATE = "exampletranslate";


        // булевые значения в которых схранится то, были ли данные изменены пользователем - мем всегда пользовательский
        // слово изменить нельзя
        // примеров и тренировочных может быть большое количество, каждый из которых имеет свой айдишник помимо привязки к карте
        public static final String COLUMN_TRANSCRIPTION_CHANGE = "transcriptionc";
        public static final String COLUMN_MEANING_CHANGE = "meaningc";
        public static final String COLUMN_MEANING_NATIVE_CHANGE = "meaningnativec";
        public static final String COLUMN_TRANSLATE_CHANGE = "translatec";
        public static final String COLUMN_SYNONYM_CHANGE = "synonymc";
        public static final String COLUMN_ANTONYM_CHANGE = "antonymc";
        public static final String COLUMN_HELP_CHANGE = "helpc";
        public static final String COLUMN_GROUP_CHANGE = "groupwordc";
        public static final String COLUMN_PART_CHANGE = "partc";
        public static final String COLUMN_SENTENCE_CHANGE = "examplec";


        public static final String CREATE_STATIC_COMMAND = "CREATE TABLE IF NOT EXISTS " + STATIC_TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN_COURSE + " " + TYPE_TEXT + ", " +
                COLUMN_WORD + " " + TYPE_TEXT + ", " +
                COLUMN_TRANSCRIPTION + " " + TYPE_TEXT + ", " +
                COLUMN_MEANING + " " + TYPE_TEXT + ", " +
                COLUMN_MEANING_NATIVE + " " + TYPE_TEXT + ", " +
                COLUMN_TRANSLATE + " " + TYPE_TEXT + ", " +
                COLUMN_SYNONYM + " " + TYPE_TEXT + ", " +
                COLUMN_LINKED + " " + TYPE_TEXT + ", " +
                COLUMN_ANTONYM + " " + TYPE_TEXT + ", " +
                COLUMN_MEM + " " + TYPE_TEXT + ", " +
                COLUMN_HELP + " " + TYPE_TEXT + ", " +
                COLUMN_GROUP + " " + TYPE_TEXT + ", " +
                COLUMN_PART + " " + TYPE_TEXT + ")";

        public static final String CREATE_PHRASEOLOGY_STATIC_COMMAND = "CREATE TABLE IF NOT EXISTS " + PHRASEOLOGY_STATIC_TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN_COURSE + " " + TYPE_TEXT + ", " +
                COLUMN_WORD + " " + TYPE_TEXT + ", " +
                COLUMN_TRANSCRIPTION + " " + TYPE_TEXT + ", " +
                COLUMN_MEANING + " " + TYPE_TEXT + ", " +
                COLUMN_MEANING_NATIVE + " " + TYPE_TEXT + ", " +
                COLUMN_TRANSLATE + " " + TYPE_TEXT + ", " +
                COLUMN_SYNONYM + " " + TYPE_TEXT + ", " +
                COLUMN_LINKED + " " + TYPE_TEXT + ", " +
                COLUMN_ANTONYM + " " + TYPE_TEXT + ", " +
                COLUMN_MEM + " " + TYPE_TEXT + ", " +
                COLUMN_HELP + " " + TYPE_TEXT + ", " +
                COLUMN_GROUP + " " + TYPE_TEXT + ", " +
                COLUMN_PART + " " + TYPE_TEXT + ")";

        public static final String CREATE_CHANGES_COMMAND = "CREATE TABLE IF NOT EXISTS " + CHANGES_TABLE_NAME +
                " (" + COLUMN_TRANSCRIPTION_CHANGE + " " + TYPE_BOOL + ", " +
                _ID + " " + TYPE_TEXT + ", " +
                COLUMN_MEANING_CHANGE + " " + TYPE_BOOL + ", " +
                COLUMN_MEANING_NATIVE_CHANGE + " " + TYPE_BOOL + ", " +
                COLUMN_TRANSLATE_CHANGE + " " + TYPE_BOOL + ", " +
                COLUMN_SYNONYM_CHANGE + " " + TYPE_BOOL + ", " +
                COLUMN_ANTONYM_CHANGE + " " + TYPE_BOOL + ", " +
                COLUMN_HELP_CHANGE + " " + TYPE_BOOL + ", " +
                COLUMN_IS_MINE + " " + TYPE_INTEGER + ", " +
                COLUMN_GROUP_CHANGE + " " + TYPE_BOOL + ", " +
                COLUMN_PART_CHANGE + " " + TYPE_BOOL + ")";

        public static final String CREATE_PHRASEOLOGY_CHANGES_COMMAND = "CREATE TABLE IF NOT EXISTS " + PHRASEOLOGY_CHANGES_TABLE_NAME +
                " (" + COLUMN_TRANSCRIPTION_CHANGE + " " + TYPE_BOOL + ", " +
                _ID + " " + TYPE_TEXT + ", " +
                COLUMN_MEANING_CHANGE + " " + TYPE_BOOL + ", " +
                COLUMN_MEANING_NATIVE_CHANGE + " " + TYPE_BOOL + ", " +
                COLUMN_TRANSLATE_CHANGE + " " + TYPE_BOOL + ", " +
                COLUMN_SYNONYM_CHANGE + " " + TYPE_BOOL + ", " +
                COLUMN_ANTONYM_CHANGE + " " + TYPE_BOOL + ", " +
                COLUMN_HELP_CHANGE + " " + TYPE_BOOL + ", " +
                COLUMN_IS_MINE + " " + TYPE_INTEGER + ", " +
                COLUMN_GROUP_CHANGE + " " + TYPE_BOOL + ", " +
                COLUMN_PART_CHANGE + " " + TYPE_BOOL + ")";

        public static final String CREATE_PROGRESS_COMAND = "CREATE TABLE IF NOT EXISTS " + PROGRESS_TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN_COURSE + " " + TYPE_TEXT + ", " +
                COLUMN_REPEAT_LEVEL + " " + TYPE_INTEGER + ", " +
                COLUMN_PRACTICE_LEVEL + " " + TYPE_INTEGER + ", " +
                COLUMN_REPETITION_DAY + " " + TYPE_INTEGER + ")";

        public static final String CREATE_PHRASEOLOGY_PROGRESS_COMAND = "CREATE TABLE IF NOT EXISTS " + PHRASEOLOGY_PROGRESS_TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN_COURSE + " " + TYPE_TEXT + ", " +
                COLUMN_REPEAT_LEVEL + " " + TYPE_INTEGER + ", " +
                COLUMN_PRACTICE_LEVEL + " " + TYPE_INTEGER + ", " +
                COLUMN_REPETITION_DAY + " " + TYPE_INTEGER + ")";

        public static final String CREATE_EXAMPLES_COMAND = "CREATE TABLE IF NOT EXISTS " + EXAMPLES_TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN__LINKED_CARD_ID + " " + TYPE_TEXT + ", " +
                COLUMN_SENTENCE + " " + TYPE_TEXT + ", " +
                COLUMN_SENTENCE_TRANSLATE + " " + TYPE_TEXT + ", " +
                COLUMN_SENTENCE_CHANGE + " " + TYPE_BOOL  + ")";

        public static final String CREATE_TRAIN_COMAND = "CREATE TABLE IF NOT EXISTS " + TRAIN_TABLE_NAME +
                " (" + _ID + " " + TYPE_TEXT + ", " +
                COLUMN__LINKED_CARD_ID + " " + TYPE_TEXT + ", " +
                COLUMN_SENTENCE + " " + TYPE_TEXT + ", " +
                COLUMN_SENTENCE_TRANSLATE + " " + TYPE_TEXT + ", " +
                COLUMN_SENTENCE_CHANGE + " " + TYPE_BOOL  + ")";

        public static final String DROP_STATIC_TABLE = "DROP TABLE IF EXISTS " + STATIC_TABLE_NAME;
        public static final String DROP_CHANGES_TABLE = "DROP TABLE IF EXISTS " + CHANGES_TABLE_NAME;
        public static final String DROP_PROGRESS_TABLE = "DROP TABLE IF EXISTS " + PROGRESS_TABLE_NAME;
        public static final String DROP_EXAMPLES_TABLE = "DROP TABLE IF EXISTS " + EXAMPLES_TABLE_NAME;
        public static final String DROP_TRAIN_TABLE = "DROP TABLE IF EXISTS " + TRAIN_TABLE_NAME;

        public static final String DROP_PHRASEOLOGY_STATIC_TABLE = "DROP TABLE IF EXISTS " + PHRASEOLOGY_STATIC_TABLE_NAME;
        public static final String DROP_PHRASEOLOGY_CHANGES_TABLE = "DROP TABLE IF EXISTS " + PHRASEOLOGY_CHANGES_TABLE_NAME;
        public static final String DROP_PHRASEOLOGY_PROGRESS_TABLE = "DROP TABLE IF EXISTS " + PHRASEOLOGY_PROGRESS_TABLE_NAME;



        public static final String GET_RANDOM_FROM_STATIC = "SELECT * FROM " + STATIC_TABLE_NAME + " ORDER BY RANDOM() LIMIT 1";
        public static final String GET_RANDOM_FROM_STATIC_PHRASEOLOGY = "SELECT * FROM " + PHRASEOLOGY_STATIC_TABLE_NAME + " ORDER BY RANDOM() LIMIT 1";
    }

}