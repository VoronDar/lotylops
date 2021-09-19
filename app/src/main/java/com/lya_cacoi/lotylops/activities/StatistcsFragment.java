package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.Statistics.OneDayInfo;
import com.lya_cacoi.lotylops.Statistics.StatisticManager;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.activities.utilities.FragmentsServer;
import com.lya_cacoi.lotylops.adapters.StatAdapter;
import com.lya_cacoi.lotylops.ruler.Ruler;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;
import java.util.List;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;

public class StatistcsFragment extends CommonFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistcs, container, false);
        //editText.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f));


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();

        FragmentsServer.setTitle(getTitle());
        List<OneDayInfo> infos = StatisticManager.getStats(getContext(), VOCABULARY_INDEX);
        StatAdapter adapter = new StatAdapter(getContext(), infos);
        adapter.notifyDataSetChanged();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        recyclerView.setAdapter(adapter);

        TextView maxCard = view.findViewById(R.id.maxCard);
        maxCard.setText(Integer.toString(adapter.getMaxCard()));

        TextView workedDays = view.findViewById(R.id.workedDays);
        workedDays.setText(transportPreferences.getDaysWorkedCount(getContext()) + "");

        TextView emptyDays = view.findViewById(R.id.emptyDays);
        emptyDays.setText(Ruler.getDay(context) - transportPreferences.getStartDay(context)
                - transportPreferences.getDaysWorkedCount(getContext()) + "");

        //TextView learnedCards = view.findViewById(R.id.learnedCards);
        //learnedCards.setText(Integer.toString(0));


        StatisticManager.setDayWorkedView(requireContext(), 0, requireView().findViewById(R.id.firstDay));
        StatisticManager.setDayWorkedView(requireContext(), 1, requireView().findViewById(R.id.secondDay));
        StatisticManager.setDayWorkedView(requireContext(), 2, requireView().findViewById(R.id.thirdDay));
        StatisticManager.setDayWorkedView(requireContext(), 3, requireView().findViewById(R.id.fourthDay));
        StatisticManager.setDayWorkedView(requireContext(), 4, requireView().findViewById(R.id.fifthDay));
        StatisticManager.setDayWorkedView(requireContext(), 5, requireView().findViewById(R.id.sixthDay));
        StatisticManager.setDayWorkedView(requireContext(), 6, requireView().findViewById(R.id.seventhDay));

        ArrayList<Integer> mistakes = StatisticManager.getDifferenceByWeek(requireContext(), StatisticManager.StatType.mistakes);
        ArrayList<Integer> repeated = StatisticManager.getDifferenceByWeek(requireContext(), StatisticManager.StatType.repeated);
        ArrayList<Integer> studied = StatisticManager.getDifferenceByWeek(requireContext(), StatisticManager.StatType.studied);
        if (mistakes.get(0) == -1) {
            view.findViewById(R.id.mistakes).setBackground(getContext().getResources().getDrawable(R.drawable.ic_stat_upper));
            ((TextView)view.findViewById(R.id.mistakesState)).setText("Совершено на " + mistakes.get(1) + "% меньше ошибок, чем в прошлую неделю");
        } else if (mistakes.get(0) == 0){
            view.findViewById(R.id.mistakes).setBackground(getContext().getResources().getDrawable(R.drawable.ic_stat_normal));
            ((TextView)view.findViewById(R.id.mistakesState)).setText("Совершено столько же ошибок, как в прошлую неделю");

        }else if (mistakes.get(0) == 1){
            view.findViewById(R.id.mistakes).setBackground(getContext().getResources().getDrawable(R.drawable.ic_stat_lower));
            ((TextView)view.findViewById(R.id.mistakesState)).setText("Совершено на " + mistakes.get(1) + "% больше ошибок, чем в прошлую неделю");
        }else{
            view.findViewById(R.id.mistakes).setBackground(getContext().getResources().getDrawable(R.drawable.ic_stat_lower));
            ((TextView)view.findViewById(R.id.mistakesState)).setText("Совершено больше ошибок, чем в прошлую неделю");
        }


        if (repeated.get(0) == -1) {
            view.findViewById(R.id.repeated).setBackground(getContext().getResources().getDrawable(R.drawable.ic_stat_lower));
            ((TextView)view.findViewById(R.id.repeatedState)).setText("Повторено на " + repeated.get(1) + "% меньше информации, чем в прошлую неделю");
        } else if (repeated.get(0) == 0){
            view.findViewById(R.id.repeated).setBackground(getContext().getResources().getDrawable(R.drawable.ic_stat_normal));
            ((TextView)view.findViewById(R.id.repeatedState)).setText("Повторено столько же информации, как в прошлую неделю");

        }else if (repeated.get(0) == 1){
            view.findViewById(R.id.repeated).setBackground(getContext().getResources().getDrawable(R.drawable.ic_stat_upper));
            ((TextView)view.findViewById(R.id.repeatedState)).setText("Повторено на " + repeated.get(1) + "% больше информации, чем в прошлую неделю");
        }else{
            view.findViewById(R.id.repeated).setBackground(getContext().getResources().getDrawable(R.drawable.ic_stat_upper));
            ((TextView)view.findViewById(R.id.repeatedState)).setText("Повторено больше информации, чем в прошлую неделю");
        }


        if (studied.get(0) == -1) {
            view.findViewById(R.id.studied).setBackground(getContext().getResources().getDrawable(R.drawable.ic_stat_lower));
            ((TextView)view.findViewById(R.id.studiedState)).setText("Изучено на " + studied.get(1) + "% меньше информации, чем в прошлую неделю");
        } else if (studied.get(0) == 0){
            view.findViewById(R.id.studied).setBackground(getContext().getResources().getDrawable(R.drawable.ic_stat_normal));
            ((TextView)view.findViewById(R.id.studiedState)).setText("Изучено столько же информации, как в прошлую неделю");

        }else if (studied.get(0) == 1){
            view.findViewById(R.id.studied).setBackground(getContext().getResources().getDrawable(R.drawable.ic_stat_upper));
            ((TextView)view.findViewById(R.id.studiedState)).setText("Изучено на " + studied.get(1) + "% больше информации, чем в прошлую неделю");
        }else{
            view.findViewById(R.id.studied).setBackground(getContext().getResources().getDrawable(R.drawable.ic_stat_upper));
            ((TextView)view.findViewById(R.id.studiedState)).setText("Изучено больше информации, чем в прошлую неделю");
        }

    }

    @Override
    public String getTitle() {
        return "Статистика";
    }
}
