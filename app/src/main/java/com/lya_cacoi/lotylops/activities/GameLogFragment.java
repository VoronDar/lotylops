package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.Game.GameData;
import com.lya_cacoi.lotylops.Game.GameManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.adapters.GameLogAdapter;

public class GameLogFragment extends CommonFragment {

    private final GameData gameData;


    public GameLogFragment(GameData gameData) {
        this.gameData = gameData;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_log, container, false);

    }



    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //// ЗАКРЫТИЕ КЛАВИАТУРЫ ///////////////////////////////////////////////////////////////////
        View v = requireActivity().getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////


        //// PREPARE VIEW //////////////////////////////////////////////////////////////////////////
        GameLogAdapter adapter = new GameLogAdapter(view.getContext(), GameManager.logs);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter.notifyDataSetChanged();
        ////////////////////////////////////////////////////////////////////////////////////////////


        //// SET SLIDE /////////////////////////////////////////////////////////////////////////////
        view.findViewById(R.id.next).setOnClickListener(a -> {
            GameManager.logs.clear();
            if (getActivity() != null) {
                //if (gameData.getId() != null)
                ((mainPlain) getActivity()).slideFragment(new GamePlayFragment(gameData));
                //else
                //((mainPlain) getActivity()).slideFragment(new GameStartFragment());
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    public String getTitle() {
        return null;
    }
}
