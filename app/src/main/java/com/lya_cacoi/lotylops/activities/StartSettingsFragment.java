package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.ApproachManager.ApproachManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.adapters.SettingsAdapter;
import com.lya_cacoi.lotylops.adapters.units.SettingUnit;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;

import static android.view.View.GONE;

public class StartSettingsFragment extends Fragment {

    public enum SettingsIndex{
        AskHelp{
            SettingsIndex getNextIndex(){
                return VocabularyApproach;}
                },
        VocabularyApproach{
            SettingsIndex getNextIndex(){
            return RulesOrIntuit;
        }},
        RulesOrIntuit {
            SettingsIndex getNextIndex(){
                return LevelOfVoc;
            }},
        LevelOfVoc{
            SettingsIndex getNextIndex(){
                return LevelOfPhr;
            }},
        LevelOfPhr{
            SettingsIndex getNextIndex(){
                return LevelOfGr;
            }},
        LevelOfGr{
            /// тут прерывается
            SettingsIndex getNextIndex(){
                return LevelOfPh.getNextIndex();
            }},
        LevelOfEx{
            SettingsIndex getNextIndex(){
                return LevelOfPh;
            }},
        LevelOfPh{
            SettingsIndex getNextIndex(){
                if (StartTimeFragment.isPhonetics ||
                        StartTimeFragment.isGrammar ||
                        StartTimeFragment.isPhraseology ||
                        StartTimeFragment.isVocabulary)
                    return VelocityOfLearning;
                return null;
            }},
        LevelOfCul{
            SettingsIndex getNextIndex(){
                return Themes;
            }},
        Themes{
            SettingsIndex getNextIndex(){
                return VelocityOfLearning;
            }},
        VelocityOfLearning{
            SettingsIndex getNextIndex(){
                return null;
            }};

        abstract SettingsIndex getNextIndex();

    }


    private SettingsAdapter adapter;
    private Context context;
    private ArrayList<SettingUnit> units;
    static SettingsIndex settingsIndex = SettingsIndex.VocabularyApproach;

    public StartSettingsFragment(){
    }
        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.select_setting, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();

        /*

        сделать новомодную заставочку перед этим


        темы: (суть тем, чтобы объединять разные курсы, в том числе разного уровня но с 1 направленностью)
        базовый разговорный
        биология
        искусство (книги, театр)
        бытовые предметы
        социальные науки
        масмедиа (музыка, игры, кино)
        технологии
        информатика
        готовка
        медецина


        НАААХРЕН

        короче - будут несколько лент с подкастами и новостями - для них подписку можно будет оформить здесь, эти ленты уже привязаны к курсам
7


        после определения уровня языка будут предложены темы для изучения

         */


        /*
        if (mainPlain.sizeRatio < mainPlain.triggerRatio){
            if ((view.findViewById(R.id.settingsText)) != null)
                ((TextView)view.findViewById(R.id.settingsText)).setTextSize(mainPlain.sizeHeight/45f);
            else{
                ((TextView)view.findViewById(R.id.settingsName)).setTextSize(mainPlain.sizeHeight/(35f*mainPlain.multiple));
                ((TextView)view.findViewById(R.id.buttonAccept)).setTextSize(mainPlain.sizeHeight/(35f*mainPlain.multiple));
                ((TextView)view.findViewById(R.id.buttonCancel)).setTextSize(mainPlain.sizeHeight/(35f*mainPlain.multiple));
                ((Button)view.findViewById(R.id.buttonAccept)).setHeight(mainPlain.sizeHeight/8);
                ((Button)view.findViewById(R.id.buttonCancel)).setHeight(mainPlain.sizeHeight/8);
                if (view.findViewById(R.id.command) != null){
                    ((TextView)view.findViewById(R.id.command)).setTextSize(mainPlain.sizeHeight/(25f*mainPlain.multiple));
                    ((TextView)view.findViewById(R.id.command2)).setTextSize(mainPlain.sizeHeight/(25f*mainPlain.multiple));
                    ((TextView)view.findViewById(R.id.description)).setTextSize(mainPlain.sizeHeight/(30f*mainPlain.multiple));
                }
                else{
                    ((TextView)view.findViewById(R.id.description)).setTextSize(mainPlain.sizeHeight/(33f*mainPlain.multiple));
                    ((TextView)view.findViewById(R.id.warning)).setTextSize(mainPlain.sizeHeight/(33f*mainPlain.multiple));
                    ((TextView)view.findViewById(R.id.editText)).setTextSize(mainPlain.sizeHeight/(10f*mainPlain.multiple));
                    ((TextView)view.findViewById(R.id.newWords)).setTextSize(mainPlain.sizeHeight/(35f*mainPlain.multiple));
                }

            }
        }
        
         */

