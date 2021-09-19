package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.adapters.units.TextBlock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class TextBlockAdapter extends RecyclerView.Adapter<TextBlockAdapter.TextBlockViewHolder>{

    private ArrayList<TextBlock> textBlocks;
    private BlockListener blockListener;
    private Context context;

    public TextBlockAdapter(Context context, ArrayList<TextBlock> blocks) {
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.textblock, viewGroup, false);
        return new TextBlockViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TextBlockViewHolder holder, int position) {
        TextBlock text_block = textBlocks.get(position);

        if (text_block.getImageId() != null) {
        /*
            String filename = text_block.getImageId();
            InputStream inputStream = null;
            try {
                inputStream = context.getAssets().open(textBlocks.get(position).getImageId());
                Drawable d = Drawable.createFromStream(inputStream, null);
                holder.image.setImageDrawable(d);
                holder.image.setScaleType(ImageView.ScaleType.FIT_XY);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null)
                        inputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
         */

            try {
                Uri file = Uri.fromFile(new File(context.getCacheDir().getPath() +
                        "/" + text_block.getImageId() + ".jpeg"));
                if (file == null)
                    throw new FileNotFoundException();
                InputStream imageStream;
                imageStream = context.getContentResolver().openInputStream(file);
                holder.image.setImageBitmap(BitmapFactory.decodeStream(imageStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                holder.image.setVisibility(View.GONE);
            }
        }


        else {
            holder.image.setVisibility(View.GONE);
        }
        if (text_block.getText() == null){
            holder.text.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
        }
        else{
            holder.text.setText(text_block.getText());
            if (mainPlain.sizeRatio < mainPlain.triggerRatio) {
                if (mainPlain.sizeRatio > mainPlain.triggerMidRatio)
                 holder.text.setTextSize(text_block.getTextSize() * (1.5f * mainPlain.multiple));
                else
                    holder.text.setTextSize(text_block.getTextSize() * (1.1f * mainPlain.multiple));
            }
            else
                holder.text.setTextSize(text_block.getTextSize());
            holder.text.setTextColor(text_block.getColorID());
            if (!text_block.isNeedDivider()) {
                holder.divider.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return textBlocks.size();
    }

    class TextBlockViewHolder extends RecyclerView.ViewHolder {

        private TextView text;
        private ImageView divider;
        private ImageView image;

        public TextBlockViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.textBlockText);
            divider = itemView.findViewById(R.id.imageView);
            image = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (blockListener != null)
                        blockListener.onClick(getAdapterPosition());
                }});
        }
    }
}
