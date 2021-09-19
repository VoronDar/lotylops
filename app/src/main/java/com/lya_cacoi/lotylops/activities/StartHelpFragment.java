package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

public class StartHelpFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.start_help, container, false);
    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.buttonYes).setOnClickListener(v -> (
                (startAppActivity) requireActivity()).slideFragment(new StartSettingsFragment()));

        view.findViewById(R.id.buttonNo).setOnClickListener(v ->
                ((startAppActivity) requireActivity()).moveToMain());

    }

}

