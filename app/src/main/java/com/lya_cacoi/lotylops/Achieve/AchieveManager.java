package com.lya_cacoi.lotylops.Achieve;

import android.content.Context;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;

public class AchieveManager {

    public static final int MAX_ACHIEVE = 3;

    public static final int A_COMPLETE_DAY = 1;
    public static final int A_7_DAYS_IN_A_ROW = 2;
    public static final int A_EASY_WON = 3;

    public static ArrayList<Achieve> getAchieves(Context context){
        ArrayList<Achieve> achieves = new ArrayList<>();
        for (int i = 1; i <= MAX_ACHIEVE; i++){
            achieves.add(new Achieve(i, transportPreferences.getAchieve(context, i)));
        }
        return achieves;
    }

    public static void setAchieve(Context context, int id){
        transportPreferences.setAchieve(context, id, true);
    }

    public static String getTitle(int id) {
        switch (id){
            case A_COMPLETE_DAY:
                return "Завершить день";
            case A_7_DAYS_IN_A_ROW:
                return "Заниматься неделю без перерывов";
            case A_EASY_WON:
                return "Выйграть в игре на легком уровне сложности";
        }
        throw new RuntimeException();
    }

    public static int getImage(int id){
        switch (id){
            case AchieveManager.A_COMPLETE_DAY:
                return R.drawable.ic_achieve_start;
            case AchieveManager.A_7_DAYS_IN_A_ROW:
                return R.drawable.ic_achieve_week;
            case AchieveManager.A_EASY_WON:
                return R.drawable.ic_achieve_easy_play;
        }
        throw new RuntimeException();
    }
}
