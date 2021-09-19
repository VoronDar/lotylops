package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.Helper.Helper;
import com.lya_cacoi.lotylops.LevelManager.LevelManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.Shop.Shop;
import com.lya_cacoi.lotylops.activities.utilities.Blockable;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.adapters.ToDoAdapter;
import com.lya_cacoi.lotylops.adapters.units.toDoUnit;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.ruler.Ruler;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.CULTURE_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.EXCEPTION_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.Helper.Helper.SHOW_LIBRARY_AFTER;
import static com.lya_cacoi.lotylops.Helper.Helper.SHOW_POOL;
import static com.lya_cacoi.lotylops.activities.mainPlain.dp;

public class DayPlanFragment extends CommonFragment implements Blockable {
    private ToDoAdapter adapter;
    private ArrayList<toDoUnit> units;
    private int newIndex;

    private boolean wasCreated = false;
    private static boolean setEnable = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_day_plan_back, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();

        Helper.showHelp(requireContext(), Helper.Help.start);
        /// DECLARING /////////////////////////////////////////////////////////////////////////////
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        ////////////////////////////////////////////////////////////////////////////////////////////



        /// CHANGING VIEW'S SIZES //////////////////////////////////////////////////////////////////
        /*
        if (mainPlain.sizeRatio < mainPlain.triggerRatio){
            ((TextView)view.findViewById(R.id.dailyPlan)).setTextSize(mainPlain.sizeHeight/45f);
            ((TextView)view.findViewById(R.id.messageError)).setTextSize(mainPlain.sizeHeight/40f);
            dayCount.setTextSize(mainPlain.sizeHeight/(30f*mainPlain.multiple));

        }

        LinearLayout.LayoutParams p =
                (LinearLayout.LayoutParams) view.findViewById(R.id.coin_drawable).getLayoutParams();
        p.height = mainPlain.sizeHeight/((mainPlain.sizeRatio < mainPlain.triggerRatio)?4:5);
         */
        ////////////////////////////////////////////////////////////////////////////////////////////



