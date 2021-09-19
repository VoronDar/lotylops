package com.lya_cacoi.lotylops.Helper;

import android.content.Context;

import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.activities.utilities.panel.SimpleInfoPannel;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

public class Helper {
    public static final int SHOW_LIBRARY = 0;
    public static final int SHOW_LIBRARY_AFTER = 2;
    public static final int SHOW_DAY_PLAN = 1;
    public static final int SHOW_POOL = 3;
    public static final int SHOW_SETTING = 4;
    public static final int SHOW_PLAY = 5;
    public static final int SHOW_LOAD = 6;




    // предполагаемый по тесту уровень языка (-1 означает "не определен")
    public static final String LEVEL_PREFERENCE = "level";

    public static void addHelp(Context context){
        transportPreferences.setHelpStudy(context,
                transportPreferences.getHelpStudy(context) +1);

    }


    public static void showHelp(Context context, Help help){
        if (!transportPreferences.getHelp(context, help.getId())) {
            SimpleInfoPannel.openPanel(mainPlain.activity, help.getTitle(), help.getDescription(), -1);
            transportPreferences.setHelp(context, help.getId(), true);
        }
    }

    public static void rebootAll(Context context){
        for (int i = 0; i <=3; i++){
            transportPreferences.setHelp(context, i ,false);
        }
    }


    public enum Help{
        start{
            @Override
            String getTitle() {
                return "Обучение";
            }

            @Override
            String getDescription() {
                return "Здесь отображается информация, которую вам следует изучить или повторить. Для лучших результатов нужно заходить каждый день, не делая перерывов.";
            }

            @Override
            int getId() {
                return 0;
            }
        },
        gameStart{
            @Override
            String getTitle() {
                return "Игра";
            }

            @Override
            String getDescription() {
                return "Вы можете посоревноваться в знании языка. Выберите соперника и начинайте игру!";
            }

            @Override
            int getId() {
                return 1;
            }
        },
        gamePlay{
            @Override
            String getTitle() {
                return "Игра";
            }

            @Override
            String getDescription() {
                return "Чтобы победить нужно понизить жизни противника до нуля или выйграть ход 5 раз подряд. Выберите задание, которое будете выполнять. Чем больше номер, тем сложнее и тем больше награда.";
            }

            @Override
            int getId() {
                return 2;
            }
        },
        library{
            @Override
            String getTitle() {
                return "Курсы";
            }

            @Override
            String getDescription() {
                return "Здесь вы можете выбрать интересующие материалы.";
            }

            @Override
            int getId() {
                return 3;
            }
        };

        abstract String getTitle();
        abstract String getDescription();
        abstract int getId();
    }

}
