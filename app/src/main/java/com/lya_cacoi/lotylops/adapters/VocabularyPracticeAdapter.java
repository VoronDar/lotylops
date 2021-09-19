package com.lya_cacoi.lotylops.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.adapters.units.VocabularyPracticeUnit;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.declaration.consts.SELECT_ALL_TODAY_CARDS;
import static com.lya_cacoi.lotylops.declaration.consts.SELECT_REPEAT_ALL_COURSE;
import static com.lya_cacoi.lotylops.declaration.consts.SELECT_REPEAT_NEXT_DAY;

public class VocabularyPracticeAdapter extends RecyclerView.Adapter<VocabularyPracticeAdapter.VocabularyPracticeViewHolder>{

    private final ArrayList<VocabularyPracticeUnit> units;
    private BlockListener blockListener;

    public VocabularyPracticeAdapter(ArrayList<VocabularyPracticeUnit> blocks) {
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
    public VocabularyPracticeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.test_pool_block, viewGroup, false);
        return new VocabularyPracticeViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull VocabularyPracticeViewHolder holder, int position) {
        VocabularyPracticeUnit unit = units.get(position);
        holder.command.setText(unit.getCommand());

        switch (unit.getID()){
            case SELECT_REPEAT_NEXT_DAY:
                holder.image.setImageResource(R.drawable.ic_icon_all_day_part);
                break;
            case SELECT_REPEAT_ALL_COURSE:
                holder.image.setImageResource(R.drawable.ic_icon_all_course);
                break;
            case SELECT_ALL_TODAY_CARDS:
                holder.image.setImageResource(R.drawable.ic_icon_all_day);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    class VocabularyPracticeViewHolder extends RecyclerView.ViewHolder {

        private final TextView command;
        private final ImageView image;

        public VocabularyPracticeViewHolder(@NonNull View itemView) {
            super(itemView);

            command = itemView.findViewById(R.id.command);
            image = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(v -> {
                if (blockListener != null)
                    blockListener.onClick(getAdapterPosition());
            });
        }
    }
}
