package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.LevelManager.LevelManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.Blockable;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.adapters.VocabularyPracticeAdapter;
import com.lya_cacoi.lotylops.adapters.units.VocabularyPracticeUnit;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.ruler.Ruler;

import java.util.ArrayList;

public class PoolFragment extends CommonFragment implements Blockable {
    private ArrayList<VocabularyPracticeUnit> units;
    private boolean setEnabled = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pool_new, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();


        LevelManager.show(requireContext(), requireView().findViewById(R.id.expSlider));

        //if (mainPlain.sizeRatio < mainPlain.triggerRatio)
        //    ((TextView)view.findViewById(R.id.practiceType)).setTextSize(mainPlain.sizeHeight/45f);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        units = new ArrayList<>(4);
        units.add(new VocabularyPracticeUnit(consts.SELECT_REPEAT_NEXT_DAY, context.getResources().getString(R.string.select_test_only_next_level)));
        units.add(new VocabularyPracticeUnit(consts.SELECT_REPEAT_ALL_COURSE, context.getResources().getString(R.string.select_test_all_course)));
        //units.add(new VocabularyPracticeUnit(consts.SELECT_PASS_EXAM, context.getResources().getString(R.string.select_test_pass_exam)));
        units.add(new VocabularyPracticeUnit(consts.SELECT_ALL_TODAY_CARDS, context.getResources().getString(R.string.select_test_all_today_cards)));


            VocabularyPracticeAdapter adapter = new VocabularyPracticeAdapter(units);
        adapter.setBlockListener(position -> {
            if (setEnabled) {
                Ruler.setPractiseChapter(units.get(position).getID());
                ((mainPlain) requireActivity()).goTothePractice();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter.notifyDataSetChanged();


    }

    @Override
    public void setEnable(Boolean enable) {
        setEnabled = enable;
    }

    @Override
    public String getTitle() {
        return "Повторение";
    }
}
