package com.lya_cacoi.lotylops.ApproachManager;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.activities.PhoneticsTheoryActivity;
import com.lya_cacoi.lotylops.activities.testSelect;
import com.lya_cacoi.lotylops.activities.testSpeak;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.PhoneticsCard;
import com.lya_cacoi.lotylops.declaration.consts;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;

public class PhoneticsManager implements ApproachManager.Manager {

    private Context context;

    private static int repeatCount;                                                                 // количество тестов для карточки, которые осталось сделать

    PhoneticsManager(Context context) {
        this.context = context;
    }

    @Override
    public Fragment getTheoryFragment() {
        return new PhoneticsTheoryActivity();
    }

    @Override
    public Fragment getTrainFragment(int trainLevel, Card card) {
        PhoneticsCard gCard = (PhoneticsCard) card;

        if (card.getPracticeLevel() > gCard.getTestCount()) card.setPracticeLevel(1);
        if (card.getPracticeLevel() < 1) card.setPracticeLevel(1);

        Log.i("main", (gCard.getSelectTrainsId() == null) + "");

        if (gCard.getTestCount() == 0)
            return getTheoryFragment();
        if (gCard.getSelectTrainsId() != null &&  gCard.getSelectTrainsId().size() > 0)
            return new testSelect();
        return new testSpeak();
    }

    @Override
    public consts.cardState setCardStudyState(Card card) {

        PhoneticsCard gCard = (PhoneticsCard) card;

        ///// НАЗНАЧИТЬ КОЛИЧЕСТВО ТЕСТОВ ДЛЯ ПОВТОРЕНИЯ ЗА 1 РАЗ //////////////////////////////////
        if (card.getPracticeLevel() < 1) card.setPracticeLevel(1);
        if (card.getRepeatlevel() < 1)
            repeatCount = 3;
        else if (card.getRepeatlevel() < 2)
            repeatCount = 2;
        else
            repeatCount = 1;

        if (gCard.getTheory() == null)
            repeatCount=4;

        if (repeatCount > gCard.getTestCount())
            repeatCount = gCard.getTestCount();
        ////////////////////////////////////////////////////////////////////////////////////////////

        consts.cardState cardState;
                if (gCard.getTheory() == null)
                    cardState = consts.cardState.stepTrain;
                else if (card.getRepeatlevel() >= consts.START_TEST)
                    cardState = consts.cardState.stepTrain;
                else
                    cardState = consts.cardState.study;
        return cardState;
    }

    @Override
    public void upPractice(Card card) {
        PhoneticsCard gCard = (PhoneticsCard) card;
        card.setPracticeLevel(card.getPracticeLevel());
        if (card.getPracticeLevel() > gCard.getTestCount())
            card.setPracticeLevel(1);

    }

    @Override
    public Fragment closeTest(Card card, consts.cardState getState, boolean isRight, SqlMain sqlMain) {


        /*

        ДОДЕЛАЙ БТТТТАААТАТАТАТАТТАТААТАЬА
         */



        if (--repeatCount == 0 && (getState == consts.cardState.study))
            return null;
        else if (repeatCount == 0 && ((PhoneticsCard)card).getTheory() != null)
            return getTheoryFragment();
        else if (repeatCount == 0 && ((PhoneticsCard)card).getTheory() == null)
            return null;
        else {
            card.setPracticeLevel(card.getPracticeLevel()+1);
            return getTrainFragment(0, card);
        }
    }

    @Override
    public void setErrorProgress(Card card, consts.cardState cardState) {
        card.setPracticeLevel(card.getPracticeLevel()-1);
        repeatCount++;
    }

    @Override
    public boolean isTrainAfterStudy(consts.cardState cardStudyState, Card lastCard) {
            return (cardStudyState == consts.cardState.study && repeatCount > 0);
    }

    @Override
    public int getIndex() {
        return PHONETIC_INDEX;
    }

    @Override
    public int getAddableCount() {
        return 2;
    }


    public static boolean isSelectSound(String answer){
        if (answer == null || answer.length() == 0) return true; // = пропуск
        return (answer.charAt(0) == '#');
    }

}
