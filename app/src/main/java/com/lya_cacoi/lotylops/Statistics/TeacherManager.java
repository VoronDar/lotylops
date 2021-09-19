package com.lya_cacoi.lotylops.Statistics;

import android.content.Context;
import android.util.Log;

import com.lya_cacoi.lotylops.Databases.transportSQL.SqlStatistic;
import com.lya_cacoi.lotylops.activities.statistics.command.Command;
import com.lya_cacoi.lotylops.activities.statistics.statistics;
import com.lya_cacoi.lotylops.ruler.Ruler;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.List;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;

public class TeacherManager {

    public static void checkPaceEffectiveness(Context context, final int index){
        // смотрим статы по текущему темпу и предыдущему
        // если раньше ситуация была лучше, чем сейчас, то возвращаем к прошлому темпу
        // смотрим по 10 дней
        // причем высчитывается именно 10 отработанных дней

        if (true) return;

        SqlStatistic sqlStatistic = SqlStatistic.getInstance(context, index);
        sqlStatistic.openDbForWatch();
        final List<OneDayInfo> lastPaceResults = sqlStatistic.getSecondLastSeven();
        final List<OneDayInfo> nowPaceResults = sqlStatistic.getLastSeven();

        Log.i("main", lastPaceResults.toString());
        Log.i("main", nowPaceResults.toString());

        if (lastPaceResults.size() == 0) return;

        /*
        lastPaceResults.add(new OneDayInfo(1, 5, 1, 5, 5));
        lastPaceResults.add(new OneDayInfo(1, 50, 10, 5, 5));
        lastPaceResults.add(new OneDayInfo(1, 50, 10, 5, 5));
        lastPaceResults.add(new OneDayInfo(1, 50, 50, 5, 5));
        lastPaceResults.add(new OneDayInfo(1, 0, 0, 5, 5));

        nowPaceResults.add(new OneDayInfo(1, 4, 1, 5, 20));
        nowPaceResults.add(new OneDayInfo(1, 40, 5, 5, 2));
        nowPaceResults.add(new OneDayInfo(1, 40, 40, 5, 2));
        nowPaceResults.add(new OneDayInfo(1, 40, 5, 5, 2));
        nowPaceResults.add(new OneDayInfo(1, 0, 0, 5, 2));


         */
        float max_ratio = 0.2f;

        float sum_ratio = 0;
        float sum_repeated = 0;
        int fact_days = 0;
        for (OneDayInfo last : lastPaceResults){
            if (last.getRemember_count() > 0){
                fact_days++;
                sum_ratio += (float)last.getForget_count()/(float)last.getRemember_count();
                sum_repeated+=last.getRemember_count();
            }
        }
        final float last_medium_ratio = sum_ratio/(float)fact_days;


        float sum_ratio_now = 0;
        float sum_repeated_now = 0;
        int fact_days_now = 0;
        for (OneDayInfo last : nowPaceResults){
            if (last.getRemember_count() > 0){
                fact_days_now++;
                sum_ratio_now += (float)last.getForget_count()/(float)last.getRemember_count();
                sum_repeated_now+=last.getRemember_count();
            }
        }
        final float now_medium_ratio = sum_ratio_now/(float)fact_days_now;

        //System.out.println("all days - " + lastPaceResults.size() + "\nfact_days - " + fact_days + "\nmedium_ratio - " + sum_ratio/(float)fact_days);

        //System.out.println("NOW\nall days - " + nowPaceResults.size() + "\nfact_days - " + fact_days_now + "\nmedium_ratio - " + now_medium_ratio);


        //max_ratio*=1+((float)nowPaceResults.get(0).getPace()/50f);


        System.out.println("max_ratio - " + max_ratio);
        final int should_pace;
        if (now_medium_ratio >= max_ratio+0.05f && last_medium_ratio < now_medium_ratio)
            should_pace = lastPaceResults.get(0).getPace();
        else if (now_medium_ratio >= max_ratio+0.05f) {
            //should_pace = nowPaceResults.get(0).getPace()*((int)now_medium_ratio-(int)max_ratio+1);
            should_pace = nowPaceResults.get(0).getPace()-1;
        }
        else if (now_medium_ratio <= max_ratio)
            should_pace = nowPaceResults.get(0).getPace()+1;
        else
            should_pace = nowPaceResults.get(0).getPace();

        //System.out.println("shouldPace - " + should_pace);
        //System.out.println((int)(60*(1-((now_medium_ratio-max_ratio)*0.7))));

        Log.i("main", "should_pace " + should_pace);
        transportPreferences.setLastTeacherDay(context, index, Ruler.getDay(context) + SqlStatistic.pickCount);

        if (should_pace != nowPaceResults.get(0).getPace()) {
            statistics stat = statistics.getInstance(context);
            stat.putCommand(new Command() {
                @Override
                public void action(Context context) {
                    if (index == VOCABULARY_INDEX)
                        transportPreferences.setPaceVocabulary(context, should_pace);
                }

                @Override
                public void drop(Context context) {
                    // nothing
                }

                @Override
                public String getName() {
                    if (should_pace > now_medium_ratio)
                        return "Вы делаете большие успехи! Увеличить скорость обучения, чтобы вы смогли учиться быстрее?";
                    else
                        return "Текущая скорость обучения слишком большая. Уменьшить скорость обучения, чтобы вы смогли учиться продуктивнее?";
                }
            });
        }

    }

    public static void checkForCritical(Context context){
        if (transportPreferences.getPaceVocabulary(context) == 0) return;

        if (transportPreferences.getTodayLeftRepeatCardQuantity(context, VOCABULARY_INDEX) >= transportPreferences.getPaceVocabulary(context)*5){
            statistics.getInstance(context).putCommand(new Command() {
                @Override
                public void action(Context context) {
                    transportPreferences.setTodayLeftStudyCardQuantity(context, VOCABULARY_INDEX, 0);
                }

                @Override
                public void drop(Context context) {
                    // nothing
                }

                @Override
                public String getName() {
                    return "Вам предстоит многое повторить за сегодня. Отменить изучение слов сегодня, это позволит вам запомнить слова лучше";
                }
            });
        }
    }

    //public static void main(String []args) {
    //    checkPaceEffectiveness(VOCABULARY_INDEX);
    //}
}
