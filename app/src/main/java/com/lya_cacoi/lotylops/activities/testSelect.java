package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.Test;
import com.lya_cacoi.lotylops.activities.utilities.TrainButtonsChanger;
import com.lya_cacoi.lotylops.activities.utilities.TrasportActivities;
import com.lya_cacoi.lotylops.cards.CultureCard;
import com.lya_cacoi.lotylops.cards.GrammarCard;
import com.lya_cacoi.lotylops.cards.PhoneticsCard;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.ruler.Ruler;

import java.util.ArrayList;
import java.util.Collections;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.CULTURE_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.PhoneticsManager.isSelectSound;
import static com.lya_cacoi.lotylops.activities.utilities.TrainButtonsChanger.setRight;

public class testSelect extends Test {


    Button firstAnswer;
    Button secondAnswer ;
    Button thirdAnswer;
    Button fourthAnswer;
    String right;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_train_select, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> answer = new ArrayList<>(4);

        GrammarCard.SelectTrain selectTrain;
        if (Ruler.getSQLIndex() == GRAMMAR_INDEX) {
            if ((card.getPracticeLevel()-1) >= ((GrammarCard) card).getSelectTrains().size()){
                TrasportActivities.goNextCardActivity(this, true, practiceState, card, GRAMMAR_INDEX);
                return;
            }
            selectTrain = ((GrammarCard) card).getSelectTrains().get(card.getPracticeLevel() - 1);
        }
        else if (Ruler.getSQLIndex() == PHONETIC_INDEX) {
            if ((card.getPracticeLevel()-1) >= ((PhoneticsCard) card).getSelectTrains().size()){
                TrasportActivities.goNextCardActivity(this, true, practiceState, card, PHONETIC_INDEX);
                return;
            }
            selectTrain = ((PhoneticsCard) card).getSelectTrains().get(card.getPracticeLevel() - 1);

            ((TextView)view.findViewById(R.id.part)).setText("выберите правильный ответ");
            if (isSelectSound(selectTrain.getAnswer())){
                mainPlain.activity.slideFragment(new testSelectSound());
                return;
            }
        }
        else if (Ruler.getSQLIndex() == CULTURE_INDEX) {

            ((TextView)view.findViewById(R.id.part)).setText("выберите правильный ответ");

            selectTrain = ((CultureCard)card).getSelectTrain();
            if (selectTrain == null){
                TrasportActivities.goNextCardActivity(this, true, practiceState, card, CULTURE_INDEX);
                return;
            }
            //if (selectTrain == null) return;
        } else{
            ((TextView)view.findViewById(R.id.part)).setText("выберите правильный вариант");
            selectTrain = ((VocabularyCard)card).getDialogueTest();
            if (selectTrain == null){
                TrasportActivities.goNextCardActivity(this, true, practiceState, card, index);
                return;
            }
        }

        TextView sentence = view.findViewById(R.id.word);
        sentence.setText(selectTrain.getAnswer());

        right = selectTrain.getRight();

        answer.add(selectTrain.getRight());
        answer.add(selectTrain.getSecond());
        if (selectTrain.getThird() != null) answer.add(selectTrain.getThird());
        if (selectTrain.getFourth() != null) answer.add(selectTrain.getFourth());
        Collections.shuffle(answer);
        firstAnswer = view.findViewById(R.id.firstAnswer);
        firstAnswer.setText(answer.get(0));
        firstAnswer.setOnClickListener(this::answerClick);
        secondAnswer = view.findViewById(R.id.secondAnswer);
        secondAnswer.setText(answer.get(1));
        secondAnswer.setOnClickListener(this::answerClick);
        thirdAnswer = view.findViewById(R.id.thirdAnswer);
        fourthAnswer = view.findViewById(R.id.fourthAnswer);
        if (answer.size() > 2) {
            if (answer.size() == 4) {
                fourthAnswer.setVisibility(View.VISIBLE);
                fourthAnswer.setText(answer.get(3));
                fourthAnswer.setOnClickListener(this::answerClick);
                //view.findViewById(R.id.upperAnswer).setMinimumHeight(400);
            } else {
                thirdAnswer = view.findViewById(R.id.anotherThirdAnswer);
            }
            thirdAnswer.setVisibility(View.VISIBLE);
            thirdAnswer.setText(answer.get(2));
            thirdAnswer.setOnClickListener(this::answerClick);
        }

        /*
        if (mainPlain.sizeRatio < mainPlain.triggerRatio){
                firstAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(15*multiple));
                secondAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(15*multiple));
                thirdAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(15*multiple));
                fourthAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/(15*multiple));
            sentence.setTextSize((mainPlain.sizeHeight/2.75f)/(12*multiple));
        }

         */
        /*
        else{
            firstAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/50);
            secondAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/50);
            thirdAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/50);
            fourthAnswer.setTextSize((mainPlain.sizeHeight/2.75f)/50);
        }

         */

    }

    private void answerClick(View view) {

        requireView().findViewById(R.id.next).setClickable(true);


        if (((Button)view).getText().equals(right)){
            setRight((Button) view, requireContext());
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
        if (index != CULTURE_INDEX)
            mainPlain.repeatTTS.speak(right, TextToSpeech.QUEUE_FLUSH, null);
        TrasportActivities.putWrongcardsBack(mainPlain.activity, isRight,  practiceState, card, index);



}

}
