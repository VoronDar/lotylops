package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.Achieve.Achieve;
import com.lya_cacoi.lotylops.Achieve.AchieveManager;
import com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo.Course;
import com.lya_cacoi.lotylops.R;

import java.util.ArrayList;

public class AchieveAdapter extends RecyclerView.Adapter<AchieveAdapter.viewHolder>{

    private final ArrayList<Achieve> units;
    private BlockListener blockListener;
    private final Context context;

    public AchieveAdapter(Context context, ArrayList<Achieve> blocks) {
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
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.achieve_block, viewGroup, false);
        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Achieve unit = units.get(position);
        holder.block.setBackground(context.getResources().getDrawable(AchieveManager.getImage(unit.id)));
        if (!unit.achieved)
            holder.block.setAlpha(0.25f);
        else
            holder.block.setAlpha(1f);
    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {
        private final View block;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            block = itemView.findViewById(R.id.block);

            itemView.setOnClickListener(v -> {
                if (blockListener != null)
                    blockListener.onClick(getAdapterPosition());
            });
        }
    }
}
