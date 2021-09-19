package com.lya_cacoi.lotylops.ApproachManager;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.activities.MainActivity;
import com.lya_cacoi.lotylops.activities.testGuess;
import com.lya_cacoi.lotylops.activities.testSentence;
import com.lya_cacoi.lotylops.activities.testSound;
import com.lya_cacoi.lotylops.activities.testSoundClear;
import com.lya_cacoi.lotylops.activities.testTranslate;
import com.lya_cacoi.lotylops.activities.testTranslateNative;
import com.lya_cacoi.lotylops.activities.testWriting;
import com.lya_cacoi.lotylops.activities.testWritingClear;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.ruler.CardRepeatManage;
import com.lya_cacoi.lotylops.ruler.Ruler;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.declaration.consts.SENTENVE;
import static com.lya_cacoi.lotylops.declaration.consts.SOUND;
import static com.lya_cacoi.lotylops.declaration.consts.SOUND_CLEAR;
import static com.lya_cacoi.lotylops.declaration.consts.TRANSLATE_LEARN;
import static com.lya_cacoi.lotylops.declaration.consts.TRANSLATE_NATIVE;
import static com.lya_cacoi.lotylops.declaration.consts.WRITING;
import static com.lya_cacoi.lotylops.declaration.consts.WRITING_CLEAR;

public class VocabularyManager implements ApproachManager.Manager {

    private final Context context;

    VocabularyManager(Context context) {
        this.context = context;
    }

    @Override
    public Fragment getTheoryFragment() {
        return new MainActivity();
    }

    @Override
    public Fragment getTrainFragment(int trainLevel, Card card) {

        if (Ruler.isIsPracticeNow()) {
            switch (trainLevel) {
                case TRANSLATE_LEARN:
                    return new testTranslate();
                case TRANSLATE_NATIVE:
                    return new testTranslateNative();
                case SOUND:
                    return new testSound();
                case SOUND_CLEAR:
                    return new testSoundClear();
                case WRITING:
                    return new testWriting();
                case WRITING_CLEAR:
                    return new testWritingClear();
                case SENTENVE:
                    return new testSentence();
            }
            return null;
        }
        switch (trainLevel) {
            case 1:
                return new testTranslate();
            case 2:
                return new testTranslateNative();
            case 3:
                return new testSound();
            default:
            case 4:
                return new testWriting();
            case 5:
                return new testSentence();
            case consts.NOUN_LEVEL:
            case 0:
                if (((VocabularyCard) card).getExamples() != null && ((VocabularyCard) card).getExamples().size() > 0)
                    return new testGuess();
                return new MainActivity();
        }
/*
        int vocApproach = transportPreferences.getVocabularyApproach(context);
        final int read = ((vocApproach == VOC_ONLY_READ_APPROACH)?1:0);

        switch (transportPreferences.getVocabularyTrainApproach(context)) {
            case VOC_EVERYTHING_APPROACH:
                switch (trainLevel) {
                    case 1:
                        return new testTranslate();
                    case 2:
                        return new testTranslateNative();
                    case 3:
                        return new testSound();
                    case 4:
                        return new testWriting();
                    case 5:
                        return new testSelect();
                    default:
                    case 6:
                        return new testSentence();
                    case 7:
                        return new testSynonyms();
                    case 8:
                        return new testContext();
                    case consts.NOUN_LEVEL:
                    case 0:
                        if (((VocabularyCard) card).getExamples() != null && ((VocabularyCard) card).getExamples().size() > 0)
                            return new testGuess();
                        return new MainActivity();
                }
            case VOC_OPTIMAL_APPROACH:
                switch (trainLevel) {
                    case 1:
                        if (vocApproach != VOC_ONLY_READ_APPROACH)
                            return new testSound();
                    case (2):
                        return new testWriting();
                    case 3:
                        return new testSelect();
                    default:
                    case 4:
                        return new testSentence();
                    case 5:
                        return new testSynonyms();
                    case 6:
                        return new testContext();
                    case consts.NOUN_LEVEL:
                    case 0:
                        if (((VocabularyCard) card).getExamples() != null && ((VocabularyCard) card).getExamples().size() > 0)
                            return new testGuess();
                        return new MainActivity();
                }
        }
        return null;

 */
    }

