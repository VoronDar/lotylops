package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.Test;
import com.lya_cacoi.lotylops.activities.utilities.TrasportActivities;
import com.lya_cacoi.lotylops.cards.ExceptionCard;

import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.getPreparedSoundManager;

public class testClearWriting extends Test {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_train_clear_writing, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView question = view.findViewById(R.id.word);
        question.setVisibility(View.VISIBLE);
        question.setText(((ExceptionCard)card).getQuestion());

        view.findViewById(R.id.check).setOnClickListener(this::answerClick);

        /*
        if (mainPlain.sizeRatio < mainPlain.triggerRatio){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(mainPlain.sizeWidth/7, 0, mainPlain.sizeWidth/7, 0);

            TextView editText = this.view.findViewById(R.id.answer);
            editText.setLayoutParams(params);
            editText.setTextSize((mainPlain.sizeWidth/(20*multiple)));
        }

         */
    }


    private void answerClick(View view) {
        view.setEnabled(false);


        String rightAnswer = ((ExceptionCard) card).getAnswer().toLowerCase();

        TextView editText = this.view.findViewById(R.id.answer);
        editText.setEnabled(false);
        boolean isRight;
        if (editText.getText().toString().toLowerCase().equals(rightAnswer)){
            isRight = true;
            editText.setTextColor(getResources().getColor(R.color.colorWhiteRight));
        }
        else{
        isRight = false;
        showAlertError(rightAnswer, editText.getText().toString());
    }

        TrasportActivities.putWrongcardsBack(mainPlain.activity, isRight,  practiceState, card, index);
        if (isRight)
            TrasportActivities.goNextCardActivity(this, true, practiceState,card, index);

        soundManager = getPreparedSoundManager(mainPlain.activity, card.getId());
        playSound(((ExceptionCard) card).getAnswer());
        if (soundManager != null)
            soundManager.closeSound();

}
}
