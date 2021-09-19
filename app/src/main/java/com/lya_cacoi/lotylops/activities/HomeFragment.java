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

import com.lya_cacoi.lotylops.Achieve.AchieveManager;
import com.lya_cacoi.lotylops.LevelManager.LevelManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.Statistics.StatisticManager;
import com.lya_cacoi.lotylops.activities.utilities.Blockable;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.activities.utilities.FragmentsServer;
import com.lya_cacoi.lotylops.activities.utilities.panel.InfoHoldable;
import com.lya_cacoi.lotylops.activities.utilities.panel.SimpleInfoPannel;
import com.lya_cacoi.lotylops.adapters.AchieveAdapter;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

public class HomeFragment extends CommonFragment implements Blockable {
    private boolean setEnabled = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentsServer.setTitle(getTitle());

        ((TextView)view.findViewById(R.id.level)).setText(LevelManager.getLevel(getContext()) + " уровень");
        //((TextView)view.findViewById(R.id.name)).setText(transportPreferences.getName(getContext()));
        ((TextView)view.findViewById(R.id.name)).setText("Пользователь111");
        ((TextView)view.findViewById(R.id.email)).setText("super@super.com");
        //((TextView)view.findViewById(R.id.email)).setText(transportPreferences.getEmail(getContext()));
        ((TextView)view.findViewById(R.id.coins)).setText(transportPreferences.getMoney(getContext()) + "");


        AchieveAdapter adapter = new AchieveAdapter(getContext(), AchieveManager.getAchieves(getContext()));
        adapter.setBlockListener(position -> SimpleInfoPannel.openPanel((InfoHoldable) requireActivity(), AchieveManager.getTitle(position+1), null, AchieveManager.getImage(position+1)));
        adapter.notifyDataSetChanged();
        RecyclerView achievesView = view.findViewById(R.id.achieves);
        achievesView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        achievesView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        achievesView.setAdapter(adapter);


        view.findViewById(R.id.move).setOnClickListener(v -> mainPlain.activity.slideFragment(new StatistcsFragment()));

        StatisticManager.setDayWorkedView(requireContext(), 0, requireView().findViewById(R.id.firstDay));
        StatisticManager.setDayWorkedView(requireContext(), 1, requireView().findViewById(R.id.secondDay));
        StatisticManager.setDayWorkedView(requireContext(), 2, requireView().findViewById(R.id.thirdDay));
        StatisticManager.setDayWorkedView(requireContext(), 3, requireView().findViewById(R.id.fourthDay));
        StatisticManager.setDayWorkedView(requireContext(), 4, requireView().findViewById(R.id.fifthDay));
        StatisticManager.setDayWorkedView(requireContext(), 5, requireView().findViewById(R.id.sixthDay));
        StatisticManager.setDayWorkedView(requireContext(), 6, requireView().findViewById(R.id.seventhDay));


    }

    @Override
    public void setEnable(Boolean enable) {
        setEnabled = enable;
    }

    @Override
    public String getTitle() {
        return "Профиль";
    }
}
