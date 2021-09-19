package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.mainPlain;

public class winSeriesAdapter extends RecyclerView.Adapter<winSeriesAdapter.ButtonLetterViewHolder>{

    private int max_win = 0;
    private Context context;

    public winSeriesAdapter(int max_win, Context context) {
        this.max_win = max_win;
        this.context = context;
    }

    @NonNull
    @Override
    public ButtonLetterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.game_series_block, viewGroup, false);
        return new ButtonLetterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ButtonLetterViewHolder holder, int position) {
        if (position < max_win)
            holder.view.setImageDrawable(context.getResources().getDrawable(R.drawable.gamecoin));
        else
            holder.view.setImageDrawable(context.getResources().getDrawable(R.drawable.gamecoinnone));
    }


    @Override
    public int getItemCount() {
        return 5;
    }

    class ButtonLetterViewHolder extends RecyclerView.ViewHolder {
        private ImageView view;
        public ButtonLetterViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView.findViewById(R.id.block);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mainPlain.sizeWidth/7, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
        }
    }
}
