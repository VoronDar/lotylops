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

public class AddLinkedAdapter extends RecyclerView.Adapter<AddLinkedAdapter.ButtonLetterViewHolder>{

    private ArrayList<Course> units;
    private BlockListener blockListener;
    private Context context;

    public AddLinkedAdapter(Context context, ArrayList<Course> blocks) {
        this.units = blocks;
        this.context = context;
    }
    public interface BlockListener {
        public void onClick(int position);

    }


    public void setBlockListener(BlockListener block_listener) {
        this.blockListener = block_listener;
    }

    @NonNull
    @Override
    public ButtonLetterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_linked, viewGroup, false);
        return new ButtonLetterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ButtonLetterViewHolder holder, final int position) {
        holder.text.setText("прохождение курса " + units.get(position).getName() + " может помочь допускать меньше ошибок. Изучить его?");
    }


    @Override
    public int getItemCount() {
        return units.size();
    }

    class ButtonLetterViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        public ButtonLetterViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.add_linked);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (blockListener != null)
                        blockListener.onClick(getAdapterPosition());
                }});
        }
    }


}
