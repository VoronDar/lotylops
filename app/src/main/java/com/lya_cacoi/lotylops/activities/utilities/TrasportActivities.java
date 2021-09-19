package com.lya_cacoi.lotylops.activities.utilities;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.lya_cacoi.lotylops.LevelManager.LevelManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.ruler.Ruler;

public class TrasportActivities {

    public static void goNextCardActivity(Fragment lastFragment, boolean isRight, consts.cardState practiceState, Card card, int sqlIndex){
        Ruler ruler = Ruler.getInstance(mainPlain.activity, sqlIndex);

        Log.i("exp", "practiceState:" + practiceState);
        ////// УРОВЕНЬ /////////////////////////////////////////////////////////////////////////////
        if (practiceState == consts.cardState.practice) {
            if (isRight)
                LevelManager.addExp(lastFragment.requireContext(), LevelManager.Action.practice);
            else
                LevelManager.addExp(lastFragment.requireContext(), LevelManager.Action.practiceWrong);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////




        //Intent intent;
        Fragment fragment;
        if (Ruler.isIsPracticeNow())
            switch (Ruler.getPractiseChapter()){
                case consts.SELECT_REPEAT_NEXT_DAY:
                    Log.i("main", "here");
                    ruler.putCardWhilePracticeForTheNextTest(card, isRight);
                    fragment = ruler.changeActivityWhilePracticeForTheNextTests(lastFragment);
                    break;
                case consts.SELECT_ALL_TODAY_CARDS:
                case consts.SELECT_REPEAT_ALL_COURSE:
                    Log.i("main", "there");
                    ruler.putCardWhilePracticeForTheNextTest(card, isRight);
                    fragment = ruler.changeActivityWhilePracticeForTheAllCards(lastFragment);
                    break;
                default:
                    fragment = ruler.changeActivityWhilePracticeForTheExam(lastFragment);
        }
        else
            fragment = ruler.closeTest(mainPlain.activity, isRight, practiceState, card);
        //ruler.closeDatabase();

        Log.i("main", fragment.toString());
        mainPlain.activity.slideFragment(fragment);


    }

    public static void skipPracticeCard(Fragment lastFragment, Card card, int sqlIndex){
        Ruler ruler = Ruler.getInstance(mainPlain.activity, sqlIndex);
        Fragment fragment;
        switch (Ruler.getPractiseChapter()){
            case consts.SELECT_REPEAT_NEXT_DAY:
                fragment = ruler.skipPracticeNextTest(lastFragment, card.getId());
            break;
            case consts.SELECT_ALL_TODAY_CARDS:
            case consts.SELECT_REPEAT_ALL_COURSE:
                fragment = ruler.skipPracticeAllCards(lastFragment, card.getId());
                break;
            default:
                fragment = null;
        }
        ruler.closeDatabase();
        mainPlain.activity.slideFragment(fragment);
    }

    public static void putWrongcardsBack(AppCompatActivity context, boolean isRight, consts.cardState practiceState, Card card, int sqlIndex){

        Ruler ruler = Ruler.getInstance(context, sqlIndex);
        if (!Ruler.isIsPracticeNow()){
            if (!isRight) {
                ruler.putErrorPracticeCardBack(context, practiceState, card);
            }
        }
        else{
            Button button = context.findViewById(R.id.skip);
            button.setVisibility(View.GONE);
            if (isRight){
                LevelManager.addExp(context, LevelManager.Action.practice);
            } else
                LevelManager.addExp(context, LevelManager.Action.practiceWrong);
        }
    }

    public static void showSkipButton(View context){
        if (Ruler.isIsPracticeNow() && Ruler.getPractiseChapter() != consts.SELECT_PASS_EXAM){
            Button button = context.findViewById(R.id.skip);
            button.setVisibility(View.VISIBLE);
            Log.i("main", "show skip button");
        }
    }

}
