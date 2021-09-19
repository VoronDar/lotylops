package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.adapters.TimePurposeAdapter;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;

public class StartTimeFragment extends Fragment {


    private TimePurposeAdapter adapter;
    private Context context;
    private ArrayList<TimePurposeAdapter.Item> units;

    private final int VOCABULARY_WEIGHT = 4;
    private final int PHRASEOLOGY_WEIGHT = 3;
    private final int GRAMMAR_WEIGHT = 2;
    private final int PHONETICS_WEIGHT = 1;

    private final int VOCABULARY_COUNT_PER_MINUTE = 2;
    private final int GRAMMAR_COUNT_PER_MINUTE = 1;


    static boolean isVocabulary = false;
    static boolean isPhraseology = false;
    static boolean isGrammar = false;
    static boolean isPhonetics = false;



    public StartTimeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_time, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();

        /*

        как рассчитывается время:
            15 секунд на слово и фразу --> 4 в минуту, 30 за 10 минут (на погрешности)
            30 секунд на правило        --> 2 в минуту, 15 за 10 минут
            40 секунд на фонетику       --> 1.5 в минуту, 10 за 10 минут


            так. При расчете времени:
            40% на вокабуляр
            30% на фразы
            20% на грамматику
            10% на фонетику

            (у каждого свой вес равный проценту)


            варианты времени:
                до 5 минут - легкий
                10 минут - средний
                до 20 минут - сложный
         */

        units = new ArrayList<>();
        units.add(new TimePurposeAdapter.Item("до 5 минут в день"));
        units.add(new TimePurposeAdapter.Item("10 минут в день"));
        units.add(new TimePurposeAdapter.Item("15 минут в день"));
        units.add(new TimePurposeAdapter.Item("20 минут в день"));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        adapter = new TimePurposeAdapter(getContext(), units);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        adapter.setBlockListener(new TimePurposeAdapter.BlockListener() {
            @Override
            public void onClick(int position) {
                if (adapter.nowPressedIndex == position) {
                    adapter.lastPressedIndex = adapter.nowPressedIndex;
                    adapter.nowPressedIndex = -1;
                    adapter.notifyItemChanged(position);
                } else {
                    adapter.lastPressedIndex = adapter.nowPressedIndex;
                    adapter.nowPressedIndex = position;
                    adapter.notifyItemChanged(position);
                    if (adapter.lastPressedIndex != -1)
                        adapter.notifyItemChanged(adapter.lastPressedIndex);
                }
            }
        });

        final Button accept = view.findViewById(R.id.buttonAccept);
        final Button cancel = view.findViewById(R.id.buttonCancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move();
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int weightSum = ((isVocabulary)?VOCABULARY_WEIGHT:0) +
                        ((isPhraseology)?PHRASEOLOGY_WEIGHT:0) +
                        ((isGrammar)?GRAMMAR_WEIGHT:0) +
                        ((isPhonetics)?PHONETICS_WEIGHT:0);

                int time = (adapter.nowPressedIndex+1)*5;

                int phonCount = (isPhonetics)?(int)((float)(time/weightSum)*PHONETICS_WEIGHT):0;
                if (phonCount==0 && isPhonetics) phonCount=1;

                int grammarCount = (isGrammar)?(int)((float)(time/weightSum)*GRAMMAR_WEIGHT):0;
                if (grammarCount==0 && isGrammar) grammarCount=1;

                int phrasCount = (isPhraseology)?(int)((float)(time/weightSum)*PHRASEOLOGY_WEIGHT):0;
                if (phrasCount==0 && isPhraseology) phrasCount=1;

                int vocCount = (isVocabulary)?(time - phonCount - grammarCount - phrasCount):0;
                if (vocCount==0 && isVocabulary) vocCount=1;

                Log.i("main", "phonCount " + phonCount);
                Log.i("main", "grammarCount " + grammarCount);
                Log.i("main", "phrasCount " + phrasCount);
                Log.i("main", "vocCount " + vocCount);

                transportPreferences.setPaceVocabulary(context, ((isVocabulary)?vocCount*VOCABULARY_COUNT_PER_MINUTE:5));
                transportPreferences.setPaceGrammar(context, ((isGrammar)?grammarCount*GRAMMAR_COUNT_PER_MINUTE:2));
                transportPreferences.setPacePhraseology(context, ((isPhraseology)?phrasCount*VOCABULARY_COUNT_PER_MINUTE:5));
                transportPreferences.setPacePhonetics(context, ((isPhonetics)?phonCount*GRAMMAR_COUNT_PER_MINUTE:2));

                move();
            }
        });
    }


    private void move() {
        StartSettingsFragment.settingsIndex = StartSettingsFragment.settingsIndex.getNextIndex();
        if (StartSettingsFragment.settingsIndex == StartSettingsFragment.settingsIndex.VelocityOfLearning){
            ((startAppActivity) getActivity()).slideFragment(new StartTimeFragment());
        }
        else if (StartSettingsFragment.settingsIndex != null)
            ((startAppActivity) getActivity()).slideFragment(new StartTestFragment());
        else {
            ((startAppActivity) getActivity()).moveToMain();
        }

    }
}