        ///// 3 states: LEARNING, COMPLETED, HAVEN"T STARTED ///////////////////////////////////////
        if (!Ruler.isDailyPlanCompleted(context)) {
            // user haven't completed the daily plan


            // show user's plan and hide "completed" messages
            recyclerView.setVisibility(VISIBLE);


            if (transportPreferences.getDayPlanState(context) == 0)
                transportPreferences.setDayPlanState(context, 1);

            // CARDS ARE READY TO BE REPEAT
            int cardCountVocabulary = transportPreferences.
                    getTodayLeftRepeatCardQuantity(context, VOCABULARY_INDEX);
            int cardCountGrammar = transportPreferences.
                    getTodayLeftRepeatCardQuantity(context, GRAMMAR_INDEX);
            int cardCountPhraseology = transportPreferences.
                    getTodayLeftRepeatCardQuantity(context, PHRASEOLOGY_INDEX);
            int cardCountException = transportPreferences.
                    getTodayLeftRepeatCardQuantity(context, EXCEPTION_INDEX);
            int cardCountCulture= transportPreferences.
                    getTodayLeftRepeatCardQuantity(context, CULTURE_INDEX);
            int cardCountPhonetics = transportPreferences.
                    getTodayLeftRepeatCardQuantity(context, PHONETIC_INDEX);



            units = new ArrayList<>();
            if (cardCountVocabulary > 0) {
                    units.add(new toDoUnit(false, "повторение", SqlMain.getInstance(context, VOCABULARY_INDEX).getIntroRepeat(),
                            VOCABULARY_INDEX, cardCountVocabulary));
            }
            if (cardCountGrammar > 0) {
                units.add(new toDoUnit(false, "повторение", SqlMain.getInstance(context, GRAMMAR_INDEX).getIntroRepeat(),
                        GRAMMAR_INDEX, cardCountGrammar));
            }
            if (cardCountPhraseology > 0){
                    units.add(new toDoUnit(false, "повторение",
                            SqlMain.getInstance(context, PHRASEOLOGY_INDEX).getIntroRepeat(), PHRASEOLOGY_INDEX, cardCountPhraseology));
            }
            if (cardCountException > 0){
                units.add(new toDoUnit(false, "повторение",
                        SqlMain.getInstance(context, EXCEPTION_INDEX).getIntroRepeat(), EXCEPTION_INDEX, cardCountException));
            }

            if (cardCountPhonetics > 0){
                units.add(new toDoUnit(false, "повторение",
                        SqlMain.getInstance(context, PHONETIC_INDEX).getIntroRepeat(), PHONETIC_INDEX, cardCountPhonetics));
            }

            if (cardCountCulture > 0){
                units.add(new toDoUnit(false, "повторение",
                        SqlMain.getInstance(context, CULTURE_INDEX).getIntroRepeat(), CULTURE_INDEX, cardCountCulture));
            }


            // CARDS ARE READY TO BE LEARNED
            int cardCountStudyVocabulary = transportPreferences.
                    getTodayLeftStudyCardQuantity(context, VOCABULARY_INDEX);
            int cardCountStudyGrammar = transportPreferences.
                    getTodayLeftStudyCardQuantity(context, GRAMMAR_INDEX);
            int cardCountStudyPhraseology = transportPreferences.
                    getTodayLeftStudyCardQuantity(context, PHRASEOLOGY_INDEX);
            int cardCountStudyException = transportPreferences.
                    getTodayLeftStudyCardQuantity(context, EXCEPTION_INDEX);
            int cardCountStudyCulture = transportPreferences.
                    getTodayLeftStudyCardQuantity(context, CULTURE_INDEX);
            int cardCountStudyPhonetics = transportPreferences.
                    getTodayLeftStudyCardQuantity(context, PHONETIC_INDEX);

            if (cardCountStudyVocabulary > 0) {
                units.add(new toDoUnit(false, "изучение", SqlMain.getInstance(context, VOCABULARY_INDEX).getIntroStudy(),
                        VOCABULARY_INDEX, cardCountStudyVocabulary));
            }
            if (cardCountStudyGrammar > 0) {
                units.add(new toDoUnit(false, "изучение", SqlMain.getInstance(context, GRAMMAR_INDEX).getIntroStudy(),
                        GRAMMAR_INDEX, cardCountStudyGrammar));
            }
            if (cardCountStudyPhraseology > 0) {
                units.add(new toDoUnit(false, "изучение", SqlMain.getInstance(context, PHRASEOLOGY_INDEX).getIntroStudy(),
                        PHRASEOLOGY_INDEX, cardCountStudyPhraseology));
            }
            if (cardCountStudyException > 0) {
                units.add(new toDoUnit(false, "изучение", SqlMain.getInstance(context, EXCEPTION_INDEX).getIntroStudy(),
                        EXCEPTION_INDEX, cardCountStudyException));
            }
            if (cardCountStudyPhonetics > 0) {
                units.add(new toDoUnit(false, "изучение", SqlMain.getInstance(context, PHONETIC_INDEX).getIntroStudy(),
                        PHONETIC_INDEX, cardCountStudyPhonetics));
            }
            if (cardCountStudyCulture > 0) {
                units.add(new toDoUnit(false, "изучение", SqlMain.getInstance(context, CULTURE_INDEX).getIntroStudy(),
                        CULTURE_INDEX, cardCountStudyCulture));
            }

            for (int i = 0; i < units.size(); i++){
                toDoUnit unit = units.get(i);
                if (unit.getToDo().equals("")){
                    if (unit.getChapterName().equals("изучение")){
                        transportPreferences.setTodayLeftStudyCardQuantity(getContext(), unit.getIndex(), 0);
                    }
                    else{
                        transportPreferences.settTodayLeftRepeatCardQuantity(getContext(), unit.getIndex(), 0);
                    }
                    units.remove(i);
                    break;
                }
            }


            ///// PREPARING THE ADAPTER FOR DAILY PLAN UNITS ///////////////////////////////////////
            adapter = new ToDoAdapter(context, units);
            adapter.setBlockListener(new ToDoAdapter.BlockListener() {

                @Override
                public void onClick(int position) {
                    if (adapter.isClickable()) {
                        newIndex = units.get(position).getIndex();
                        goToTheLearn(position);
                    }
                }

                @Override
                public void onLongClick(int position) {
                }
            });
            adapter.notifyDataSetChanged();
            adapter.setClickable(setEnable);


            recyclerView.setAdapter(adapter);
            recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);


