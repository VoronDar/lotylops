package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo.Course;
import com.lya_cacoi.lotylops.Helper.Helper;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.Backable;
import com.lya_cacoi.lotylops.activities.utilities.Blockable;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.activities.utilities.FragmentsServer;
import com.lya_cacoi.lotylops.adapters.SectionsAdapter;
import com.lya_cacoi.lotylops.adapters.units.toDoUnit;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.CULTURE_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.EXCEPTION_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;

public class SelectSectionFragment extends CommonFragment implements Blockable {

    private SectionsAdapter adapter;
    private ArrayList<toDoUnit> units;
    private static FragmentManager fragmentManager;
    private static FragmentTransaction ft;

    private static SelectSectionFragment me;

    private int selectedIndex = 0;
    private Course selectedCourse;

    private boolean wasCreated = false;
    static boolean setEnable = true;

    public SelectSectionFragment(){
    }

    public SelectSectionFragment(int index, Course course) {
     selectedIndex = index;
     selectedCourse = course;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        me = this;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sections, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getContext();

        Helper.showHelp(requireContext(), Helper.Help.library);

        FragmentsServer.setTitle(getTitle());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        if (recyclerView != null)
            recyclerView.setHasFixedSize(true);
        units = new ArrayList<>();
        adapter = new SectionsAdapter(view.getContext(), units);

        units.add(new toDoUnit(false, null, null, VOCABULARY_INDEX, 0));
        units.add(new toDoUnit(false, null, null, PHRASEOLOGY_INDEX, 0));
        units.add(new toDoUnit(false, null, null, GRAMMAR_INDEX, 0));
        units.add(new toDoUnit(false, null, null, EXCEPTION_INDEX, 0));
        units.add(new toDoUnit(false, null, null, PHONETIC_INDEX, 0));
        units.add(new toDoUnit(false, null, null, CULTURE_INDEX, 0));

        /*
        if (mainPlain.sizeRatio < mainPlain.triggerRatio) {
            adapter.selected = 0;

            recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2, RecyclerView.VERTICAL, false));
            fragmentManager = mainPlain.activity.getSupportFragmentManager();
            ft = fragmentManager.beginTransaction();
            Fragment fragment;
            if (selectedCourse == null)
                fragment = new LibraryCourseFragment(selectedIndex);
            else{
                fragment = new LibraryFragment(selectedCourse, selectedIndex);
                selectedCourse = null;
                selectedIndex = 0;
            }
            ft.add(R.id.course_place, fragment);
            ft.commit();

        }
        else
         */
        assert recyclerView != null;
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2, RecyclerView.VERTICAL, false));
            adapter.setBlockListener(position -> {
                if (setEnable) {
                    //if (mainPlain.sizeRatio < mainPlain.triggerRatio) {
                    //    slide(new LibraryCourseFragment(position));
                    //} else {
                        ((mainPlain) requireActivity()).slideFragment(new LibraryCourseFragment(units.get(position).getIndex()));

                    //}
                }
        });



            recyclerView.setAdapter(adapter);
            recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            adapter.notifyDataSetChanged();
        }

        void slide(Fragment fr){
            try {
                mainPlain.activity.selectedFr = (Backable) fr;
            } catch (ClassCastException ignored){}
            ft = fragmentManager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
            //ft.replace(R.id.course_place, fr);
            ft.commit();
        }

        public static SelectSectionFragment fr(){
        return me;
        }

    @Override
    public void setEnable(Boolean enable) {
        setEnable = enable;
    }

    @Override
    public String getTitle() {
        if (getContext() == null) return "Разделы";
        return requireContext().getResources().getString(R.string.fragment_name_sections);
    }
}

