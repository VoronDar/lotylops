package com.lya_cacoi.lotylops.activities.Games;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.Databases.Auth.User;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlVocabulary;
import com.lya_cacoi.lotylops.Game.GameData;
import com.lya_cacoi.lotylops.Game.GameManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.ruler.Ruler;

import java.util.Random;

public class GameFirstCheckTranslate extends Game {

    private TextView word;
    private TextView translate;

    private Random random;

    // правдивая ли инфа пришла
    private boolean isRight;
    // что нажал пользователь
    private boolean pressed;
    private SqlVocabulary sqlVocabulary;
    private Button accept;
    private Button cancel;

    public GameFirstCheckTranslate(GameData gameData, User user) {
        super(gameData, user);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_first_word_right, container, false);

    }

    @Override
    public void onViewCreated(final @NonNull View view, @Nullable Bundle savedInstanceState) {
        word = view.findViewById(R.id.word_check);
        translate = view.findViewById(R.id.word_translate);



        random = new Random(Ruler.getDay(getContext()));
        sqlVocabulary = SqlVocabulary.getInstance(getContext());
        sqlVocabulary.openDbForLearn();


        final VocabularyCard card;
        if (random.nextBoolean()) {
            card = sqlVocabulary.getRightWordAndTranslate();
            isRight = true;
        }
        else{
            card = sqlVocabulary.getWrongWordAndTranslate();
            isRight = false;
        }

        word.setText(card.getWord());
        translate.setText(card.getTranslate());

        cancel = view.findViewById(R.id.cancel);
        accept = view.findViewById(R.id.accept);

        cancel.setOnClickListener(v -> {
            pressed = false;
            v.setClickable(false);
            accept.setClickable(false);
            checkAnswer();
        });
        accept.setOnClickListener(v -> {
            pressed = true;
            v.setClickable(false);
            cancel.setClickable(false);
            checkAnswer();
        });

        super.onViewCreated(view, savedInstanceState);

    }

    protected boolean isRight(){

        if (isRight==pressed){
            translate.setTextColor(getResources().getColor(R.color.colorGameRightText));
        } else{
            translate.setTextColor(getResources().getColor(R.color.colorGameWrongText));

        }

        return (isRight==pressed);
    }
    protected void loadData(){

        final VocabularyCard card;
        if (random.nextBoolean()) {
            card = sqlVocabulary.getRightWordAndTranslate();
            isRight = true;
        }
        else{
            card = sqlVocabulary.getWrongWordAndTranslate();
            isRight = false;
        }

        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.become_smaller);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                translate.setTextColor(getResources().getColor(R.color.colorGameText));

                word.setText(card.getWord());
                translate.setText(card.getTranslate());
                word.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.become_bigger));
                translate.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.become_bigger));
                cancel.setClickable(true);
                accept.setClickable(true);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        word.startAnimation(a);
        translate.startAnimation(a);

    }
    protected int getTimeLeftMax(){
        return 10;
    }

    @Override
    public GameManager.GameLog getLog() {
        return new GameManager.GameLog("перевод слова " + word.getText() + " - " + translate.getText(), isRight + "", pressed + "", isRight());
    }
}
