package com.lya_cacoi.lotylops.activities.Games;

import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.Databases.Auth.User;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlVocabulary;
import com.lya_cacoi.lotylops.Game.GameData;
import com.lya_cacoi.lotylops.Game.GameManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.adapters.ButtonLetterAdapter;
import com.lya_cacoi.lotylops.adapters.units.buttonLetterUnit;
import com.lya_cacoi.lotylops.cards.VocabularyCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameSecondWordWrite extends Game {

    private TextView word;
    private TextView editText;

    private boolean isRight;
    private SqlVocabulary sqlVocabulary;
    private Button accept;
    private Button cancel;

    private ButtonLetterAdapter adapter;
    private ArrayList<buttonLetterUnit> units;
    private int lettersCount = 0;

    private VocabularyCard card;

    public GameSecondWordWrite(GameData gameData, User user) {
        super(gameData, user);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_second_writing_, container, false);

    }

    @Override
    public void onViewCreated(final @NonNull View view, @Nullable Bundle savedInstanceState) {
        word = view.findViewById(R.id.word);
        editText = view.findViewById(R.id.editText);

        sqlVocabulary = SqlVocabulary.getInstance(requireContext());
        sqlVocabulary.openDbForLearn();



        do {
            card = (VocabularyCard) sqlVocabulary.getRandomCardFromStatic();
        } while (card.getWord() == null || card.getTranslate() == null);


        word.setText(card.getTranslate());

        cancel = view.findViewById(R.id.cancel);
        accept = view.findViewById(R.id.accept);


        units = new ArrayList<>();
        setUnits(card);
        adapter.setBlockListener(position -> {
            TextView word = requireView().findViewById(R.id.editText);
            if (units.get(position).isPressed()){
                String wordString = word.getText().toString();
                wordString = wordString.substring(0, units.get(position).getWordPosition()-1) + wordString.substring(units.get(position).getWordPosition());
                word.setText(wordString);
                units.get(position).setPressed(false);
                int oldPos =  units.get(position).getWordPosition();
                units.get(position).setWordPosition(0);
                for (int i = 0; i < units.size(); i++){
                    if (units.get(i).getWordPosition() != 0 && units.get(i).getWordPosition() > oldPos){
                        units.get(i).setWordPosition(units.get(i).getWordPosition()-1);
                    }
                }
                lettersCount = word.getText().toString().length();
                adapter.notifyDataSetChanged();
            }
            else{
                lettersCount = word.getText().toString().length();
                units.get(position).setPressed(true);
                units.get(position).setWordPosition(++lettersCount);
                adapter.notifyDataSetChanged();
                word.setText(word.getText() + units.get(position).getLetter());
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.blocks);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 7));
        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter.notifyDataSetChanged();

        cancel.setOnClickListener(v1 -> {
            isRight = false;
            v1.setClickable(false);
            accept.setClickable(false);
            checkAnswer();
        });
        accept.setOnClickListener(v12 -> {
            isRight = (editText.getText().toString().toLowerCase().equals(card.getWord().toLowerCase()));
            Log.i("main", editText.getText().toString() + " " + card.getWord());

            v12.setClickable(false);
            cancel.setClickable(false);
            checkAnswer();
        });

        super.onViewCreated(view, savedInstanceState);

    }

    protected boolean isRight(){
        return isRight;
    }
    protected void loadData(){

        do {
            card = (VocabularyCard) sqlVocabulary.getRandomCardFromStatic();
        } while (card.getWord() == null || card.getTranslate() == null);

        if (isRight) {
            word.setTextColor(requireContext().getResources().getColor(R.color.colorWhiteRight));
            editText.setTextColor(requireContext().getResources().getColor(R.color.colorWhiteRight));
        } else{
            word.setTextColor(requireContext().getResources().getColor(R.color.colorAccent));
            editText.setTextColor(requireContext().getResources().getColor(R.color.colorAccent));
        }

        Animation a = AnimationUtils.loadAnimation(requireContext(), R.anim.become_smaller);


        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                word.setTextColor(requireContext().getResources().getColor(R.color.colorWhiteTranslate));
                editText.setTextColor(requireContext().getResources().getColor(R.color.colorWhiteExample));
                setUnits(card);
                word.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.become_bigger));
                editText.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.become_bigger));
                requireView().findViewById(R.id.blocks).startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.become_bigger));
                cancel.setClickable(true);
                accept.setClickable(true);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        word.startAnimation(a);
        editText.startAnimation(a);
        requireView().findViewById(R.id.blocks).startAnimation(a);

    }
    protected int getTimeLeftMax(){
        return 30;
    }


    @Override
    public GameManager.GameLog getLog() {
        return new GameManager.GameLog("перевод слова " + card.getWord() , card.getTranslate() , editText.getText().toString(), isRight);
    }

    private void setUnits(VocabularyCard card){
        word.setText(card.getTranslate());
        editText.setText("");
        units.clear();
        for (int i = 0; i < card.getWord().length(); i++){
            units.add(new buttonLetterUnit(card.getWord().substring(i, i+1)));
        }
        for (int i = 0; i < (units.size()+7)%7; i++){
            Random r = new Random();
            String c = String.valueOf((char)(r.nextInt(26) + 'a'));
            units.add(new buttonLetterUnit(c));
        }
        Collections.shuffle(units);
        if (adapter == null)
            adapter = new ButtonLetterAdapter(requireContext(), units, 0);
        else
            adapter.setUnits(units);

        adapter.notifyDataSetChanged();


    }

    @Override
    public Fragment getSisterGame() {
        return new GameThirdCheckGroup(gameData, user);
    }
}
