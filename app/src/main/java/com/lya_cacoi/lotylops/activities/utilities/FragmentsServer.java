package com.lya_cacoi.lotylops.activities.utilities;

import android.widget.TextView;

import com.lya_cacoi.lotylops.R;

/** Выдача фрагментам одинаковых по структуре атрибутов*/
public class FragmentsServer {
    private static CommonFragment fragment;

    public static void setFr(CommonFragment Fragment){
        FragmentsServer.fragment = Fragment;
    }

    public static void setTitle(String title){
        if (fragment == null || FragmentsServer.fragment.getView() == null) return;
        TextView view = FragmentsServer.fragment.getView().findViewById(R.id.fragment_title);
        if (view != null) view.setText(title);
    }
}
