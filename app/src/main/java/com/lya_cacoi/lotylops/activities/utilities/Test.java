package com.lya_cacoi.lotylops.activities.utilities;


import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.PoolFragment;
import com.lya_cacoi.lotylops.activities.PracticeSelectPoolFragment;
import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.activities.utilities.panel.ErrorPanel;
import com.lya_cacoi.lotylops.activities.utilities.panel.InfoHoldable;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.ruler.Ruler;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static android.view.View.GONE;
import static com.lya_cacoi.lotylops.activities.mainPlain.dp;
import static com.lya_cacoi.lotylops.declaration.consts.SELECT_REPEAT_ALL_COURSE;

public class Test extends CommonFragment implements Backable{

    protected Button buttonNext;
    protected Button buttonCheck;
    protected Button buttonSkip;
    protected Ruler ruler;
    protected consts.cardState practiceState;
    protected boolean isRight;
    protected Card card;
    protected int index;
    protected View view;

    protected SoundManager soundManager;
    protected boolean isRealSound;
    public static int multiple;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setSliderSize(int allCards, int leftCards){
        View bar = requireView().findViewById(R.id.progress_bar);

        Log.i("main", "cards, allCards: " + allCards + ", leftCards: " + leftCards);
        if (allCards == 0){
            bar.setVisibility(GONE);
            return;
        }

        int oneItem = (mainPlain.sizeWidth)/allCards;
        ViewGroup.LayoutParams p = bar.getLayoutParams();
        p.width = oneItem*(allCards-leftCards);
        if (p.width == 0)
            bar.setVisibility(GONE);
        else
            bar.setVisibility(View.VISIBLE);
    }



        protected void getInformation(){
        index = Ruler.getSQLIndex();
        ruler = Ruler.getInstance(getContext(), index);
        practiceState = ruler.getCardStudyState();
        card = Ruler.getLastCard();
    }
    protected void setButtonsListeners(){
        this.view.findViewById(R.id.skip).setOnClickListener(this::skipTest);
        if (this.view.findViewById(R.id.next) != null) {
            this.view.findViewById(R.id.next).setOnClickListener(this::goNext);
        }
    }

    public void skipTest(View view){
        TrasportActivities.skipPracticeCard(this, card, index);
    }
    public void goNext(View view) {
        Log.i("main", "go next");
        TrasportActivities.goNextCardActivity(this, isRight, practiceState, card, index);

    }


    @Override
    public void onDestroy() {
        if (soundManager != null)
            soundManager.closeSound();
        super.onDestroy();
    }
    protected void playWord(View view){
        if (isRealSound)
            soundManager.playSound();
        else{
            mainPlain.repeatTTS.speak(((VocabularyCard)card).getWord(), TextToSpeech.QUEUE_FLUSH,
                    null);
        }
    }
    protected void playSound(String sound){
        if (isRealSound)
            soundManager.playSound();
        else{
            mainPlain.repeatTTS.speak(sound, TextToSpeech.QUEUE_FLUSH,
                    null);
        }
    }


    protected void setButtonNextDisabled(){
        buttonNext = this.view.findViewById(R.id.next);
        if (buttonNext != null)
            buttonNext.setEnabled(false);
    }

    protected void setNextButtonAvailable(){
        Log.i("main", "setVisible");
        buttonSkip.setVisibility(View.GONE);
        buttonNext.setVisibility(View.VISIBLE);
        buttonNext.setEnabled(true);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getInformation();

        int sqlIndex = Ruler.getSQLIndex();
        setSliderSize(SqlMain.getInstance(getContext(), sqlIndex).getLearnCardToday(),
                SqlMain.getInstance(getContext(), sqlIndex).getLearnCardLeft());


        this.view = view;
        buttonCheck = view.findViewById(R.id.check);
        setButtonNextDisabled();
        buttonSkip = view.findViewById(R.id.skip);


        if (mainPlain.sizeHeight < 1300)
            multiple = 1;
        else
            multiple = 2;



        TrasportActivities.showSkipButton(getView());
        setButtonsListeners();
    }

    private void setNewButtonSize(Button button){
        if (button != null){
            button.setTextSize((mainPlain.sizeHeight/(35f*multiple)));
            button.setHeight(mainPlain.sizeHeight /8);
        }
    }

