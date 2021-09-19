package com.lya_cacoi.lotylops.Databases.Union;

import android.content.Context;

import com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo.Course;

import java.util.ArrayList;

public interface FirebaseTransporter {
    void onNoCourcesGet();
    void onAllCourcesGet(ArrayList<Course> courses);
    void onAllGrammarCourcesGet(ArrayList<Course> courses);
    Context getContext();
    void onAllSimpleCourseGet(ArrayList<Course> courses);

    void onVocPhrAllCourcesGet(ArrayList<Course> courses);
}
