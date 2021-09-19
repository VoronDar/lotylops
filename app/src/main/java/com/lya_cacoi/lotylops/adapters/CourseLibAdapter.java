package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo.Course;
import com.lya_cacoi.lotylops.R;

import java.util.ArrayList;

public class CourseLibAdapter extends RecyclerView.Adapter<CourseLibAdapter.CourseLibViewHolder>{

    private final ArrayList<Course> units;
    private BlockListener blockListener;
    private final Context context;

    public CourseLibAdapter(Context context, ArrayList<Course> blocks) {
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
    public CourseLibViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.course_lib_block, viewGroup, false);
        return new CourseLibViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CourseLibViewHolder holder, int position) {
        Course unit = units.get(position);

        holder.id.setText(unit.getName());


        if (unit.isEnabled()) {
            holder.indicator.setBackground(context.getResources().getDrawable(R.drawable.ic_remove));
        }
        else if (unit.isHasAccess()){
            holder.indicator.setBackground(context.getResources().getDrawable(R.drawable.ic_add));
            }
        else if (!unit.isAccessibility()){
            holder.indicator.setBackground(context.getResources().getDrawable(R.drawable.ic_download));
                }
    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    class CourseLibViewHolder extends RecyclerView.ViewHolder {

        private final TextView id;
        private final View indicator;

        public CourseLibViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.course_name);
            indicator = itemView.findViewById(R.id.state);

            itemView.setOnClickListener(v -> {
                if (blockListener != null)
                    blockListener.onClick(getAdapterPosition());
            });
        }
    }
}
