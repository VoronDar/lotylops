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
import com.lya_cacoi.lotylops.adapters.units.GrammarEnterTest;
import com.lya_cacoi.lotylops.adapters.units.TestUnit;

import java.util.ArrayList;

public class GrammarEnterTestAdapter extends RecyclerView.Adapter<GrammarEnterTestAdapter.ButtonLetterViewHolder>{

    private final ArrayList<TestUnit> units;
    private BlockListener blockListener;
    private final Context context;
    private Boolean isClickable = false;

    public GrammarEnterTestAdapter(Context context, ArrayList<TestUnit> blocks) {
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grammar_enter_test, viewGroup, false);
        return new ButtonLetterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ButtonLetterViewHolder holder, final int position) {
        final GrammarEnterTest unit = (GrammarEnterTest) units.get(position);
        holder.question.setText(unit.getQuestion());
        holder.firstAnswer.setText(unit.getFirstAnswer());
        holder.secondAnswer.setText(unit.getSecondAnswer());
        if (unit.getPressed() != 1) {
            holder.firstAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_outlined_background));
            holder.firstAnswer.setTextColor(context.getResources().getColor(R.color.outlinedButtonColor));
        }
        else {
            holder.firstAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_rounded_background));
            holder.firstAnswer.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }
        if (unit.getPressed() != 2){
            holder.secondAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_outlined_background));
            holder.secondAnswer.setTextColor(context.getResources().getColor(R.color.outlinedButtonColor));
        }
        else{

            holder.secondAnswer.setBackground(context.getResources().getDrawable(R.drawable.button_rounded_background));
            holder.secondAnswer.setTextColor(context.getResources().getColor(R.color.colorWhite));
        }

        holder.firstAnswer.setOnClickListener(v -> {
            if (!isClickable) return;
            if (unit.getPressed() == 1) unit.setPressed(0);
            else unit.setPressed(1);

            notifyItemChanged(position);
        });
        holder.secondAnswer.setOnClickListener(v -> {
            if (!isClickable) return;
            if (unit.getPressed() == 2) unit.setPressed(0);
            else unit.setPressed(2);

            notifyItemChanged(position);
        });

    }


    public void setClickable(boolean isClickable){
        this.isClickable = isClickable;
    }


    @Override
    public int getItemCount() {
        return units.size();
    }

    class ButtonLetterViewHolder extends RecyclerView.ViewHolder {

        private final TextView question;
        private final TextView firstAnswer;
        private final TextView secondAnswer;

        public ButtonLetterViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            firstAnswer = itemView.findViewById(R.id.firstAnswer);
            secondAnswer = itemView.findViewById(R.id.secondAnswer);

            itemView.setOnClickListener(v -> {
                if (blockListener != null)
                    blockListener.onClick(getAdapterPosition());
            });
        }
    }
}