            setTodayLeftSlider();
            // we have two columns of units on a tablet
            //if ((mainPlain.sizeRatio < mainPlain.triggerRatio) && mainPlain.sizeWidth > smallWidth)
                recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2,
                        RecyclerView.VERTICAL, false));
            //else
            //    recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            ////////////////////////////////////////////////////////////////////////////////////////
        }
        else{
            // the daily plan completed or not started

            // add money for completing the plan
            if (transportPreferences.getDayPlanState(context) == 1) {
                transportPreferences.setDayPlanState(context, 2);
                transportPreferences.setMoney(context, Shop.ADD_EVERY_DAY_MONEY +
                        transportPreferences.getMoney(context));

                transportPreferences.setDaysWorkedCount(getContext(),
                        transportPreferences.getDaysWorkedCount(getContext()) + 1);


            }
            if (transportPreferences.getDayPlanState(context) == 2) {
                view.findViewById(R.id.dailyPlan).setVisibility(GONE);
            }


        }

        ////////////////////////////////////////////////////////////////////////////////////////////


        // show help when first daily plan is completed
        if (Ruler.isDailyPlanCompleted(context) &&
                (transportPreferences.getHelpStudy(context) == SHOW_POOL || transportPreferences.getHelpStudy(context) == SHOW_LIBRARY_AFTER)){
            ((mainPlain) requireActivity()).showHelp();
        }


        wasCreated = true;

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void goToTheLearn(int position) {
            if (units.get(position).getChapterName().equals("повторение")) {
                Ruler.dayState = consts.dayState.repeat;
                ((mainPlain) requireActivity()).goToTheRepeating(newIndex);
            }
            if (units.get(position).getChapterName().equals("изучение")) {
                Ruler.dayState = consts.dayState.study;
                ((mainPlain) requireActivity()).goToTheRepeating(newIndex);
            }
    }
    /*
    private void delete_card(int position){
        if (units.get(position).isRemovable()){
            // ЧУ_ЧУТЬ позже
        }
    }

     */


    @Override
    public void setEnable(Boolean enable) {
        setEnable = enable;
        if (wasCreated) {
            if (adapter != null)
            adapter.setClickable(enable);
        }
    }


    @Override
    public String getTitle() {
        return null;
    }


    private void setTodayLeftSlider(){
        LevelManager.show(requireContext(), requireView().findViewById(R.id.expSlider));

        int left = 0;
        int all = 0;
        for (int i = 0; i < PHONETIC_INDEX; i++){
            left += transportPreferences.getTodayLeftStudyCardQuantity(getContext(), i);
            left += transportPreferences.getTodayLeftRepeatCardQuantity(getContext(), i);

            all += transportPreferences.getAllTodayStudyCount(getContext(), i);
            all += transportPreferences.getAllTodayRepeatCount(getContext(), i);
        }

        View bar = requireView().findViewById(R.id.expToday);

        if (all == 0) all = 1;

        int oneItem = (mainPlain.sizeWidth - dp(60))/all;
        ViewGroup.LayoutParams p = bar.getLayoutParams();
        p.width = oneItem*(all-left);
        if (p.width == 0)
            bar.setVisibility(GONE);
        else
            bar.setVisibility(View.VISIBLE);

    }

}
