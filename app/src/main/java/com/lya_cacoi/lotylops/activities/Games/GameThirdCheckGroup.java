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
import androidx.fragment.app.Fragment;

import com.lya_cacoi.lotylops.Databases.Auth.User;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlVocabulary;
import com.lya_cacoi.lotylops.Game.GameData;
import com.lya_cacoi.lotylops.Game.GameManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.cards.VocabularyCard;
import com.lya_cacoi.lotylops.ruler.Ruler;

import java.util.Random;

public class GameThirdCheckGroup extends Game {
    private Random random;

    /** правдивая ли инфа пришла */
    private boolean isRight;
    /** что нажал пользователь */
    private boolean pressed;
    private SqlVocabulary sqlVocabulary;
    private VocabularyCard card;

    private Button accept;
    private Button cancel;
    private TextView word;
    private TextView synonym;

    public GameThirdCheckGroup(GameData gameData, User user) {
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
        synonym = view.findViewById(R.id.word_translate);

        ((TextView)view.findViewById(R.id.taskName)).setText("Выбрано правильное описание слова?");

        random = new Random(Ruler.getDay(getContext()));
        sqlVocabulary = SqlVocabulary.getInstance(getContext());
        sqlVocabulary.openDbForLearn();

        setData();
        word.setText(card.getWord());
        synonym.setText(card.getGroup());

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
            synonym.setTextColor(getResources().getColor(R.color.colorGameRightText));
        } else{
            synonym.setTextColor(getResources().getColor(R.color.colorGameWrongText));

        }

        return (isRight==pressed);
    }
    protected void loadData(){

        setData();
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.become_smaller);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                synonym.setTextColor(getResources().getColor(R.color.colorGameText));

                word.setText(card.getWord());
                synonym.setText(card.getGroup());
                word.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.become_bigger));
                synonym.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.become_bigger));
                cancel.setClickable(true);
                accept.setClickable(true);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        word.startAnimation(a);
        synonym.startAnimation(a);

    }
    protected int getTimeLeftMax(){
        return 7;
    }


    @Override
    public GameManager.GameLog getLog() {
        return new GameManager.GameLog("группа слова " + card.getWord() + " - " + synonym.getText(), isRight +"" , pressed+"", isRight());
    }

    private void setData(){
        int counter = 100;
        if (random.nextBoolean()) {
            isRight = true;



            do {
                card = (VocabularyCard) sqlVocabulary.getRandomCardFromStatic();
            } while (card == null || card.getGroup() == null && --counter > 0);


            if (counter <= 0){
                extraQuit();
            }
        }
        else{
            isRight = false;
            do {
                card = (VocabularyCard) sqlVocabulary.getRandomCardFromStatic();
            } while (card.getGroup() != null && card.getGroup().equals("термин"));

            int group = random.nextInt(6);
            switch (group){
                case 0:
                    if (card.getGroup() == null || !card.getGroup().equals("устаревшее")) {
                        card.setGroup("устаревшее");
                        break;
                    }
                case 1:
                if (card.getGroup() == null || !card.getGroup().equals("формальное")) {
                    card.setGroup("формальное");
                    break;
                }
                case 2:
                if (card.getGroup() == null || !card.getGroup().equals("сленг")) {
                    card.setGroup("сленг");
                    break;
                }
                case 3:
                    if (card.getGroup() != null && !card.getGroup().equals("общеупотребительное")) {
                        card.setGroup("общеупотребительное");
                        break;
                    } else{
                        if (random.nextBoolean()) card.setGroup("разговорное");
                        else card.setGroup("формальное");
                    }
                case 4:
                    if (card.getGroup() == null || !card.getGroup().equals("неформальное")) {
                        card.setGroup("неформальное");
                    }
                case 5:
                    if (card.getGroup() == null || !card.getGroup().equals("старомодное, но не вышедшее из обихода")) {
                        card.setGroup("старомодное, но не вышедшее из обихода");
                    }
            }
            /*
            группы слов

            устаревшее
            общеупотребительное
            разговорное
            формальное
            сленговое
            книжное
            термин
            старомодное, но не вышедшее из обихода


            ! общеупотребительное предлагается только в вариантах с уже существующей группой, ибо пустая группа - это простое общеупотребительное слово

            термины не пихать
             */
        }
    }


    @Override
    public Fragment getSisterGame() {
        return new GameSecondWordWrite(gameData, user);
    }
}
