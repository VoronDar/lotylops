package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;

import java.util.ArrayList;

public class CourseSelectAdapter extends RecyclerView.Adapter<CourseSelectAdapter.ButtonLetterViewHolder>{

    private final ArrayList<Item> units;
    private BlockListener blockListener;

    public CourseSelectAdapter(ArrayList<Item> blocks) {
        this.units = blocks;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.courses_select_unit, viewGroup, false);
        return new ButtonLetterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ButtonLetterViewHolder holder, final int position) {
        holder.check.setText(units.get(position).string);

        holder.check.setOnClickListener(v -> units.get(position).setSelected(!units.get(position).isSelected()));
    }


    @Override
    public int getItemCount() {
        return units.size();
    }

    class ButtonLetterViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox check;

        public ButtonLetterViewHolder(@NonNull View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.check);

            itemView.setOnClickListener(v -> {
                if (blockListener != null)
                    blockListener.onClick(getAdapterPosition());
            });
        }
    }


    public static class Item{
        public final String string;
        private boolean selected;

        public Item(String string) {
            this.string = string;
            selected = true;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

}
