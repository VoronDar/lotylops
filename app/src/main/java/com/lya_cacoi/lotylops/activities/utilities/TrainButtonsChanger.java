package com.lya_cacoi.lotylops.activities.utilities;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lya_cacoi.lotylops.R;

public class TrainButtonsChanger {
    public static void setRight(View view, Button firstAnswer, Button secondAnswer, Button thirdAnswer, Button fourthAnswer, Context context, String right){
        ((Button) view).setTextColor(context.getResources().getColor(R.color.buttonWrongColour));
        view.setBackground(context.getResources().getDrawable(R.drawable.button_wrong_answer_background));
        ((Button) view).setTextColor(context.getResources().getColor(R.color.colorAccent));
        if (firstAnswer.getText().equals(right)) {
            firstAnswer.setTextColor(context.getResources().getColor(R.color.buttonRightColour));
            firstAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_right_answer_background));
        } else if (secondAnswer.getText().equals(right)) {
            secondAnswer.setTextColor(context.getResources().getColor(R.color.buttonRightColour));
            secondAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_right_answer_background));
        } else if (thirdAnswer.getText().equals(right)) {
            thirdAnswer.setTextColor(context.getResources().getColor(R.color.buttonRightColour));
            thirdAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_right_answer_background));
        } else {
            fourthAnswer.setTextColor(context.getResources().getColor(R.color.buttonRightColour));
            fourthAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_right_answer_background));
        }


    }


    public static void setRight(ImageView clicked, ImageView firstAnswer, ImageView secondAnswer, ImageView thirdAnswer, ImageView fourthAnswer, Context context, int id){

        clicked.setBackground(context.getResources().getDrawable(R.drawable.button_wrong_answer_background));
        clicked.setImageResource(R.drawable.ic_volume_unpressed);
        if (id == 1) {
            firstAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_rounded_background));
            firstAnswer.setImageResource(R.drawable.ic_volume);
        } else if (id == 2) {
            secondAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_rounded_background));
            secondAnswer.setImageResource(R.drawable.ic_volume);
        } else if (id == 3) {
            thirdAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_rounded_background));
            thirdAnswer.setImageResource(R.drawable.ic_volume);
        } else {
            fourthAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_rounded_background));
            fourthAnswer.setImageResource(R.drawable.ic_volume);
        }
    }

    public static void setRight(Button view, Context context) {
        (view).setTextColor(context.getResources().getColor(R.color.colorWhite));
        view.setBackground(context.getResources().getDrawable(R.drawable.button_rounded_background));
    }

    public static void setRight(ImageView view, Context context) {
        view.setBackground(context.getResources().getDrawable(R.drawable.button_rounded_background));
        view.setImageResource(R.drawable.ic_volume);
    }


    public static void select(ImageView clicked, ImageView firstAnswer, ImageView secondAnswer, ImageView thirdAnswer, ImageView fourthAnswer, Context context){

        firstAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_select_background));
        firstAnswer.setImageResource(R.drawable.ic_volume_unpressed);
        secondAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_select_background));
        secondAnswer.setImageResource(R.drawable.ic_volume_unpressed);
        thirdAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_select_background));
        thirdAnswer.setImageResource(R.drawable.ic_volume_unpressed);
        fourthAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_select_background));
        fourthAnswer.setImageResource(R.drawable.ic_volume_unpressed);
        clicked.setBackground(context.getResources().getDrawable(R.drawable.button_rounded_background));
        clicked.setImageResource(R.drawable.ic_volume);
    }
}
