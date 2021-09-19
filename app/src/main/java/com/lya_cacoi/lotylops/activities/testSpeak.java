package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import com.lya_cacoi.lotylops.cards.PhoneticsCard;

public class testSpeak extends Test {
    private boolean isRight;
    private String wordString;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_test_speak, null);
        return inflater.inflate(R.layout.activity_test_speak, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wordString = ((PhoneticsCard)card).getWordTrains().get(card.getPracticeLevel()-1);
        ((TextView)view.findViewById(R.id.word)).setText(wordString);
        view.findViewById(R.id.check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerClick(v);
            }
        });

        TextView editText = this.view.findViewById(R.id.editText);
        //if (mainPlain.sizeRatio < mainPlain.triggerRatio){
        //    editText.setTextSize(mainPlain.sizeHeight/(20*multiple));
        //}
        
    }


    public void answerClick(View view) {
        view.setEnabled(false);

        isRight = true;

        if (false)
            {
        isRight = false;
            AlertDialog.Builder adb = new AlertDialog.Builder(mainPlain.activity);
            View my_custom_view = getLayoutInflater().inflate(R.layout.error_layout, null);
            adb.setView(my_custom_view);
            TextView rightAnswer = my_custom_view.findViewById(R.id.rightAnswer);
            rightAnswer.setText(wordString);
            TextView yourAnswer = my_custom_view.findViewById(R.id.yourAnswer);
            //yourAnswer.setText(editText.getText());
            adb.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    goNext(null);
                }
            });
            final AlertDialog ad = adb.create();
            my_custom_view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.cancel();
                }
            });

            if (mainPlain.sizeRatio < mainPlain.triggerRatio){
                rightAnswer.setTextSize(mainPlain.sizeHeight/(30f* mainPlain.multiple));
                yourAnswer.setTextSize(mainPlain.sizeHeight/(30f* mainPlain.multiple));
                ((TextView)my_custom_view.findViewById(R.id.errorName)).
                        setTextSize(mainPlain.sizeHeight/(40f* mainPlain.multiple));
                ((Button)my_custom_view.findViewById(R.id.next)).
                        setTextSize(mainPlain.sizeHeight/(35f* mainPlain.multiple));
                ((Button)my_custom_view.findViewById(R.id.next)).
                        setHeight((int)(mainPlain.sizeHeight/(12f)));
                rightAnswer.setTextSize(mainPlain.sizeHeight/(30f* mainPlain.multiple));
            }

            ad.show();
    }


        TrasportActivities.putWrongcardsBack(mainPlain.activity, isRight,  practiceState, card, index);

        if (isRight)
            TrasportActivities.goNextCardActivity(this, isRight, practiceState,card, index);

        //soundManager = getPreparedSoundManager(mainPlain.activity, card.getId());
        //playWord(null);
        playSound(wordString);
        if (soundManager != null)
            soundManager.closeSound();

}
}
