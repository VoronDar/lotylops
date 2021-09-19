package com.lya_cacoi.lotylops.ApproachManager;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.activities.GrammarTheoryActivity;
import com.lya_cacoi.lotylops.activities.testSelect;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.ruler.CardRepeatManage;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.CULTURE_INDEX;

public class CultureManager implements ApproachManager.Manager {

    private Context context;

    CultureManager(Context context) {
        this.context = context;
    }

    @Override
    public Fragment getTheoryFragment() {
        return new GrammarTheoryActivity();
    }

    @Override
    public Fragment getTrainFragment(int trainLevel, Card card) {
        return new testSelect();
    }

    @Override
    public consts.cardState setCardStudyState(Card card) {
        if (card.getRepetitionDat() == 0)
            return consts.cardState.study;
        else
            return consts.cardState.stepTrain;

    }

    @Override
    public void upPractice(Card card) {
        if ((card.getPracticeLevel() < card.getRepeatlevel()))                                      // if practiceLevel == end test set it is not need to rise it
            card.setPracticeLevel(card.getRepeatlevel());
}

    @Override
    public Fragment closeTest(Card card, consts.cardState getState, boolean isRight, SqlMain sqlMain) {
            if (isRight) {
                return getTheoryFragment();
            } else {
                card.setRepeatlevel(consts.NOUN_LEVEL);
                CardRepeatManage.upDay(card, context, this);
                sqlMain.updateProgress(card);
                return getTrainFragment(card.getPracticeLevel(), card);
            }
    }

    @Override
    public void setErrorProgress(Card card, consts.cardState cardState) {
        if (cardState == consts.cardState.stepTrain) {
            card.setPracticeLevel(card.getRepeatlevel());
        } else
            card.setRepetitionDat(0);
    }

    @Override
    public boolean isTrainAfterStudy(consts.cardState cardStudyState, Card lastCard) {
        return false;
    }

    @Override
    public int getIndex() {
        return CULTURE_INDEX;
    }

    @Override
    public int getAddableCount() {
        return 2;
    }
}
