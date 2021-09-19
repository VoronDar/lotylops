package com.lya_cacoi.lotylops.ruler;

import android.content.Context;

import com.lya_cacoi.lotylops.ApproachManager.ApproachManager;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.FREQUENCY_LINEAR;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;

public class CardRepeatManage {


    private static int setDay(int day, int level, int index, Context context) {
        if (transportPreferences.getFrequency(context, index) == ApproachManager.FREQUENCY_EXPO)
            return day + getLastDay(level) - getLastDay(level-1) - 1;                          // right memorization bow
        return day + level + 1;                                                                     // fast remembering
    }
    public static int getLastDayOfRepeat(int nextDay, int nowLevel, Context context, int sectionIndex){
        if (transportPreferences.getFrequency(context, sectionIndex) == FREQUENCY_LINEAR)
            return nextDay - nowLevel;
        return nextDay - getLastDay(nowLevel) + getLastDay(nowLevel-1) +1;
    }

    public static void upDay(Card card, Context context, ApproachManager.Manager manager){
        card.setRepetitionDat(setDay(Ruler.getDay(context), card.getRepeatlevel(), manager.getIndex(), context));
        card.setRepeatlevel(card.getRepeatlevel()+1);
        manager.upPractice(card);

    }
    public static void riseFromFall(Card card){                                                     // it's only for extremely repeat mode (day+level+1). We have null level = -1, because if we want
                                                                                                    // to forget the card we should use this to put it in the current day. And in the card view we
                                                                                                    // have to rise it to 0 to put it to tomorrow
        if (card.getRepeatlevel() == consts.NOUN_LEVEL){
            card.setRepeatlevel(consts.NOUN_LEVEL+1);
        }
    }

    static int getLastDay(int level) {                                                                     // for interval
        if (level > -1) {
            return getLastDay(level - 1) * 2 + 1;
        }
        return 0;
    }


    public static int getEndTest(int index){
        switch (index){
            case VOCABULARY_INDEX:
                return consts.END_TEST;
            case PHRASEOLOGY_INDEX:
                return consts.P_END_TEST;
            default:
                return 0;
        }
    }                                                   // get level of the last test of some chapter


}
