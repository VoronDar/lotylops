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
import com.lya_cacoi.lotylops.adapters.units.toDoUnit;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.CULTURE_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.EXCEPTION_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;

public class SectionsAdapter extends RecyclerView.Adapter<SectionsAdapter.ViewHolder>{

    private final ArrayList<toDoUnit> units;
    private BlockListener blockListener;
    private final Context context;

    public int selected = -1;
    private int lastSelected = 0;



    public SectionsAdapter(Context context, ArrayList<toDoUnit> blocks) {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.section_block, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        toDoUnit unit = units.get(position);


        switch (unit.getIndex()){
            case VOCABULARY_INDEX:
                holder.icon.setImageResource(R.drawable.ic_vocabulary);
                holder.text.setText(context.getResources().getString(R.string.vocabulary));
                holder.text.setTextColor(context.getResources().getColor(R.color.backTextVoc));
                holder.text.setBackgroundColor(context.getResources().getColor(R.color.backTextBackVoc));
                break;
            case PHRASEOLOGY_INDEX:
                holder.icon.setImageResource(R.drawable.ic_phraseology);
                holder.text.setText(context.getResources().getString(R.string.phraseology));
                holder.text.setTextColor(context.getResources().getColor(R.color.backTextPhr));
                holder.text.setBackgroundColor(context.getResources().getColor(R.color.backTextBackPhr));
                break;
            case GRAMMAR_INDEX:
                holder.icon.setImageResource(R.drawable.ic_grammar);
                holder.text.setText(context.getResources().getString(R.string.grammar));
                holder.text.setTextColor(context.getResources().getColor(R.color.backTextGram));
                holder.text.setBackgroundColor(context.getResources().getColor(R.color.backTextBackGram));
                break;
            case EXCEPTION_INDEX:
                holder.icon.setImageResource(R.drawable.ic_exceptions);
                holder.text.setText(context.getResources().getString(R.string.exceptions));
                holder.text.setTextColor(context.getResources().getColor(R.color.backTextExc));
                holder.text.setBackgroundColor(context.getResources().getColor(R.color.backTextBackExc));
                break;
            case PHONETIC_INDEX:
                holder.icon.setImageResource(R.drawable.ic_phonetis);
                holder.text.setText(context.getResources().getString(R.string.phonetics));
                holder.text.setTextColor(context.getResources().getColor(R.color.backTextPhon));
                holder.text.setBackgroundColor(context.getResources().getColor(R.color.backTextBackPhon));
                break;
            case CULTURE_INDEX:
                holder.icon.setImageResource(R.drawable.ic_culture);
                holder.text.setText(context.getResources().getString(R.string.culture));
                holder.text.setTextColor(context.getResources().getColor(R.color.backTextCul));
                holder.text.setBackgroundColor(context.getResources().getColor(R.color.backTextBackCul));
                break;

        }
    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView icon;
        public final TextView text;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            text = itemView.findViewById(R.id.name);


            itemView.setOnClickListener(v -> {
                if (blockListener != null)
                    lastSelected = selected;
                    selected = getAdapterPosition();
                    if (lastSelected != -1)
                        notifyItemChanged(lastSelected);
                    notifyItemChanged(selected);
                    blockListener.onClick(getAdapterPosition());
            });
        }
    }
}
