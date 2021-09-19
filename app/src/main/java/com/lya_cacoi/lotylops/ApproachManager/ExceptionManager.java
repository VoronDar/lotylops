package com.lya_cacoi.lotylops.ApproachManager;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.activities.GrammarTheoryActivity;
import com.lya_cacoi.lotylops.activities.testClearWriting;
import com.lya_cacoi.lotylops.activities.testClearWritingSound;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.ExceptionCard;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.ruler.CardRepeatManage;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.EXCEPTION_INDEX;
import static com.lya_cacoi.lotylops.declaration.consts.EXCEPTION_TRIGGER;
import static com.lya_cacoi.lotylops.declaration.consts.NOUN_LEVEL;

public class ExceptionManager implements ApproachManager.Manager {

    private Context context;

    public ExceptionManager(Context context) {
        this.context = context;
    }

    @Override
    public Fragment getTheoryFragment() {
        return new GrammarTheoryActivity();
    }

    @Override
    public Fragment getTrainFragment(int trainLevel, Card card) {
        if (trainLevel < EXCEPTION_TRIGGER)
            return new testClearWriting();
        ((ExceptionCard)card).isLastTest = false;
        return new testClearWritingSound();
    }

    @Override
    public consts.cardState setCardStudyState(Card card) {
        if (card.getRepeatlevel() != card.getPracticeLevel() && card.getPracticeLevel() >= EXCEPTION_TRIGGER) return consts.cardState.problemTrain;
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
            if (((ExceptionCard)card).isLastTest) {
                CardRepeatManage.riseFromFall(card);
                CardRepeatManage.upDay(card, context, this);
                sqlMain.updateProgress(card);
                return null;
            }

            ((ExceptionCard)card).isLastTest = true;
            return new testClearWriting();
        } else{
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
            card.setRepeatlevel(NOUN_LEVEL);
        }
    }

    @Override
    public boolean isTrainAfterStudy(consts.cardState cardStudyState, Card lastCard) {
        return false;
    }

    @Override
    public int getIndex() {
        return EXCEPTION_INDEX;
    }

    @Override
    public int getAddableCount() {
        return 4;
    }
}
