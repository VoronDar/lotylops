package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.adapters.units.PracticeTestsUnit;
import com.lya_cacoi.lotylops.ruler.Ruler;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.activities.mainPlain.dp;

public class PracticeTypeAdapter extends RecyclerView.Adapter<PracticeTypeAdapter.ViewHolder>{

    private final ArrayList<PracticeTestsUnit> units;
    private BlockListener blockListener;
    private View view;
    private boolean clickable;
    private final boolean isLinear;

    public PracticeTypeAdapter(ArrayList<PracticeTestsUnit> blocks, boolean isLinear) {
        this.units = blocks;
        this.isLinear = isLinear;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public interface BlockListener {
        void onClick(int position);

    }


    public void setBlockListener(BlockListener block_listener) {
        this.blockListener = block_listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.select_type__pool_block, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PracticeTestsUnit unit = units.get(position);
        holder.cards.setText(unit.getCardCount() + " " + Ruler.getCardEnd(unit.getCardCount()));
        holder.name.setText(unit.getChapter());
        if (mainPlain.sizeRatio<mainPlain.triggerRatio){
            holder.name.setTextSize(mainPlain.sizeHeight/(25f*mainPlain.multiple));
            holder.cards.setTextSize(mainPlain.sizeHeight/(40f*mainPlain.multiple));
        }
        holder.image.setImageDrawable(view.getResources().getDrawable(unit.getImageResourse()));



    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView cards;
        private final TextView name;
        private final ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            cards = itemView.findViewById(R.id.cardsCount);
            name = itemView.findViewById(R.id.command);
            image = itemView.findViewById(R.id.image);


            if (!isLinear){
                name.setTextSize(dp(15));
                cards.setTextSize(dp(15));
            }

            itemView.setOnClickListener(v -> {
                if (blockListener != null)
                    blockListener.onClick(getAdapterPosition());
            });
        }
    }
}
