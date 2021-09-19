package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.adapters.units.toDoUnit;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.CULTURE_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.EXCEPTION_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>{

    private final ArrayList<toDoUnit> units;
    private BlockListener blockListener;
    private boolean clickable;
    private final Context context;

    public ToDoAdapter(Context context, ArrayList<toDoUnit> blocks) {
        this.units = blocks;
        this.context = context;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public interface BlockListener {
        void onClick(int position);
        void onLongClick(int position);

    }


    public void setBlockListener(BlockListener block_listener) {
        this.blockListener = block_listener;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.day_plan_block, viewGroup, false);
        return new ToDoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        toDoUnit unit = units.get(position);
        holder.things.setText(unit.getToDo());
        holder.toDoType.setText(unit.getChapterName());

        switch (unit.getIndex()){
            case VOCABULARY_INDEX:
                holder.toDo.setText(context.getResources().getString(R.string.vocabulary));
                holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.backVoc));
                holder.toDo.setTextColor(context.getResources().getColor(R.color.backTextVoc));
                holder.toDo.setBackgroundColor(context.getResources().getColor(R.color.backTextBackVoc));
                holder.toDoType.setTextColor(context.getResources().getColor(R.color.backNameVoc));
                break;
            case PHRASEOLOGY_INDEX:
                holder.toDo.setText(context.getResources().getString(R.string.phraseology));
                holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.backPhr));
                holder.toDo.setTextColor(context.getResources().getColor(R.color.backTextPhr));
                holder.toDo.setBackgroundColor(context.getResources().getColor(R.color.backTextBackPhr));
                holder.toDoType.setTextColor(context.getResources().getColor(R.color.backNamePhr));
                break;
            case GRAMMAR_INDEX:
                holder.toDo.setText(context.getResources().getString(R.string.grammar));
                holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.backGram));
                holder.toDo.setTextColor(context.getResources().getColor(R.color.backTextGram));
                holder.toDo.setBackgroundColor(context.getResources().getColor(R.color.backTextBackGram));
                holder.toDoType.setTextColor(context.getResources().getColor(R.color.backNameGram));
                break;
            case EXCEPTION_INDEX:
                holder.toDo.setText(context.getResources().getString(R.string.exceptions));
                holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.backExc));
                holder.toDo.setTextColor(context.getResources().getColor(R.color.backTextExc));
                holder.toDo.setBackgroundColor(context.getResources().getColor(R.color.backTextBackExc));
                holder.toDoType.setTextColor(context.getResources().getColor(R.color.backNameExc));
                break;
            case PHONETIC_INDEX:
                holder.toDo.setText(context.getResources().getString(R.string.phonetics));
                holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.backPhon));
                holder.toDo.setTextColor(context.getResources().getColor(R.color.backTextPhon));
                holder.toDo.setBackgroundColor(context.getResources().getColor(R.color.backTextBackPhon));
                holder.toDoType.setTextColor(context.getResources().getColor(R.color.backNamePhon));
                break;
            case CULTURE_INDEX:
                holder.toDo.setText(context.getResources().getString(R.string.culture));
                holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.backCul));
                holder.toDo.setTextColor(context.getResources().getColor(R.color.backTextCul));
                holder.toDo.setBackgroundColor(context.getResources().getColor(R.color.backTextBackCul));
                holder.toDoType.setTextColor(context.getResources().getColor(R.color.backNameCul));
                break;
        }
        //holder.toDoNumber.setText(Integer.toString(unit.getCardCount()));


    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder {

        /** повторение или изучение */
        private final TextView toDoType;
        /** слова, фразы, грамматика и тд */
        private final TextView toDo;
        private final MaterialCardView card;
        /** краткий обзор того, что будет сегодня */
        private final TextView things;
        private TextView toDoNumber;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);

            toDo = itemView.findViewById(R.id.name);
            //toDoNumber = itemView.findViewById(R.id.toDoNumber);
            toDoType = itemView.findViewById(R.id.sence);
            card = itemView.findViewById(R.id.card);
            things = itemView.findViewById(R.id.things);


            itemView.setOnClickListener(v -> {
                if (blockListener != null)
                    blockListener.onClick(getAdapterPosition());
            });
            itemView.setOnLongClickListener(v -> {
                if (blockListener != null)
                    blockListener.onLongClick(getAdapterPosition());
                return true;
            });
        }
    }
}
