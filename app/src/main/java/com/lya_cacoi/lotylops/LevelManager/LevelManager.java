package com.lya_cacoi.lotylops.LevelManager;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static android.view.View.GONE;
import static com.lya_cacoi.lotylops.activities.mainPlain.dp;

public class LevelManager {

    public static void show(Context context, View slider){
        int oneItem = (mainPlain.sizeWidth - dp(60))/getMaxExp(context);
        ViewGroup.LayoutParams p = slider.getLayoutParams();
        p.width = oneItem*(transportPreferences.getExp(context));
        if (p.width == 0)
            slider.setVisibility(GONE);
        else
            slider.setVisibility(View.VISIBLE);
    }


    public static int getLevel(Context context){
        return transportPreferences.getLevel(context);
    }
    public static int getMaxExp(Context context){
        int level = getLevel(context);
        return (int)getExp(level, 50);
    }

    public static void upLevel(Context context){
        boolean isCheckUp = false;
        if (transportPreferences.getExp(context) > getMaxExp(context)) {
            transportPreferences.setExp(context, transportPreferences.getExp(context) - getMaxExp(context));
            isCheckUp = true;
        }
        else
            transportPreferences.setExp(context,0);
        transportPreferences.setLevel(context, transportPreferences.getLevel(context)+1);
        if (isCheckUp)
            checkUp(context);
    }

    /** проверка, поднялся ли уровень */
    public static void checkUp(Context context){
        if (transportPreferences.getExp(context) >= getMaxExp(context))
            upLevel(context);
    }

    private static float getExp(int level, int exp){
        if (level == 1)
            return exp;
        return getExp(level-1, exp)*1.25f;
    }

    public static void addExp(Context context, Action action){
        Log.i("exp", "addExp: "+ action.getExp(context));
        transportPreferences.setExp(context, transportPreferences.getExp(context) + action.getExp(context));
        checkUp(context);
    }

    public enum Action{
        lookItem{
            @Override
            int getExp(Context context) {
                return 5;
            }
        },
        repeatItem {
            @Override
            int getExp(Context context) {
                return 10;
            }
        },
        lookRule{
            @Override
            int getExp(Context context) {
                return 6;
            }
        },
        repeatRule{
            @Override
            int getExp(Context context) {
                return 12;
            }
        },
        practice{
            @Override
            int getExp(Context context) {
                return 4;
            }
        },
        practiceWrong{
            @Override
            int getExp(Context context) {
                return 1;
            }
        },
        gameEasyWin{
            @Override
            int getExp(Context context) {
                if (getLevel(context) < 10)
                    return (int)(getMaxExp(context)*0.3f);
                else
                    return (int)(getMaxExp(context)*0.1f);
            }
        },
        gameMediumWin{
            @Override
            int getExp(Context context) {
                if (getLevel(context) < 10)
                    return (int)(getMaxExp(context)*0.5f);
                else
                    return (int)(getMaxExp(context)*0.2f);
            }
        },
        gameHardWin{
            @Override
            int getExp(Context context) {
                if (getLevel(context) < 10)
                    return (int)(getMaxExp(context)*0.9f);
                else
                    return (int)(getMaxExp(context)*0.3f);
            }
        },
        gameLose{
            @Override
            int getExp(Context context) {
                if (getLevel(context) < 10)
                    return (int)(getMaxExp(context)*0.1f);
                else
                    return (int)(getMaxExp(context)*0.05f);
            }
        },
        acceptCard{
            @Override
            int getExp(Context context) {
                if (getLevel(context) < 10)
                    return (int)(getMaxExp(context));
                else
                    return (int)(getMaxExp(context)*0.3f);
            }
        };
        abstract int getExp(Context context);
    }
}
