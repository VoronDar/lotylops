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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo.Course;
import com.lya_cacoi.lotylops.Databases.transportSQL.TransportSQLCourse;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.adapters.CourseLibAdapter;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;
import static com.lya_cacoi.lotylops.activities.mainPlain.smallWidth;

public class PoolCourseFragment extends CommonFragment {

    private ArrayList<Course> units;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cources, container, false);

    }



    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TransportSQLCourse sqlCourse = TransportSQLCourse.getInstance(view.getContext(), VOCABULARY_INDEX);
        sqlCourse.openDb();


        //// SET WIDGET SIZE////////////////////////////////////////////////////////////////////////
        //if (mainPlain.sizeRatio < mainPlain.triggerRatio)
        //    ((TextView) view.findViewById(R.id.cards)).setTextSize(mainPlain.sizeHeight / 45f);
        ////////////////////////////////////////////////////////////////////////////////////////////




        //// FILL COURSES //////////////////////////////////////////////////////////////////////////
        units = sqlCourse.getAllActivated();
        ////////////////////////////////////////////////////////////////////////////////////////////



        //// PREPARE VIEW //////////////////////////////////////////////////////////////////////////
        CourseLibAdapter adapter = new CourseLibAdapter(view.getContext(), units);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        if ((mainPlain.sizeRatio >= mainPlain.triggerRatio) || mainPlain.sizeWidth < smallWidth)
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        else
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter.notifyDataSetChanged();
        ////////////////////////////////////////////////////////////////////////////////////////////



        //// SET ACTIONS ///////////////////////////////////////////////////////////////////////////
        adapter.setBlockListener(position -> mainPlain.activity.slideFragment(
                new PracticeSelectPoolFragment(units.get(position).getId())));
        ////////////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    public String getTitle() {
        return "Повторение";
    }
}