        boolean isHorisontal = false;                                                               // if select blocks are horisontal and lie on other blocks
        boolean isManySelected = false;                                                             // if the user can select several blocks

        SettingUnit.DoSomehtingInteface doSomehtingIntefaceBoth = null;
        SettingUnit.DoSomehtingInteface doSomehtingIntefaceNone = null;
        String BothDescription = null;
        String NoneDescription = "";

        final Button cancel = view.findViewById(R.id.buttonCancel);
        cancel.setVisibility(View.VISIBLE);
        final Button accept = view.findViewById(R.id.buttonAccept);
        final TextView settingsName = view.findViewById(R.id.fragment_title);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        if (recyclerView != null)
            recyclerView.setHasFixedSize(true);
        units = new ArrayList<>();
        adapter = new SettingsAdapter(view.getContext(), units);

        if (settingsIndex == SettingsIndex.VocabularyApproach) {
                settingsName.setText(R.string.settings_title_vocabulary_approach);
                units.add(new SettingUnit(R.drawable.icon_vocabular_audio, getString(R.string.start_setting_audio), false, getString(R.string.settings_start_desc_audio), () -> transportPreferences.setVocabularyApproach(context, ApproachManager.VOC_AUDIO_APPROACH)));
                units.add(new SettingUnit(R.drawable.icon_vocabular_read, getString(R.string.start_setting_text), false, getString(R.string.settings_start_desrc_text), () -> transportPreferences.setVocabularyApproach(context, ApproachManager.VOC_READ_APPROACH)));
            }
        else if (settingsIndex == SettingsIndex.RulesOrIntuit) {
            settingsName.setText(R.string.settings_title_grammar_approach);
            units.add(new SettingUnit(R.drawable.grammar_deduction, getString(R.string.start_setting_deduciton), false, getString(R.string.settings_start_desc_deduction), () -> transportPreferences.setVocabularyApproach(context, ApproachManager.VOC_AUDIO_APPROACH)));
            units.add(new SettingUnit(R.drawable.grammar_transduction, getString(R.string.start_setting_transdiction), false, getString(R.string.settings_start_desc_transduction), () -> transportPreferences.setVocabularyApproach(context, ApproachManager.VOC_READ_APPROACH)));
        }



