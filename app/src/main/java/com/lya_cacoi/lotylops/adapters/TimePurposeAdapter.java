package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;

import java.util.ArrayList;

public class TimePurposeAdapter extends RecyclerView.Adapter<TimePurposeAdapter.ButtonLetterViewHolder>{

    private final ArrayList<Item> units;
    private BlockListener blockListener;
    private final Context context;

    public int lastPressedIndex = -1;
    public int nowPressedIndex = -1;

    public TimePurposeAdapter(Context context, ArrayList<Item> blocks) {
        this.units = blocks;
        this.context = context;
    }
    public interface BlockListener {
        void onClick(int position);

    }


    public void setBlockListener(BlockListener block_listener) {
        this.blockListener = block_listener;
    }

    @NonNull
    @Override
    public ButtonLetterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.day_plan_time_unit, viewGroup, false);
        return new ButtonLetterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ButtonLetterViewHolder holder, int position) {
        holder.button.setText(units.get(position).string);

        if (nowPressedIndex == position) {
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.buttonBackground));
            holder.button.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }
        else{
            holder.card.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        holder.button.setTextColor(context.getResources().getColor(R.color.colorBlack));}
    }


    @Override
    public int getItemCount() {
        return units.size();
    }

    class ButtonLetterViewHolder extends RecyclerView.ViewHolder {

        private final TextView button;
        private final CardView card;

        public ButtonLetterViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
            card = itemView.findViewById(R.id.card);

            itemView.setOnClickListener(v -> {
                if (blockListener != null)
                    blockListener.onClick(getAdapterPosition());
            });
        }
    }


    public static class Item{
        public final String string;

        public Item(String string) {
            this.string = string;
        }

    }

}