    protected void setMeaningAndTranslate(){

        TextView meaning = view.findViewById(R.id.meaning);
        String meaningText = "";
        boolean isMeaning = false;
        if (transportPreferences.getMeaningPriority(getContext()) > consts.PRIORITY_NEVER_TEST){
            if (((VocabularyCard)card).getMeaning() != null &&
                    (transportPreferences.getMeaningLanguage(getContext()) != consts.LANGUAGE_NATIVE)){
                meaningText+=(((VocabularyCard)card).getMeaning()) + "\n";
                if (meaning != null)
                meaning.setVisibility(View.VISIBLE);
                isMeaning = true;
            }
            if (((VocabularyCard)card).getMeaningNative() != null &&
                    (transportPreferences.getMeaningLanguage(getContext()) != consts.LANGUAGE_NEW)){
                meaningText+=((VocabularyCard)card).getMeaningNative();
                if (meaning != null)
                meaning.setVisibility(View.VISIBLE);
                isMeaning = true;
            }
            if (!isMeaning && ((VocabularyCard)card).getMeaningNative() != null){
                meaningText+=((VocabularyCard)card).getMeaningNative();
                if (meaning != null)
                meaning.setVisibility(View.VISIBLE);
                isMeaning = true;
            }
            else if (!isMeaning && ((VocabularyCard)card).getMeaning() != null){
                meaningText+=((VocabularyCard)card).getMeaning();
                if (meaning != null)
                meaning.setVisibility(View.VISIBLE);
                isMeaning = true;
            }
        }
        TextView word = view.findViewById(R.id.word);
        if (transportPreferences.getTranslatePriority(getContext()) > consts.PRIORITY_NEVER_TEST ||
                !isMeaning){
            word.setVisibility(View.VISIBLE);
            if (((VocabularyCard)card).getTranslate() == null && !isMeaning){
                boolean isNoneMeaning = false;
                Log.i("main", "no translate and no precise meaning");
                if (((VocabularyCard)card).getMeaning() != null &&
                        (transportPreferences.getMeaningLanguage(getContext()) != consts.LANGUAGE_NATIVE)){
                    meaningText+=(((VocabularyCard)card).getMeaning()) + "\n";
                    if (meaning != null)
                    meaning.setVisibility(View.VISIBLE);
                    isNoneMeaning = true;
                    Log.i("main", "let's take meaning");
                }
                if (((VocabularyCard)card).getMeaningNative() != null &&
                        (transportPreferences.getMeaningLanguage(getContext()) != consts.LANGUAGE_NEW)){
                    meaningText+=((VocabularyCard)card).getMeaningNative();
                    if (meaning != null)
                    meaning.setVisibility(View.VISIBLE);
                    isNoneMeaning = true;
                    Log.i("main", "let's take meaning-native");
                }
                if (!isNoneMeaning && ((VocabularyCard)card).getMeaningNative() != null){
                    meaningText+=((VocabularyCard)card).getMeaningNative();
                    if (meaning != null)
                    meaning.setVisibility(View.VISIBLE);
                    isNoneMeaning = true;
                    Log.i("main", "let's take meaning-native");
                }
                else if (!isNoneMeaning && ((VocabularyCard)card).getMeaning() != null){
                    meaningText+=((VocabularyCard)card).getMeaning();
                    if (meaning != null)
                    meaning.setVisibility(View.VISIBLE);
                    isNoneMeaning = true;
                    Log.i("main", "let's take meaning");
                }
                if (!isNoneMeaning){
                    Log.i("main", "there is nothing");
                    ActivitiesUtils.createErrorWindow(mainPlain.activity, () -> {
                        isRight = true;
                        TrasportActivities.putWrongcardsBack(mainPlain.activity, true,  practiceState, card, index);
                        goNext(null);
                    }, card.getId(), getResources().getString(R.string.no_translate_meaning));
                    return;
                }
            }
            word.setText(((VocabularyCard)card).getTranslate());
        }

        if (meaning != null)
            meaning.setText(meaningText);

    }

    protected void showAlertError(String rightAnswer, String wrongAnswer){
        ErrorPanel.openPanel((InfoHoldable) requireActivity(), wrongAnswer, rightAnswer,
                null, (ErrorPanel.DoAfter) () -> goNext(null));
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void onPushBackListener() {
        if (Ruler.isIsPracticeNow()){
            if (Ruler.getPractiseChapter() == SELECT_REPEAT_ALL_COURSE)
            ((mainPlain) requireActivity()).slideFragment(new PoolFragment());
            else
                ((mainPlain) requireActivity()).slideFragment(new PracticeSelectPoolFragment());
        }
    }

}