            if (units.size()==2) {
                recyclerView.setVisibility(GONE);
                view.findViewById(R.id.linearLayout).setVisibility(View.VISIBLE);
                isHorisontal = true;

                final TextView description = view.findViewById(R.id.description);
                final TextView command1 = view.findViewById(R.id.command);
                final TextView command2 = view.findViewById(R.id.command2);
                final View indicator = view.findViewById(R.id.indicator);
                final View indicator2 = view.findViewById(R.id.indicator2);
                final ImageView image1 = view.findViewById(R.id.image);
                final ImageView image2 = view.findViewById(R.id.image2);

                command1.setText(units.get(0).getCommand());
                command2.setText(units.get(1).getCommand());
                image1.setImageResource(units.get(0).getImageID());
                image2.setImageResource(units.get(1).getImageID());

                final boolean finalIsManySelected = isManySelected;
                final String finalBothDescription = BothDescription;
                final String finalNoneDescription = NoneDescription;
                view.findViewById(R.id.setting_block_one).setOnClickListener(v -> {
                    Log.i("main", "click 1");
                    if (!finalIsManySelected) {
                        description.setText(units.get(0).getDescription());
                        units.get(0).setChecked(true);
                        units.get(1).setChecked(false);
                        indicator.setVisibility(View.VISIBLE);
                        indicator2.setVisibility(GONE);
                    } else {
                        if (units.get(0).isChecked()) {
                            description.setText(units.get(1).getDescription());
                            units.get(0).setChecked(false);
                            indicator.setVisibility(GONE);
                        } else {
                            description.setText(units.get(0).getDescription());
                            units.get(0).setChecked(true);
                            indicator.setVisibility(View.VISIBLE);
                        }
                    }
                    if (units.get(0).isChecked() && units.get(1).isChecked()) {
                        description.setText(finalBothDescription);
                    } else if (!units.get(0).isChecked() && !units.get(1).isChecked()) {
                        if (finalNoneDescription.equals(""))
                            accept.setVisibility(GONE);
                        description.setText(finalNoneDescription);
                    } else
                        accept.setVisibility(View.VISIBLE);
                });

                view.findViewById(R.id.setting_block_two).setOnClickListener(v -> {
                    Log.i("main", "click 2");
                    if (!finalIsManySelected) {
                        description.setText(units.get(1).getDescription());
                        units.get(1).setChecked(true);
                        units.get(0).setChecked(false);
                        indicator2.setVisibility(View.VISIBLE);
                        indicator.setVisibility(GONE);
                    } else {
                        if (units.get(1).isChecked()) {
                            description.setText(units.get(0).getDescription());
                            units.get(1).setChecked(false);
                            indicator2.setVisibility(GONE);
                        } else {
                            description.setText(units.get(1).getDescription());
                            units.get(1).setChecked(true);
                            indicator2.setVisibility(View.VISIBLE);
                        }
                    }
                    if (units.get(0).isChecked() && units.get(1).isChecked()) {
                        description.setText(finalBothDescription);
                    } else if (!units.get(0).isChecked() && !units.get(1).isChecked()) {
                        if (finalNoneDescription.equals(""))
                            accept.setVisibility(GONE);
                        description.setText(finalNoneDescription);
                    } else
                        accept.setVisibility(View.VISIBLE);


                });

                if (!isManySelected) {
                    if (units.get(0).isChecked())
                        view.findViewById(R.id.setting_block_one).callOnClick();
                    else
                        view.findViewById(R.id.setting_block_two).callOnClick();
                }
                else
                {
                    Log.i("main", "isChecked 0 " + units.get(0).isChecked());
                    Log.i("main", "isChecked 1 " + units.get(1).isChecked());

                    if (units.get(0).isChecked() && units.get(1).isChecked()) {
                        description.setText(BothDescription);
                        indicator2.setVisibility(View.VISIBLE);
                        indicator.setVisibility(View.VISIBLE);
                    }
                    else if (units.get(0).isChecked()) {
                        indicator.setVisibility(View.VISIBLE);
                        indicator2.setVisibility(GONE);
                        description.setText(units.get(0).getDescription());
                    }
                    else if (units.get(1).isChecked()) {
                        indicator2.setVisibility(View.VISIBLE);
                        indicator.setVisibility(GONE);
                        description.setText(units.get(1).getDescription());
                    }
                    else {
                        description.setText(NoneDescription);
                        indicator.setVisibility(GONE);
                        indicator2.setVisibility(GONE);
                    }
                }

            }
            else{
                view.findViewById(R.id.linearLayout).setVisibility(GONE);


                final TextView description = view.findViewById(R.id.description);

                for (SettingUnit unit : units){
                    if (unit.isChecked()){
                        description.setText(unit.getDescription());
                        break;
                    }
                }


                adapter.setBlockListener(position -> {
                    ((TextView)view.findViewById(R.id.description)).setText(units.get(position).getDescription());
                    for (int i = 0; i < units.size(); i++){
                        if (units.get(i).isChecked())
                            units.get(i).setChecked(false);
                    }
                    units.get(position).setChecked(true);
                    adapter.notifyDataSetChanged();
                });
            }


            final SettingUnit.DoSomehtingInteface finalDoSomehtingInteface = doSomehtingIntefaceBoth;

            final boolean finalIsManySelected1 = isManySelected;
            final SettingUnit.DoSomehtingInteface finalDoSomehtingIntefaceNone = doSomehtingIntefaceNone;


            cancel.setOnClickListener(v -> {
                settingsIndex = settingsIndex.getNextIndex();
                if (settingsIndex == SettingsIndex.VocabularyApproach || settingsIndex == SettingsIndex.RulesOrIntuit)
                    ((startAppActivity)requireActivity()).slideFragment(new StartSettingsFragment());
                else if (settingsIndex != null)
                    ((startAppActivity)requireActivity()).slideFragment(new StartTestFragment());
                else{
                    ((startAppActivity)requireActivity()).moveToMain();
                }
            });
            accept.setOnClickListener(v -> {
                if (finalIsManySelected1 && units.get(0).isChecked() && units.get(1).isChecked())
                    finalDoSomehtingInteface.doSomething();
                else if(finalDoSomehtingIntefaceNone!= null && !units.get(0).isChecked() && !units.get(1).isChecked())
                    finalDoSomehtingIntefaceNone.doSomething();
                else{
                    for (int i = 0; i < units.size(); i++) {
                        if (units.get(i).isChecked() == true) {
                            units.get(i).doSomething();
                            break;
                        }
                    }
                }

                settingsIndex = settingsIndex.getNextIndex();
                if (settingsIndex == SettingsIndex.VocabularyApproach || settingsIndex == SettingsIndex.RulesOrIntuit)
                    ((startAppActivity)requireActivity()).slideFragment(new StartSettingsFragment());
                else if (settingsIndex != null)
                    ((startAppActivity)requireActivity()).slideFragment(new StartTestFragment());
                else{
                    ((startAppActivity)requireActivity()).moveToMain();
                }
            });



        if (!isHorisontal) {
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            recyclerView.setAdapter(adapter);
            recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            adapter.notifyDataSetChanged();
            }
        }

    }

