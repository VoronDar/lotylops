package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.Achieve.AchieveManager;
import com.lya_cacoi.lotylops.LevelManager.LevelManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.Shop.Shop;
import com.lya_cacoi.lotylops.Statistics.StatisticManager;
import com.lya_cacoi.lotylops.activities.utilities.Blockable;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.activities.utilities.panel.ShopPanel;
import com.lya_cacoi.lotylops.activities.utilities.panel.SimpleInfoPannel;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static androidx.core.app.ActivityCompat.finishAffinity;

public class ExtraActionsFragment extends CommonFragment implements Blockable, ShopPanel.ShopHoldable {

    private boolean setEnable = true;
    /** причина, закрывшая магаз: -1 ничего, с 1 по порядку кнопки*/
    private int reason = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_extra_actions, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LevelManager.show(requireContext(), requireView().findViewById(R.id.expSlider));

        requireView().findViewById(R.id.restIcon).setOnClickListener(v -> {
            if (!setEnable){ ShopPanel.closeShop(mainPlain.activity, this);  reason = 1; return;}
            finishAffinity(requireActivity());
            setEnable = false;
        });
        requireView().findViewById(R.id.learnIcon).setOnClickListener(v -> {
            if (!setEnable){ ShopPanel.closeShop(mainPlain.activity, this);  reason = 2;return;}
            if (Shop.Thing.addPlan.isPaid(requireContext())){
                mainPlain.activity.slideFragment(new DayPlanFragment());
            }
            else {
                ShopPanel.openShop(mainPlain.activity, this, Shop.Thing.addPlan, this);
            }
            setEnable = false;
        });
        requireView().findViewById(R.id.practiceIcon).setOnClickListener(v -> {
            if (!setEnable){ ShopPanel.closeShop(mainPlain.activity, this); reason = 3;return;}
            if (Shop.Thing.practice.isPaid(requireContext())){
                mainPlain.activity.slideFragment(new PoolFragment());
            }
            else {
                ShopPanel.openShop(mainPlain.activity, this, Shop.Thing.practice, this);
            }
            setEnable = false;
        });
        requireView().findViewById(R.id.playIcon).setOnClickListener(v -> {
            if (!setEnable){ ShopPanel.closeShop(mainPlain.activity, this); reason = 4;return;}

            if (transportPreferences.getPlayerName(requireContext()) == null )
                ((mainPlain)requireActivity()).slideFragment(new GameStartFragment());
            else
                ((mainPlain)requireActivity()).slideFragment(new GamePlayFragment());
        });

        // todo таки дабить, чтобы nothing started нормально работал
        //if (!transportPreferences.isDayCompleted(requireContext()) && !transportPreferences.isNothingStarted(requireContext())){
        if (!transportPreferences.isDayCompleted(requireContext())){
            SimpleInfoPannel.openPanel(mainPlain.activity, "Поздравляем! Вы успешно завершили дневной план!",
                    "Вы получили 8 монет", R.drawable.coin_size);
            transportPreferences.setDayCompleted(requireContext(), true);
            transportPreferences.setMoney(requireContext(), transportPreferences.getMoney(requireContext())+8);
            StatisticManager.endDay(requireContext());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public String getTitle() {
        return null;
    }


    @Override
    public void setEnable(Boolean enable) {
        setEnable = enable;
    }

    @Override
    public void onShopClosed() {
        setEnable = true;

        switch (reason){
            case -1:
                return;
            case 1:
                finishAffinity(requireActivity());
                break;
            case 2:
                ShopPanel.openShop(mainPlain.activity, this, Shop.Thing.addPlan, this);
                if (Shop.Thing.addPlan.isPaid(requireContext())){
                    mainPlain.activity.slideFragment(new DayPlanFragment());
                }
                else {
                    ShopPanel.openShop(mainPlain.activity, this, Shop.Thing.addPlan, this);
                }
                break;
            case 3:
                if (Shop.Thing.practice.isPaid(requireContext())){
                    mainPlain.activity.slideFragment(new PoolFragment());
                }
                else {
                    ShopPanel.openShop(mainPlain.activity, this, Shop.Thing.practice, this);
                }
                ShopPanel.openShop(mainPlain.activity, this, Shop.Thing.practice, this);
                break;
            case 4:
                if (transportPreferences.getPlayerName(requireContext()) == null )
                    ((mainPlain)requireActivity()).slideFragment(new GameStartFragment());
                else
                    ((mainPlain)requireActivity()).slideFragment(new GamePlayFragment());
        }
        reason = -1;
    }

}
