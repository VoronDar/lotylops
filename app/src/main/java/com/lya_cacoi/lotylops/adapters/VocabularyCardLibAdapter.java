package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.adapters.units.VocabularyCardLibUnit;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;

public class VocabularyCardLibAdapter extends RecyclerView.Adapter<VocabularyCardLibAdapter.VocabularyCardLibViewHolder>{

    private final ArrayList<VocabularyCardLibUnit> units;
    private BlockListener blockListener;
    private final Context context;
    private final int sectionId;

    public VocabularyCardLibAdapter(Context context, ArrayList<VocabularyCardLibUnit> blocks, int sectionId) {
        this.units = blocks;
        this.context = context;
        this.sectionId = sectionId;
    }

    public interface BlockListener {
        void onClick(int position);
        void onSendClick(int position);

    }


    public void setBlockListener(BlockListener block_listener) {
        this.blockListener = block_listener;
    }

    @NonNull
    @Override
    public VocabularyCardLibViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_lib_block, viewGroup, false);
        return new VocabularyCardLibViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull VocabularyCardLibViewHolder holder, int position) {
        VocabularyCardLibUnit unit = units.get(position);

        holder.word.setText(unit.getWord());

        if (mainPlain.sizeRatio < mainPlain.triggerRatio){
            holder.word.setTextSize(mainPlain.sizeHeight/(40f* mainPlain.multiple));
            holder.translate.setTextSize(mainPlain.sizeHeight/(50f* mainPlain.multiple));
        }
        holder.translate.setText(unit.getTranslate());
        /*
        if (unit.getLevel()<=0 && sectionId == VOCABULARY_INDEX)
            holder.indicator.setVisibility(View.GONE);
        else if (unit.getLevel() <= 1)
            holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.colorLightGray));
        else if (unit.getLevel()<=4)
            holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.colorWhiteSynonym));
        else if (unit.getLevel()<=6)
            holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.colorWhiteMeaning));
        else
            holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.colorBlack));

         */
        //((TextView)holder.indicator).setTextColor();
        //if (position == 0)
        //    holder.divider.setVisibility(View.GONE);



        //if (unit.isMine())
        //    holder.send.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    class VocabularyCardLibViewHolder extends RecyclerView.ViewHolder {

        private final TextView word;
        private final TextView translate;
        //private final View send;

        public VocabularyCardLibViewHolder(@NonNull View itemView) {
            super(itemView);

            word = itemView.findViewById(R.id.word);
            translate = itemView.findViewById(R.id.translate);
            //send = itemView.findViewById(R.id.send);

            itemView.setOnClickListener(v -> {
                if (blockListener != null)
                    blockListener.onClick(getAdapterPosition());
            });

            /*
            send.setOnClickListener(v -> {
                if (blockListener != null)
                    blockListener.onSendClick(getAdapterPosition());
            });

             */
        }
    }
}
