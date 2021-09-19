package com.lya_cacoi.lotylops.Databases.transportSQL.contracts;

import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.Entry.TYPE_INTEGER;
import static com.lya_cacoi.lotylops.Databases.transportSQL.contracts.Entry.TYPE_TEXT;

public class CourseContract {

    public static final String VOCABULARY_TABLE_NAME = "courses";
    public static final String GRAMMAR_TABLE_NAME = "coursesgr";
    public static final String PHRASEOLOGY_TABLE_NAME = "coursesph";
    public static final String EXCEPTION_TABLE_NAME = "coursesex";
    public static final String PHONETIC_TABLE_NAME = "coursepn";
    public static final String CULTURE_TABLE_NAME = "coursecul";


    public static final String COLUMN_VERSION = "vers";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ENABLED = "enable";
    public static final String COLUMN_DIFFICULTY = "difficulty";
    public static final String COLUMN_ACCESS = "accss";
    public static final String COLUMN_ACCESSIBILITY = "accssib";
    public static final String COLUMN_MISTAKE_ID = "mistakeId";
    public static final String COLUMN_NAME = "name";

    private static final String CREATE_HEADER_COMMAND = " (" + COLUMN_ID + " " + TYPE_TEXT + ", " +
            COLUMN_ACCESS + " " + TYPE_INTEGER + ", " +
            COLUMN_ACCESSIBILITY + " " + TYPE_INTEGER + ", " +
            COLUMN_ENABLED + " " + TYPE_INTEGER + ", " +
            COLUMN_DIFFICULTY + " " + TYPE_INTEGER + ", " +
            COLUMN_NAME + " " + TYPE_INTEGER + ", " +
            COLUMN_VERSION + " " + TYPE_INTEGER +  ")";


    private static final String CREATE_HEADER_COMMAND_FOR_GRAMMAR = " (" + COLUMN_ID + " " + TYPE_TEXT + ", " +
            COLUMN_ACCESS + " " + TYPE_INTEGER + ", " +
            COLUMN_ACCESSIBILITY + " " + TYPE_INTEGER + ", " +
            COLUMN_ENABLED + " " + TYPE_INTEGER + ", " +
            COLUMN_DIFFICULTY + " " + TYPE_INTEGER + ", " +
            COLUMN_MISTAKE_ID + " " + TYPE_TEXT + ", " +
            COLUMN_NAME + " " + TYPE_INTEGER + ", " +
            COLUMN_VERSION + " " + TYPE_INTEGER +  ")";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + VOCABULARY_TABLE_NAME +
            CREATE_HEADER_COMMAND;


    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + VOCABULARY_TABLE_NAME;


    public static final String CREATE_GRAMMAR_TABLE = "CREATE TABLE IF NOT EXISTS " + GRAMMAR_TABLE_NAME +
            CREATE_HEADER_COMMAND_FOR_GRAMMAR;


    public static final String DROP_GRAMMAR_TABLE = "DROP TABLE IF EXISTS " + GRAMMAR_TABLE_NAME;


    public static final String CREATE_PHRASEOLOGY_TABLE = "CREATE TABLE IF NOT EXISTS " + PHRASEOLOGY_TABLE_NAME +
            CREATE_HEADER_COMMAND;


    public static final String DROP_PHRASEOLOGY_TABLE = "DROP TABLE IF EXISTS " + PHRASEOLOGY_TABLE_NAME;


    public static final String CREATE_EXCEPTION_TABLE = "CREATE TABLE IF NOT EXISTS " + EXCEPTION_TABLE_NAME +
            CREATE_HEADER_COMMAND;


    public static final String DROP_EXCEPTION_TABLE = "DROP TABLE IF EXISTS " + EXCEPTION_TABLE_NAME;



    public static final String CREATE_CULTURE_TABLE = "CREATE TABLE IF NOT EXISTS " + CULTURE_TABLE_NAME +
            CREATE_HEADER_COMMAND;


    public static final String DROP_CULTURE_TABLE = "DROP TABLE IF EXISTS " + CULTURE_TABLE_NAME;

    public static final String CREATE_PHONETIC_TABLE = "CREATE TABLE IF NOT EXISTS " + PHONETIC_TABLE_NAME +
            CREATE_HEADER_COMMAND;


    public static final String DROP_PHONETIC_TABLE = "DROP TABLE IF EXISTS " + PHONETIC_TABLE_NAME;

}
