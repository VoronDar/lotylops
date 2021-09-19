package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.ApproachManager.ApproachManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.SoundManager;
import com.lya_cacoi.lotylops.activities.utilities.Test;
import com.lya_cacoi.lotylops.activities.utilities.TrasportActivities;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.activities.utilities.TrainButtonsChanger.select;
import static com.lya_cacoi.lotylops.activities.utilities.TrainButtonsChanger.setRight;

public class testTranslateNative extends Test {
    private int approach;
    private View firstAnswer;
    private View secondAnswer;
    private View thirdAnswer;
    private View fourthAnswer;
    private ArrayList<String> answer;
    private ArrayList<View> answerButtons;
    private int answerSelectId          = -1;
    private int rightId                 = -1;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getInformation();
        approach = transportPreferences.getVocabularyApproach(getContext());
            if (approach == ApproachManager.VOC_AUDIO_APPROACH){
                return inflater.inflate(R.layout.activity_train_translate_sound, container, false);
            }
            return inflater.inflate(R.layout.activity_train_translate, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (approach == ApproachManager.VOC_AUDIO_APPROACH) {
            answerButtons = new ArrayList<>(4);
            answerButtons.add((view.findViewById(R.id.sound1)));
            answerButtons.add((view.findViewById(R.id.sound2)));
            answerButtons.add((view.findViewById(R.id.sound3)));
            answerButtons.add((view.findViewById(R.id.sound4)));
            buttonNext.setEnabled(true);
        }

        setMeaningAndTranslate();

        answer = new ArrayList<>(4);

        if (approach == ApproachManager.VOC_AUDIO_APPROACH && index == VOCABULARY_INDEX){
            answer.add(card.getId());
            for (int i = 0; i < 3; i++) {
                String translate;
                do {
                    translate =ruler.getRandomCard().getId();
                    //translate = ruler.getDatabase().getRandomCard().getId();
                }
                while (answer.contains((translate)));
                answer.add(translate);
            }
            Collections.shuffle(answer);

            soundManager = new SoundManager(mainPlain.activity);
            isRealSound = soundManager.loadSeveralSound(answer);
            if (!isRealSound)
                answer.clear();

            Collections.shuffle(answer);
            for (int i = 0; i < answer.size(); i++){
                Log.i("main", "a- get " + answer.get(i));
                if (answer.get(i).equals(card.getId())){
                    rightId = i;
                    break;
                }
            }
        }
        if (approach != ApproachManager.VOC_AUDIO_APPROACH || !isRealSound) {
                answer.add(((VocabularyCard) card).getWord());
            int failedCount = 0;
                for (int i = 0; i < 3; i++) {
                    String translate;
                    do {
                        //translate = ((VocabularyCard) ruler.getDatabase().getRandomCard()).getWord();

                        translate = ((VocabularyCard)ruler.getRandomCard()).getWord();
                    }
                    while (answer.contains((translate)) && ++failedCount < 10);

                    if (failedCount>= 10){
                        Toast.makeText(getContext(), "недостаточно слов, чтобы запустить тест", Toast.LENGTH_LONG).show();
                        isRight = true;
                        TrasportActivities.putWrongcardsBack(mainPlain.activity, true,  practiceState, card, index);
                        goNext(view);
                        return;
                    }
                    answer.add(translate);
                }
            String right = answer.get(0);
            Collections.shuffle(answer);
            for (int i = 0; i < answer.size(); i++){
                if (answer.get(i).equals(right)){
                    rightId = i;
                    break;
            }
            }
        }
        Log.i("main", "rightId - " + rightId);
        //ruler.closeDatabase();


        if (approach == ApproachManager.VOC_AUDIO_APPROACH){
            view.findViewById(R.id.check).setOnClickListener(this::answerClick);

            for (View n: answerButtons)
                n.setOnClickListener(this::selectSound);
        }
        else {
            firstAnswer = view.findViewById(R.id.firstAnswer);
            ((Button)firstAnswer).setText(answer.get(0));
            firstAnswer.setOnClickListener(this::answerClick);
            secondAnswer = view.findViewById(R.id.secondAnswer);
            ((Button)secondAnswer).setText(answer.get(1));
            secondAnswer.setOnClickListener(this::answerClick);
            thirdAnswer = view.findViewById(R.id.thirdAnswer);
            ((Button)thirdAnswer).setText(answer.get(2));
            thirdAnswer.setOnClickListener(this::answerClick);
            fourthAnswer = view.findViewById(R.id.fourthAnswer);
            ((Button)fourthAnswer).setText(answer.get(3));
            fourthAnswer.setOnClickListener(this::answerClick);

            /*
            if (mainPlain.sizeRatio < mainPlain.triggerRatio){
                firstAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(13*multiple));
                secondAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(13*multiple));
                thirdAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(13*multiple));
                fourthAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(13*multiple));
                ((ImageView)view.findViewById(R.id.container)).setMaxHeight((int)(mainPlain.sizeHeight/2.75f));
            }
             */
        }


            /*
        if (mainPlain.sizeRatio < mainPlain.triggerRatio){
            ((ImageView)view.findViewById(R.id.container)).setMaxWidth((int)(mainPlain.sizeWidth*0.85f));
            view.findViewById(R.id.meaning).setPadding(mainPlain.sizeWidth/7, 0, mainPlain.sizeWidth/7, 0);
            //view.findViewById(R.id.answers_place).setPadding(mainPlain.sizeWidth/4, 0, mainPlain.sizeWidth/4, 0);

        }
             */


    }

