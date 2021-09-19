package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.Test;
import com.lya_cacoi.lotylops.activities.utilities.TrasportActivities;
import com.lya_cacoi.lotylops.activities.utilities.panel.ErrorPanel;
import com.lya_cacoi.lotylops.activities.utilities.panel.InfoHoldable;
import com.lya_cacoi.lotylops.cards.VocabularyCard;

import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.getPreparedSoundManager;

public class testWritingClear extends Test {
    private String wordString;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_train_clear_writing, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setMeaningAndTranslate();

        wordString = ((VocabularyCard)card).getWord().toLowerCase();

        view.findViewById(R.id.check).setOnClickListener(this::answerClick);

        /*
        TextView editText = this.view.findViewById(R.id.answer);
        if (mainPlain.sizeRatio < mainPlain.triggerRatio){
            editText.setTextSize(mainPlain.sizeHeight/(20*multiple));
        }


        if (mainPlain.sizeRatio < mainPlain.triggerRatio)
            view.findViewById(R.id.meaning).setPadding(mainPlain.sizeWidth/7, 0,
                    mainPlain.sizeWidth/7, 0);

         */
        
    }


    public void answerClick(View view) {
        view.setEnabled(false);



        TextView editText = this.view.findViewById(R.id.answer);
        editText.setEnabled(false);

        boolean isRight;
        if (editText.getText().toString().toLowerCase().replaceAll("[.,!\n]","").equals(wordString)){
            isRight = true;
            editText.setTextColor(getResources().getColor(R.color.colorWhiteRight));
        }
        else{
        isRight = false;
            ErrorPanel.openPanel((InfoHoldable) requireActivity(), editText.getText().toString(),
                    ((VocabularyCard) card).getWord(), null, (ErrorPanel.DoAfter) () -> goNext(null));
    }


        TrasportActivities.putWrongcardsBack(mainPlain.activity, isRight,  practiceState, card, index);

        if (isRight)
            TrasportActivities.goNextCardActivity(this, true, practiceState,card, index);

        soundManager = getPreparedSoundManager(mainPlain.activity, card.getId());
        playWord(null);
        if (soundManager != null)
            soundManager.closeSound();

}
}
