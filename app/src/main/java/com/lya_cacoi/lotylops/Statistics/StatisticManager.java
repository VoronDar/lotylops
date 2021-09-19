package com.lya_cacoi.lotylops.Statistics;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.lya_cacoi.lotylops.Achieve.AchieveManager;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlStatistic;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;

public class StatisticManager {

    /*
    статистика работает таким образом, что данные закидываются прямо перед началом следующего дня

    необходимые данные -
     */

    private static final int NOW_WEEK = 1;
    private static final int LAST_WEEK = 0;

    /**
     * Обозначает, что сегодняшний день закончен
     * */
    public static void endDay(Context context){
        int nowDays = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-2;
        if (nowDays == -1) nowDays = 6;
        transportPreferences.setWorkedByDay(context, nowDays, true);
        transportPreferences.setDaysWorkedCount(context, transportPreferences.getDaysWorkedCount(context)+1);


        AchieveManager.setAchieve(context, AchieveManager.A_COMPLETE_DAY);
        for (int i = 0; i < 7; i++){
            if (!transportPreferences.getWorkedByDay(context, i)) return;
        }
        AchieveManager.setAchieve(context, AchieveManager.A_7_DAYS_IN_A_ROW);

    }

    public static void uppRepeated(Context context, int index){
        transportPreferences.setRememberedCount(context, index, transportPreferences.getRememberedCount(context, index)+1);
        transportPreferences.setWeekRepeated(context, NOW_WEEK, transportPreferences.getWeekRepeated(context, NOW_WEEK)+1);

    }
    public static void uppStudied(Context context, int index){
        transportPreferences.setStudiedCount(context, index, transportPreferences.getRememberedCount(context, index)+1);
        transportPreferences.setWeekStudied(context, NOW_WEEK, transportPreferences.getWeekRepeated(context, NOW_WEEK)+1);
    }
    public static void uppMistakes(Context context, int index){
        transportPreferences.setForgettedCount(context, index, transportPreferences.getForgettedCount(context, index)+1);
        transportPreferences.setWeekMistakes(context, NOW_WEEK, transportPreferences.getWeekMistakes(context, NOW_WEEK)+1);
    }

    public static void addNew(Context context){
        //Random random = new Random(47);
        for (int i = VOCABULARY_INDEX; i <= VOCABULARY_INDEX; i++){
            SqlStatistic sqlStatistic = SqlStatistic.getInstance(context, i);
            sqlStatistic.openDbForUpdate();
            sqlStatistic.addNew(new OneDayInfo(transportPreferences.getLastDayComming(context), transportPreferences.getRememberedCount(context, i), transportPreferences.getForgettedCount(context,i), transportPreferences.getStudiedCount(context, i), transportPreferences.getPaceVocabulary(context), 3));
            sqlStatistic.closeDatabases();

            transportPreferences.setRememberedCount(context, i, 0);
            transportPreferences.setStudiedCount(context, i, 0);
            transportPreferences.setForgettedCount(context, i, 0);
        }

    }

    public static List<OneDayInfo> getStats(Context context, int index){
        SqlStatistic sqlStatistic = SqlStatistic.getInstance(context, index);
        sqlStatistic.openDbForWatch();
        List<OneDayInfo> oneDayInfos = sqlStatistic.getLastSeven();
        sqlStatistic.closeDatabases();
        Collections.reverse(oneDayInfos);
        return oneDayInfos;
    }


    /** обновляем недельные показатели */
    public static void setThisWeekInfo(Context context){
        if (transportPreferences.getWeek(context, NOW_WEEK) < getNowWeek()){
            // прошла неделя - переносим данные из этой недели в прошлую
            transportPreferences.setWeekMistakes(context, LAST_WEEK, transportPreferences.getWeekMistakes(context, NOW_WEEK));
            transportPreferences.setWeekRepeated(context, LAST_WEEK, transportPreferences.getWeekRepeated(context, NOW_WEEK));
            transportPreferences.setWeekStudied(context, LAST_WEEK, transportPreferences.getWeekStudied(context, NOW_WEEK));
            // чистим данные сегодняшней недели
            for (int i = 0; i < 7; i++){
                transportPreferences.setWorkedByDay(context, i, false);
            }
            transportPreferences.setWeekMistakes(context, NOW_WEEK, 0);
            transportPreferences.setWeekRepeated(context, NOW_WEEK, 0);
            transportPreferences.setWeekStudied(context, NOW_WEEK, 0);
            transportPreferences.setWeek(context, NOW_WEEK, getNowWeek());
        } else{

        }
    }

    /** возвращает, насколько изменились недельные показатели
    /* возаращает ArrayList в первом значении -1, 0, 1 (ухудшилось, изменилось, осталось). Во втором насколько в процентах
        2 - если параметры были нулевые
     **/
    public static ArrayList<Integer> getDifferenceByWeek(Context context, StatType type){
        int now;
        int last;
        switch (type){
            case studied:
                now = transportPreferences.getWeekStudied(context, NOW_WEEK);
                last = transportPreferences.getWeekStudied(context, LAST_WEEK);
                break;
            case repeated:
                now = transportPreferences.getWeekRepeated(context, NOW_WEEK);
                last = transportPreferences.getWeekRepeated(context, LAST_WEEK);
                break;
            case mistakes:
                now = transportPreferences.getWeekMistakes(context, NOW_WEEK);
                last = transportPreferences.getWeekMistakes(context, LAST_WEEK);
                break;
            default:
                throw new RuntimeException();
        }
        int nowDays = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-2;
        if (nowDays == -1) nowDays = 6;
        nowDays++;

        int oldStateForDay = last/7;
        int nowStateForDay = now/nowDays;

        ArrayList<Integer> result = new ArrayList<>();

        if (oldStateForDay == 0){
            if (nowStateForDay == 0)
                result.add(0);
            else
                result.add(2);
            return result;
        }

        if (oldStateForDay*0.85 > nowStateForDay){
            result.add(-1);
            result.add(nowStateForDay/(oldStateForDay/100));
        } else if(oldStateForDay*1.15 < nowStateForDay){
            result.add(1);
            result.add((nowStateForDay-oldStateForDay)/(oldStateForDay/100));
        } else{
            result.add(0);
            result.add(0);
        }
        return result;

    }

    private static int getNowWeek(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR)*100 + calendar.get(Calendar.WEEK_OF_YEAR);
    }


    public enum StatType{
        studied,
        repeated,
        mistakes;
    }

    /** включает индикатор, если в этот день занимались
     * @param day - день недели. Указывать с 0 (понедельник)*/
    public static void setDayWorkedView(Context context, int day, View view){
        boolean isWorked = transportPreferences.getWorkedByDay(context, day);
        if (isWorked)
            view.setBackground(context.getResources().getDrawable(R.drawable.do_indicator));
        else
            view.setBackground(context.getResources().getDrawable(R.drawable.dont_indicator));
    }

}
