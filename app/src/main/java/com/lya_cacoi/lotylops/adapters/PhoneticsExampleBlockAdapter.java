package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.adapters.units.ExampleBlock;

import java.util.ArrayList;

public class PhoneticsExampleBlockAdapter extends RecyclerView.Adapter<PhoneticsExampleBlockAdapter.TextBlockViewHolder>{

    private ArrayList<ExampleBlock> textBlocks;
    private BlockListener blockListener;
    private Context context;

    public PhoneticsExampleBlockAdapter(Context context, ArrayList<ExampleBlock> blocks) {
        this.textBlocks = blocks;
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
    public TextBlockViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.example_block, viewGroup, false);
        return new TextBlockViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TextBlockViewHolder holder, int position) {
        ExampleBlock text_block = textBlocks.get(position);
        holder.text.setText(text_block.getText());
    }

    @Override
    public int getItemCount() {
        return textBlocks.size();
    }

    class TextBlockViewHolder extends RecyclerView.ViewHolder {

        private TextView text;

        public TextBlockViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.textBlockText);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (blockListener != null)
                        blockListener.onClick(getAdapterPosition());
                }});
        }
    }
}
