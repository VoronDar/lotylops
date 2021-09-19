package com.lya_cacoi.lotylops.activities.Games;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lya_cacoi.lotylops.Databases.Auth.User;
import com.lya_cacoi.lotylops.Game.GameData;
import com.lya_cacoi.lotylops.Game.GameManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.GameLogFragment;
import com.lya_cacoi.lotylops.activities.GamePlayFragment;
import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

public abstract class Game extends CommonFragment {

    /** The number of properly solved tasks - the number of mistakes*/
    private int test_right_count = 0;

    protected GameData gameData;
    protected User user;

    /** был ли выход из задания произведен правильно
     *  влияет на то, будет ли запущен doneGame, который закрывает ход
     *  запускает, если равно false
     * */
    private boolean rightClosed = false;

    public Game(GameData gameData, User user) {
        this.gameData = gameData;
        this.user = user;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transportPreferences.setTurnType(getContext(), gameData.getTurn_type());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!rightClosed)
            doneGame();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTimerView();
    }

    /** set timer */
    private void setTimerView(){
        View timerView = requireView().findViewById(R.id.timer);
        Animation a = new ScaleAnimation(timerView.getScaleX(), 0,
                timerView.getScaleY(), timerView.getScaleY());
        a.setDuration(getTimeLeftMax()*1000);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                doneGame();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        timerView.startAnimation(a);
        ((LinearLayout)requireView().findViewById(R.id.timerLayout)).setWeightSum(getTimeLeftMax());

    }



    /** определение правильности ответа - для каждой конкретной игры работает по-разному*/
    protected abstract boolean isRight();
    protected abstract void loadData();
    protected abstract int getTimeLeftMax();
    /** получить список ответов и их описание (правильно/неправильно)*/
    public abstract GameManager.GameLog getLog();

    /**
     * определяется в конкретных играх
     * @return возвращает фрагмент игры, которая проходится на том же уровне, что и эта, если null - то такого фрагмента нет
     * */
    public Fragment getSisterGame(){
        return null;
    }

    /** проверка правильности ответа */
    @SuppressLint("SetTextI18n")
    final void checkAnswer(){
        // игровые логи - сохранение правильных и неправильных ответов со всего задания
        GameManager.logs.add(getLog());

        if (isRight()) test_right_count++;
        else test_right_count--;
        loadData();

        ((TextView)requireView().findViewById(R.id.score)).
                setText(Integer.toString(gameData.getTurn_type()*test_right_count));
    }

    /** closing the game turn */
    private void doneGame(){
        rightClosed = true;

        //// ЗАКРЫТИЕ КЛАВИАТУРЫ ///////////////////////////////////////////////////////////////////
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////


        //// ЗАКРЫТИЕ ХОДА /////////////////////////////////////////////////////////////////////////
        if (gameData.getTurn_player() == 1){
            gameData.setOnes_score(gameData.getTurn_type()*test_right_count);
            gameData.setTurn_player(2);
            if (gameData.getSecond_scores() != -1) GameManager.turnDone(getContext(), gameData, false);
        } else{
            gameData.setSecond_scores(gameData.getTurn_type()*test_right_count + 1);
            gameData.setTurn_player(1);
            if (gameData.getOnes_score() != -1) GameManager.turnDone(getContext(), gameData, false);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////


        if (getActivity() != null)
            ((mainPlain)getActivity()).slideFragment(new GameLogFragment(gameData));
    }


    @Override
    public String getTitle() {
        return null;
    }

    /**
     * выкидывает из игры по уважительным причинам, к примеру - из-за отсутствия достаточного кол-ва карт
     * определяется в конкретных классах игр, там же и вызывается
     * нужен для того, чтобы перейти в другую игру
     *
     * if there is no sister game - it throws to GamePlayFragment
     */
    final protected void extraQuit(){
        rightClosed = true;
        Fragment sister = getSisterGame();
        if (sister == null){
            mainPlain.activity.slideFragment(new GamePlayFragment(gameData));
            Toast.makeText(getContext(), "Не удалось запустить эту игру, попробуйте добавить больше курсов", Toast.LENGTH_LONG).show();
        } else
            mainPlain.activity.slideFragment(sister);

    }
}
