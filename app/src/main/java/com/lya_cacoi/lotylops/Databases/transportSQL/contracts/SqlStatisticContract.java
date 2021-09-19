package com.lya_cacoi.lotylops.Databases.transportSQL.contracts;

import android.provider.BaseColumns;

public class SqlStatisticContract {
    public static final class DatabaseEntry extends Entry implements BaseColumns {
        /*

данные хранятся по дням за последний месяц только они и влияют на *учителя*
понятно, что со временем количество ошибок увеличивается и в этом нет вины пользователя, в общем, нужно подумать, как изменять уч. план

собственно влияение статистики: критические дни (когда повторить дофига)
высчет благоприятного времени суток
изменение темпа обучения в зависимости от количества ошибок на количество изучаемых слов


         */
        public static final String COLUMN_DAY = "day";
        public static final String COLUMN_STUDIED = "studied";
        public static final String COLUMN_REMEMBERED = "remember";
        public static final String COLUMN_FORGOTTEN = "forgotten";
        public static final String COLUMN_TIME = "time";                                            // время суток
        public static final String COLUMN_PACE = "pace";

        public static final String TABLE_NAME = "statistic";


        public static final String CREATE_TABLE_COMMAND = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + COLUMN_DAY + " " + TYPE_INTEGER + ", " +
                COLUMN_TIME + " " + TYPE_INTEGER + ", " +
                COLUMN_STUDIED + " " + TYPE_INTEGER + ", " +
                COLUMN_REMEMBERED + " " + TYPE_INTEGER + ", " +
                COLUMN_FORGOTTEN + " " + TYPE_INTEGER + ", " +
                COLUMN_PACE + " " + TYPE_INTEGER + ")";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

}