    public void answerClick(View view) {
        requireView().findViewById(R.id.testScroll).scrollBy(0, 200);

        this.view.findViewById(R.id.next).setVisibility(View.VISIBLE);
        playWord(null);
        if (approach == ApproachManager.VOC_AUDIO_APPROACH){
            isRight = (answerSelectId == rightId);

            if (isRight){
                setRight((ImageView) answerButtons.get(answerSelectId), requireContext());
            }
            else {
                setRight((ImageView) answerButtons.get(answerSelectId), (ImageView)answerButtons.get(0), (ImageView)answerButtons.get(1),
                        (ImageView)answerButtons.get(2), (ImageView)answerButtons.get(3), requireContext(), answerSelectId);
            }

            answerButtons.get(0).setClickable(false);
            answerButtons.get(1).setClickable(false);
            answerButtons.get(2).setClickable(false);
            answerButtons.get(3).setClickable(false);
            this.view.findViewById(R.id.check).setVisibility(View.GONE);
        }
        else {
            String right;
            right = ((VocabularyCard)card).getWord();

            if (((Button)view).getText().equals(right)){
                setRight((Button)view, requireContext());
                isRight = true;
            }
            else {
                isRight = false;
                setRight(view, (Button) firstAnswer, (Button) secondAnswer, (Button) thirdAnswer, (Button) fourthAnswer, requireContext(), right);
            }

            fourthAnswer.setClickable(false);
            firstAnswer.setClickable(false);
            secondAnswer.setClickable(false);
            thirdAnswer.setClickable(false);
            buttonNext.setEnabled(true);
        }



        TrasportActivities.putWrongcardsBack(mainPlain.activity, isRight,  practiceState, card, index);


}

    private void selectSound(View view){
        select((ImageView) view, (ImageView)answerButtons.get(0), (ImageView)answerButtons.get(1),
                (ImageView)answerButtons.get(2), (ImageView)answerButtons.get(3), getContext());
        if (view.getId() == R.id.sound1)
            answerSelectId = 0;
        else if (view.getId() == R.id.sound2)
            answerSelectId = 1;
        else if (view.getId() == R.id.sound3)
            answerSelectId = 2;
        else if (view.getId() == R.id.sound4)
            answerSelectId = 3;
        playSound(answerSelectId);

        requireView().findViewById(R.id.testScroll).scrollBy(0, 200);
    }

    private void playSound(int select) {
        if (isRealSound)
            soundManager.playSound(select);
        else
            mainPlain.repeatTTS.speak(answer.get(select), TextToSpeech.QUEUE_FLUSH,
                    null);
    }

}