    @Override
    public consts.cardState setCardStudyState(Card card) {
        consts.cardState cardStudyState;

        if (card.getRepetitionDat() == 0)
            return consts.cardState.stepTrain;
        else if (card.getRepeatlevel() >= consts.START_TEST &&
                card.getPracticeLevel() > (card.getRepeatlevel()) &&
                (card.getRepeatlevel() <= CardRepeatManage.getEndTest(VOCABULARY_INDEX))) {
            cardStudyState = consts.cardState.stepTrain;

        } else if (card.getRepeatlevel() < card.getPracticeLevel() &&
                card.getRepeatlevel() >= consts.START_TEST) {
            cardStudyState = consts.cardState.problemTrain;
        }
        else if (card.getRepeatlevel() <= 0 && card.getPracticeLevel() >= consts.START_TEST) {
            cardStudyState = consts.cardState.problemTrain;
        } else if (card.getRepeatlevel() == card.getPracticeLevel() &&
                card.getRepeatlevel() >= consts.START_TEST) {
            cardStudyState = consts.cardState.problemTrain;
        } else if (card.getRepeatlevel() < card.getPracticeLevel() &&
                card.getRepeatlevel() < consts.START_TEST) {
            cardStudyState = consts.cardState.problemTrain;
        } else if (card.getRepeatlevel() > CardRepeatManage.getEndTest(VOCABULARY_INDEX)) {         // all trains was passed
            cardStudyState = consts.cardState.problemTrain;
        } else {
            cardStudyState = consts.cardState.study;
        }


        if (cardStudyState != consts.cardState.study) {
            if (card.getPracticeLevel() == SENTENVE && card.getTrains() == null) {
                cardStudyState = consts.cardState.study;
            }
        }
        return cardStudyState;
    }

    @Override
    public void upPractice(Card card) {
        if ((card.getPracticeLevel() < card.getRepeatlevel()))                                      // if practiceLevel == end test set it is not need to rise it
            card.setPracticeLevel(card.getRepeatlevel());
        if (card.getRepeatlevel() > CardRepeatManage.getEndTest(VOCABULARY_INDEX))
            card.setPracticeLevel(CardRepeatManage.getEndTest(VOCABULARY_INDEX));
    }

    @Override
    public Fragment closeTest(Card card, consts.cardState getState, boolean isRight, SqlMain sqlMain) {
        if (getState == consts.cardState.stepTrain) {
            if (card.getPracticeLevel() != card.getRepeatlevel()) {
                Ruler.setProblemTrain(true);
                return getTrainFragment(card.getPracticeLevel(), card);
            } else {
                Ruler.setProblemTrain(false);
                return getTheoryFragment();
            }
        } else {
            if (isRight) {
                return getTheoryFragment();
            } else {
                card.setRepeatlevel(consts.NOUN_LEVEL);
                CardRepeatManage.upDay(card, context, this);
                sqlMain.updateProgress(card);
                return getTrainFragment(card.getPracticeLevel(), card);
            }
        }
        /*
        if (!isRight){
            card.setRepeatlevel(consts.NOUN_LEVEL);
            CardRepeatManage.upDay(card, context, this);
            sqlMain.updateProgress(card);
            //return null;
        }
        if (getState == consts.cardState.stepTrain) {
            if (card.getPracticeLevel() != card.getRepeatlevel()) {
                Ruler.setProblemTrain(true);
                return getTrainFragment(card.getPracticeLevel(), card);
            } else {
                Ruler.setProblemTrain(false);
                return getTheoryFragment();
            }
        } else
            return getTheoryFragment();

         */
    }

    @Override
    public void setErrorProgress(Card card, consts.cardState cardState) {
        if (cardState == consts.cardState.stepTrain) {
            card.setPracticeLevel(card.getRepeatlevel());
        }
    }

    @Override
    public boolean isTrainAfterStudy(consts.cardState cardStudyState, Card lastCard) {
        return false;
    }

    @Override
    public int getIndex() {
        return VOCABULARY_INDEX;
    }

    @Override
    public int getAddableCount() {
        return 5;
    }
}
