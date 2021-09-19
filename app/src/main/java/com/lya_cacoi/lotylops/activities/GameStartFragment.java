package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.Helper.Helper;
import com.lya_cacoi.lotylops.LevelManager.LevelManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.Shop.Shop;
import com.lya_cacoi.lotylops.activities.utilities.Blockable;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.activities.utilities.panel.ShopPanel;
import com.lya_cacoi.lotylops.adapters.SettingsAdapter;
import com.lya_cacoi.lotylops.adapters.units.SettingUnit;
import com.lya_cacoi.lotylops.ruler.Ruler;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;

import static android.view.View.VISIBLE;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;

public class GameStartFragment extends CommonFragment implements Blockable, ShopPanel.ShopHoldable {



    private SettingsAdapter adapter;
    private ArrayList<SettingUnit> units;

    public static int selectedId = -1;

    private boolean isEnable = true;

    public GameStartFragment(){
    }
        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_start, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        LevelManager.show(requireContext(), getView().findViewById(R.id.expSlider));
        Helper.showHelp(requireContext(), Helper.Help.gameStart);

        /*
        if (mainPlain.sizeRatio < mainPlain.triggerRatio){
            if ((view.findViewById(R.id.settingsText)) != null)
                ((TextView)view.findViewById(R.id.settingsText)).setTextSize(mainPlain.sizeHeight/45f);
            else{
                ((TextView)view.findViewById(R.id.settingsName)).setTextSize(mainPlain.sizeHeight/(35f*mainPlain.multiple));
                ((TextView)view.findViewById(R.id.buttonCancel)).setTextSize(mainPlain.sizeHeight/(35f*mainPlain.multiple));
                ((Button)view.findViewById(R.id.buttonAccept)).setHeight(mainPlain.sizeHeight/8);
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


        if (!transportPreferences.isGameAllowed(getContext())){
            view.findViewById(R.id.informer).setVisibility(VISIBLE);
            ((TextView)view.findViewById(R.id.informer)).setText("Добавьте курсы слов, чтобы играть в игру");
            view.findViewById(R.id.buttonGoLibrary).setVisibility(VISIBLE);
            view.findViewById(R.id.buttonGoLibrary).setOnClickListener(v -> mainPlain.activity.slideFragment(new LibraryCourseFragment(VOCABULARY_INDEX)));

            return;
        }


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        units = new ArrayList<>();

        units.add(new SettingUnit(R.drawable.avatar_two, "Джеб (легкий)", () -> {
            transportPreferences.setPlayerName(getContext(), "1");
            return new GamePlayFragment();
        }));
        units.add(new SettingUnit(R.drawable.gamer2, "Джо (средний)", () -> {
            transportPreferences.setPlayerName(getContext(), "2");
            return new GamePlayFragment();
        }));
        units.add(new SettingUnit(R.drawable.avatar_one, "Джен (сложный)", () -> {
            transportPreferences.setPlayerName(getContext(), "3");
            return new GamePlayFragment();
        }));


        adapter = new SettingsAdapter(view.getContext(), units);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter.notifyDataSetChanged();
        adapter.setClickable(true);
        setEnable(true);
                adapter.setBlockListener(position -> {
                            if (selectedId != -1){
                                ShopPanel.closeShop(mainPlain.activity, this);
                            }
                            if (!isEnable) return;
                                for (int i = 0; i < units.size(); i++){
                                    if (i == position) continue;
                                    if (units.get(i).isChecked()) {
                                        units.get(i).setChecked(false);
                                    }
                                }
                            units.get(position).setChecked(true);
                            adapter.notifyDataSetChanged();
                            selectedId = position;

                            if (!isEnable || selectedId == -1) return;
                            units.get(selectedId).openAlert();
                            ShopPanel.openShop(mainPlain.activity, this, Shop.Thing.game, this);
                            selectedId = -1;
                        });
        if (!Ruler.isDailyPlanCompleted(getContext())){
            recyclerView.setVisibility(View.INVISIBLE);
            view.findViewById(R.id.informer).setVisibility(VISIBLE);
        }

            }


    @Override
    public void setEnable(Boolean enable) {
        isEnable = enable;
    }

    @Override
    public String getTitle() {
        return "игра";
    }

    @Override
    public void onShopClosed() {
        isEnable = true;
        if (selectedId != -1){
            units.get(selectedId).openAlert();
            ShopPanel.openShop(mainPlain.activity, this, Shop.Thing.game, this);
        }
        selectedId = -1;
    }
}

