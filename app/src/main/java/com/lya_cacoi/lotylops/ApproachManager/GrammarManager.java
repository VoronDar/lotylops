package com.lya_cacoi.lotylops.ApproachManager;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.activities.GrammarTheoryActivity;
import com.lya_cacoi.lotylops.activities.testBlock;
import com.lya_cacoi.lotylops.activities.testSelect;
import com.lya_cacoi.lotylops.activities.testSentence;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.GrammarCard;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;

public class GrammarManager implements ApproachManager.Manager {

    private Context context;

    private static int repeatCount;                                                                 // количество тестов для карточки, которые осталось сделать

    GrammarManager(Context context) {
        this.context = context;
    }

    @Override
    public Fragment getTheoryFragment() {
        return new GrammarTheoryActivity();
    }

    @Override
    public Fragment getTrainFragment(int trainLevel, Card card) {
        GrammarCard gCard = (GrammarCard)card;

        Log.i("main", gCard.toString());


        if (card.getPracticeLevel() > gCard.getTestCount()) card.setPracticeLevel(1);
        if (card.getPracticeLevel() < 1) card.setPracticeLevel(1);


        if (gCard.getTestCount() == 0)
            return getTheoryFragment();
        if (gCard.getPracticeLevel() <= gCard.getSelectTrainsId().size())
            return new testSelect();
        if (gCard.getPracticeLevel() <= (gCard.getBlockTrainsId().size() +
                (gCard.getSelectTrainsId() == null ? 0 : gCard.getSelectTrainsId().size())))
            return new testBlock();
        if (gCard.getTrainsId() != null )
            return new testSentence();
        if (transportPreferences.getGrammarWay(context) == ApproachManager.GRAMMAR_WAY_TRANSDUCTION)
            return null;
        if (transportPreferences.getGrammarWay(context) == ApproachManager.GRAMMAR_WAY_INDUCTION
                && card.getRepeatlevel() < 3)
            return null;

        return getTheoryFragment();
    }

    @Override
    public consts.cardState setCardStudyState(Card card) {

        GrammarCard gCard = (GrammarCard) card;

        ///// НАЗНАЧИТЬ КОЛИЧЕСТВО ТЕСТОВ ДЛЯ ПОВТОРЕНИЯ ЗА 1 РАЗ //////////////////////////////////
        if (card.getPracticeLevel() < 1) card.setPracticeLevel(1);
        if (card.getRepeatlevel() < 1)
            repeatCount = 5;
        else if (card.getRepeatlevel() < 2)
            repeatCount = 4;
        else
            repeatCount = 2;

        if (gCard.getTheory() == null)
            repeatCount=6;

        if (repeatCount > gCard.getTestCount())
            repeatCount = gCard.getTestCount();
        ////////////////////////////////////////////////////////////////////////////////////////////


        consts.cardState cardState = null;
        switch (transportPreferences.getGrammarWay(context)){
            case ApproachManager.GRAMMAR_WAY_TRANSDUCTION:
            case ApproachManager.GRAMMAR_WAY_INDUCTION:
                cardState = consts.cardState.stepTrain;
                break;
            case ApproachManager.GRAMMAR_WAY_DECUCTION:

                if (gCard.getTheory() == null)
                    cardState = consts.cardState.stepTrain;
                else if (card.getRepeatlevel() >= consts.START_TEST)
                    cardState = consts.cardState.stepTrain;
                else
                    cardState = consts.cardState.study;
                break;
        }

        Log.i("main", "cardState aaaaaaaaaaa  - " + cardState);

        return cardState;
    }

    @Override
    public void upPractice(Card card) {
        GrammarCard gCard = (GrammarCard) card;
        card.setPracticeLevel(card.getPracticeLevel());
        if (card.getPracticeLevel() > gCard.getTestCount())
            card.setPracticeLevel(1);

    }

    @Override
    public Fragment closeTest(Card card, consts.cardState getState, boolean isRight, SqlMain sqlMain) {
        if (--repeatCount == 0 && (getState == consts.cardState.study || (card.getRepeatlevel() < 3 && transportPreferences.getGrammarWay(context) == ApproachManager.GRAMMAR_WAY_INDUCTION) || transportPreferences.getGrammarWay(context) == ApproachManager.GRAMMAR_WAY_TRANSDUCTION))
            return null;
        else if (repeatCount == 0 && ((GrammarCard)card).getTheory() != null)
            return getTheoryFragment();
        else if (repeatCount == 0 && ((GrammarCard)card).getTheory() == null)
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
        return GRAMMAR_INDEX;
    }

    @Override
    public int getAddableCount() {
        return 2;
    }
}
