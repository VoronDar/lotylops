package com.lya_cacoi.lotylops.Shop;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.activities.DayPlanFragment;
import com.lya_cacoi.lotylops.activities.GamePlayFragment;
import com.lya_cacoi.lotylops.activities.GameStartFragment;
import com.lya_cacoi.lotylops.activities.PoolFragment;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;

public class Shop {

    /*

    за выполнение дневного плана выдается 4 монетки

    за выполнение дневного плана 5 раз подряд выдается дополнительно 10 монеток



     */

    public static final int MONEY_FOR_LOADING_CARD = 8;

    public static final int ADD_EVERY_DAY_MONEY = 4;

    public enum Thing{
        practice{
            @Override
            public Fragment getFragment() {
                return new PoolFragment();
            }

            @Override
            public void apply(Context context) {
                transportPreferences.setPreference(context, getPreference(), 1);
            }

            @Override
            public int getPrice() {
                return 4;
            }

            @Override
            public int getSalesPrice() {
                return 1;
            }

            @Override
            public int getInitialPreference() {
                return 0;
            }

            @Override
            public String getString() {
                return "дополнительное повторение сегодня";
            }
        }, game{
            @Override
            public Fragment getFragment() {
                return new GamePlayFragment();
            }

            @Override
            public void apply(Context context) {
                transportPreferences.setPreference(context, getPreference(), 1);
            }

            @Override
            public int getPrice() {
                return GameStartFragment.selectedId+1;
            }

            @Override
            public int getSalesPrice() {
                return GameStartFragment.selectedId+1;
            }

            @Override
            public int getInitialPreference() {
                return 0;
            }
            @Override
            public String getString() {
                return "сыграть в игру 1 раз";
            }

        }, addPlan{
            @Override
            public Fragment getFragment() {
                return new DayPlanFragment();
            }

            @Override
            public String getString() {
                return "дополнительное обучение сегодня";
            }
            @Override
            public void apply(Context context) {
                transportPreferences.setPreference(context, getPreference(), 1);
                for (int index = 0; index < PHONETIC_INDEX; index++) {
                    if (index == VOCABULARY_INDEX)
                        Log.i("main", "voc");
                    SqlMain sql = SqlMain.getInstance(context, index);
                    sql.openDbForLearn();
                    sql.getTodayAddableCardsCount();
                    sql.closeDatabases();
                }
            }

            @Override
            public int getPrice() {
                return 8;
            }

            @Override
            public int getSalesPrice() {
                return 2;
            }

            @Override
            public int getInitialPreference() {
                return 0;
            }}
                      ;

        public abstract void apply(Context context);

        // обычная цена
        public abstract int getPrice();
        // цена в период скидок
        public abstract int getSalesPrice();
        // значение по умолчанию (когда еще не уплочено)
        public abstract int getInitialPreference();
        // надпись покупки
        public abstract String getString();

        public String getPreference(){
            return "shop_" + this.name();
        }
        public boolean isPaid(Context context){
            return transportPreferences.getPreference(context, "shop_" + this.name()) == 1;
        }

        abstract public Fragment getFragment();

        public void reload(Context context){
            transportPreferences.setPreference(context, "shop_" + this.name(), 0);
        }
    }


}
