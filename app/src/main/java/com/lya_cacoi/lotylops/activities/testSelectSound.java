package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.Test;
import com.lya_cacoi.lotylops.activities.utilities.TrasportActivities;
import com.lya_cacoi.lotylops.cards.GrammarCard;
import com.lya_cacoi.lotylops.cards.PhoneticsCard;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;

public class testSelectSound extends Test {
    private ArrayList<String> answer;
    private ArrayList<View> answerButtons;
    private int answerSelectId          = -1;
    private int rightId                 = -1;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_test_select_sound, null);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GrammarCard.SelectTrain selectTrain;

        if ((card.getPracticeLevel()-1) >= ((PhoneticsCard) card).getSelectTrains().size()){
            TrasportActivities.goNextCardActivity(this, true, practiceState, card, PHONETIC_INDEX);
            return;
        }
        selectTrain = ((PhoneticsCard) card).getSelectTrains().get(card.getPracticeLevel() - 1);

        if (selectTrain.getAnswer() == null || selectTrain.getRight() == null || selectTrain.getSecond() == null){
            if ((card.getPracticeLevel()-1) >= ((PhoneticsCard) card).getSelectTrains().size()) {
                TrasportActivities.goNextCardActivity(this, true, practiceState, card, PHONETIC_INDEX);
                return;
            }
        }

        ((TextView)view.findViewById(R.id.word)).setText("какое произношение соответствует слову " + selectTrain.getAnswer().substring(1));

        ((TextView)view.findViewById(R.id.part)).setText("выберите правильное звучание слова");


            answerButtons = new ArrayList<>(2);
            answerButtons.add((view.findViewById(R.id.sound1)));
            answerButtons.add((view.findViewById(R.id.sound2)));
            buttonNext.setEnabled(true);

            for (View button: answerButtons){
                button.setOnClickListener(this::selectSound);

            }

        answer = new ArrayList<>(2);

        answer.add(selectTrain.getRight());
        answer.add(selectTrain.getSecond());


        view.findViewById(R.id.check).setOnClickListener(this::answerClick);

        Log.i("main", "rightId - " + rightId);
        //ruler.closeDatabase();




            if (mainPlain.sizeRatio < mainPlain.triggerRatio){
                ((ImageView)view.findViewById(R.id.container)).setMaxHeight((int)(mainPlain.sizeHeight/2.75f));
            }


        if (mainPlain.sizeRatio < mainPlain.triggerRatio){
            ((ImageView)view.findViewById(R.id.container)).setMaxWidth((int)(mainPlain.sizeWidth*0.85f));
            view.findViewById(R.id.meaning).setPadding(mainPlain.sizeWidth/7, 0, mainPlain.sizeWidth/7, 0);
            view.findViewById(R.id.answers_place).setPadding(mainPlain.sizeWidth/4, 0, mainPlain.sizeWidth/4, 0);

        }

    }

    public void answerClick(View view) {
        requireView().findViewById(R.id.testScroll).scrollBy(0, 200);


            isRight = (answerSelectId == rightId);
            for (int i = 0; i < answerButtons.size(); i++){
                answerButtons.get(i).setAlpha(0.3f);
                answerButtons.get(i).setEnabled(false);
            }
            answerButtons.get(rightId).setAlpha(1);
            this.view.findViewById(R.id.check).setVisibility(View.GONE);

            buttonNext.setEnabled(true);

            playSound(answer.get(rightId));




        TrasportActivities.putWrongcardsBack(mainPlain.activity, isRight,  practiceState, card, index);


}

    private void selectSound(View view){
        if (view.getId() == R.id.sound1) {
            answerSelectId = 0;
        }
        else if (view.getId() == R.id.sound2) {
            answerSelectId = 1;
        }

        playSound(answer.get(answerSelectId));

        for (int i = 0; i < answerButtons.size(); i++)
            answerButtons.get(i).setAlpha(1);
        view.setAlpha(0.5f);
        Log.i("main", "id - " + answerSelectId);

        requireView().findViewById(R.id.testScroll).scrollBy(0, 200);
    }

}
