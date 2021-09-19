package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.adapters.units.TestUnit;
import com.lya_cacoi.lotylops.adapters.units.buttonLetterUnit;

import java.util.ArrayList;

public class ButtonLetterAdapter extends RecyclerView.Adapter<ButtonLetterAdapter.ButtonLetterViewHolder>{

    private ArrayList<TestUnit> buttonLetterUnits;
    private BlockListener blockListener;
    private final Context context;

    public ButtonLetterAdapter(Context context, ArrayList<TestUnit> blocks) {
        this.buttonLetterUnits = blocks;
        this.context = context;
    }
    public ButtonLetterAdapter(Context context, ArrayList<buttonLetterUnit> blocks, int a) {
        this.buttonLetterUnits = new ArrayList<>();
        buttonLetterUnits.addAll(blocks);
        this.context = context;
    }

    public interface BlockListener {
        void onClick(int position);

    }

    public void setUnits(ArrayList<buttonLetterUnit> blocks){
        buttonLetterUnits.clear();
        buttonLetterUnits = new ArrayList<>();
        this.buttonLetterUnits = new ArrayList<>();
        buttonLetterUnits.addAll(blocks);
    }


    public void setBlockListener(BlockListener block_listener) {
        this.blockListener = block_listener;
    }

    @NonNull
    @Override
    public ButtonLetterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.button_leter, viewGroup, false);
        return new ButtonLetterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ButtonLetterViewHolder holder, int position) {
        buttonLetterUnit unit = (buttonLetterUnit) buttonLetterUnits.get(position);

        if (unit.getLetter().contains(" "))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                holder.letter.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
            }

        holder.letter.setText(unit.getLetter());
        if (unit.isPressed()) {
            holder.letter.setBackground(context.getResources().getDrawable(R.drawable.button_rounded_background));
            holder.letter.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }
        else {
            holder.letter.setBackground(context.getResources().getDrawable(R.drawable.button_outlined_background));
            holder.letter.setTextColor(context.getResources().getColor(R.color.outlinedButtonColor));
        }
    }


    @Override
    public int getItemCount() {
        return buttonLetterUnits.size();
    }

    class ButtonLetterViewHolder extends RecyclerView.ViewHolder {

        private final TextView letter;

        public ButtonLetterViewHolder(@NonNull View itemView) {
            super(itemView);
            letter = itemView.findViewById(R.id.buttonLetter);

            itemView.setOnClickListener(v -> {
                if (blockListener != null)
                    blockListener.onClick(getAdapterPosition());
            });
        }
    }
}
