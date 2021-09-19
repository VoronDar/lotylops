package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.ApproachManager.ApproachManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils;
import com.lya_cacoi.lotylops.activities.utilities.Test;
import com.lya_cacoi.lotylops.activities.utilities.TrainButtonsChanger;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;
import java.util.Collections;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.activities.utilities.TrainButtonsChanger.setRight;

public class testGuess extends Test {


    Button firstAnswer;
    Button secondAnswer ;
    Button thirdAnswer;
    Button fourthAnswer;
    String right;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_train_translate, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String task = "Угадайте что значит "
                + ((index == VOCABULARY_INDEX || !((VocabularyCard) card).getWord().contains(" "))?"слово":"фраза") +
                " '" + ((VocabularyCard) card).getWord() +"'";

        ((TextView)view.findViewById(R.id.part)).setText(task);

        TextView word = view.findViewById(R.id.word);
        if (transportPreferences.getVocabularyApproach(getContext()) ==
                ApproachManager.VOC_AUDIO_APPROACH){
            word.setVisibility(View.GONE);
            view.findViewById(R.id.soundPlay).setVisibility(View.VISIBLE);
            view.findViewById(R.id.soundPlay).setOnClickListener(
                    v -> mainPlain.repeatTTS.speak(((VocabularyCard)card).getExamples().get(0).getSentence(), TextToSpeech.QUEUE_FLUSH,
                    null));
            mainPlain.repeatTTS.speak(((VocabularyCard)card).getExamples().get(0).getSentence(), TextToSpeech.QUEUE_FLUSH,
                    null);
        }
        else {
            word.setVisibility(View.VISIBLE);
            word.setText(((VocabularyCard) card).getExamples().get(0).getSentence());
        }

        boolean isTranslate = true;
        boolean isNativeMeaning = true;

        ArrayList<String> answer = new ArrayList<>(4);


        if (transportPreferences.getTranslatePriority(getContext()) > consts.PRIORITY_NEVER &&
        ((VocabularyCard)card).getTranslate() != null){
            answer.add(((VocabularyCard) card).getTranslate());
        }
        else if(((VocabularyCard)card).getMeaning() != null ||
                ((VocabularyCard)card).getMeaningNative() != null ||
                ((VocabularyCard)card).getTranslate() != null){
            isTranslate = false;
            if (((VocabularyCard)card).getMeaningNative() != null && (transportPreferences.getMeaningLanguage(getContext()) == consts.LANGUAGE_BOTH ||
                    transportPreferences.getMeaningLanguage(getContext()) == consts.LANGUAGE_NATIVE)) {
                answer.add(((VocabularyCard) card).getMeaningNative());
                isNativeMeaning = true;
            }
            else if (((VocabularyCard)card).getMeaning() != null &&
                    transportPreferences.getMeaningLanguage(getContext()) >= consts.LANGUAGE_NEW){
                isNativeMeaning = false;
                answer.add(((VocabularyCard) card).getMeaning());
            }
            else if (((VocabularyCard)card).getMeaningNative() != null){
                answer.add(((VocabularyCard) card).getMeaningNative());
                isNativeMeaning = true;
            }
            else if (((VocabularyCard)card).getMeaning() != null){
                isNativeMeaning = false;
                answer.add(((VocabularyCard) card).getMeaning());
            }
            else if (((VocabularyCard)card).getTranslate() != null)
                answer.add(((VocabularyCard) card).getTranslate());
        }
        else{
            Log.i("main", "HERE1");
            ActivitiesUtils.createErrorWindow(mainPlain.activity, () -> goNext(null), card.getId(), getResources().getString(R.string.no_translate_meaning));
            return;
        }
        right = answer.get(0);


        int failedCount;
            for (int i = 0; i < 3; i++) {
                failedCount = 0;
                VocabularyCard newCard;
                String translate;
                do {
                    newCard = (VocabularyCard)ruler.getRandomCard();
                    if (isTranslate)
                        translate = newCard.getTranslate();
                    else if (isNativeMeaning)
                        translate = newCard.getMeaningNative();
                    else
                        translate = (newCard).getMeaning();

                }
                while ((translate == null || answer.contains(translate) ||
                        newCard.getWord().equals(((VocabularyCard)card).getWord())) &&
                  ++failedCount < 10);
                if (failedCount>= 10){
                    //mainPlain.repeatTTS.stop();
                    //Toast.makeText(getContext(), "недостаточно слов, чтобы запустить тест", Toast.LENGTH_LONG).show();
                    isRight = true;
                    goNext(view);
                    return;
                }
                answer.add(translate);
            }


        Collections.shuffle(answer);
        firstAnswer = view.findViewById(R.id.firstAnswer);
        firstAnswer.setText(answer.get(0));
        firstAnswer.setOnClickListener(this::answerClick);
        secondAnswer = view.findViewById(R.id.secondAnswer);
        secondAnswer.setText(answer.get(1));
        secondAnswer.setOnClickListener(this::answerClick);
        thirdAnswer = view.findViewById(R.id.thirdAnswer);
        thirdAnswer.setText(answer.get(2));
        thirdAnswer.setOnClickListener(this::answerClick);
        fourthAnswer = view.findViewById(R.id.fourthAnswer);
        fourthAnswer.setText(answer.get(3));
        fourthAnswer.setOnClickListener(this::answerClick);

        /*
        if (mainPlain.sizeRatio < mainPlain.triggerRatio){
            if (isTranslate){
            firstAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(15*multiple));
            secondAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(15*multiple));
            thirdAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(15*multiple));
            fourthAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(15*multiple));}
            else{
                firstAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(20*multiple));
                secondAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(20*multiple));
                thirdAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(20*multiple));
                fourthAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(20*multiple));
            }
            word.setTextSize((mainPlain.sizeHeight/(22*multiple)));
            ((ImageView)view.findViewById(R.id.container)).setMaxHeight((int)(mainPlain.sizeHeight/2.75f));
            ((ImageView)view.findViewById(R.id.soundPlay)).setMaxHeight(mainPlain.sizeHeight/7);
        }

         */
        /*
        else{
            if (isTranslate) {
                firstAnswer.setTextSize((mainPlain.sizeHeight / 2.75f) / 45);
                secondAnswer.setTextSize((mainPlain.sizeHeight / 2.75f) / 45);
                thirdAnswer.setTextSize((mainPlain.sizeHeight / 2.75f) / 45);
                fourthAnswer.setTextSize((mainPlain.sizeHeight / 2.75f) / 45);
            }
            else{
                    firstAnswer.setTextSize((mainPlain.sizeHeight / 2.75f) / 60);
                    secondAnswer.setTextSize((mainPlain.sizeHeight / 2.75f) / 60);
                    thirdAnswer.setTextSize((mainPlain.sizeHeight / 2.75f) / 60);
                    fourthAnswer.setTextSize((mainPlain.sizeHeight / 2.75f) / 60);
                }
            ((ImageView)view.findViewById(R.id.container)).setMaxHeight((int)(mainPlain.sizeHeight/2.45f));
        }

         */

    }

    private void answerClick(View view) {

        if (((Button)view).getText().equals(right)){
            setRight((Button)view, requireContext());
            isRight = true;
        }
        else {
            isRight = false;
            TrainButtonsChanger.setRight(view, firstAnswer, secondAnswer, thirdAnswer, fourthAnswer, requireContext(), right);
        }


        fourthAnswer.setClickable(false);
        firstAnswer.setClickable(false);
        secondAnswer.setClickable(false);
        thirdAnswer.setClickable(false);

        setNextButtonAvailable();
        //goNext(null);

}

}
