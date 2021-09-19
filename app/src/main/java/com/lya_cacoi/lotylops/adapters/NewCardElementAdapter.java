package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.NewCardFragment;
import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.activities.utilities.CardChangable;
import com.lya_cacoi.lotylops.activities.utilities.panel.InfoHoldable;
import com.lya_cacoi.lotylops.activities.utilities.panel.SimpleInfoPannel;
import com.lya_cacoi.lotylops.adapters.units.newCardElementUnit;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.activities.mainPlain.activity;
import static com.lya_cacoi.lotylops.activities.mainPlain.dp;

public class NewCardElementAdapter extends RecyclerView.Adapter<NewCardElementAdapter.TextBlockViewHolder>{

    private final ArrayList<newCardElementUnit> units;
    private BlockListener blockListener;
    private final Context context;
    private boolean isChange;
    private int lastSentenceIndex;
    private final CardChangable parent;

    public NewCardElementAdapter(boolean isChange, Context context, ArrayList<newCardElementUnit> blocks, CardChangable fr) {
        this.units = blocks;
        this.context = context;
        this.isChange = isChange;
        this.parent = fr;

        this.setHasStableIds(true);
    }

    public void setLastSentenceIndex(int lastSentenceIndex) {
        this.lastSentenceIndex = lastSentenceIndex;
    }

    public interface BlockListener {
        void onClick(int position);

    }


    public void setBlockListener(BlockListener block_listener) {
        this.blockListener = block_listener;
    }

    @NonNull
    @Override
    public TextBlockViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.info_card_block, viewGroup, false);
        return new TextBlockViewHolder(view);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final TextBlockViewHolder holder, final int position) {
        final newCardElementUnit block = units.get(position);

        holder.setIsRecyclable(false);
        holder.name.setText(block.getName());


        /*
        holder.itemView.findViewById(R.id.add_block).setVisibility(View.GONE);
        holder.itemView.findViewById(R.id.delSentence).setVisibility(View.GONE);
        holder.itemView.findViewById(R.id.addNew).setVisibility(View.GONE);


        if (block.getId() == NewCardFragment.ElIds.train){
            holder.itemView.findViewById(R.id.add_block).setVisibility(View.VISIBLE);
            if (!block.isRequired()){
                holder.itemView.findViewById(R.id.delSentence).setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.delSentence).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("main - pos", Integer.toString(position));
                        Log.i("main - last", Integer.toString(lastSentenceIndex));
                        lastSentenceIndex--;
                        units.remove(position);
                        //notifyItemRemoved(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });
            }
            if (position == lastSentenceIndex){
                holder.itemView.findViewById(R.id.addNew).setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.addNew).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        units.add(++lastSentenceIndex, new newCardElementUnit(
                                context.getString(R.string.exampleTrainLearn) + " (" + context.getString(R.string.variant) + ")",
                                null, false,
                                NewCardFragment.ElIds.train));

                        //notifyItemInserted(lastSentenceIndex);
                        notifyDataSetChanged();
                    }
                });
            }

        }
         */


        if (block.getValue() == null)
            holder.value.setHint(block.getPrevValue());
        else
            holder.value.setHint("");

        if (block.getId() == NewCardFragment.ElIds.transcript){
            holder.value.setInputType(InputType.TYPE_TEXT_VARIATION_PHONETIC);
        }
        if (block.isRequired()){
            holder.name.setTextColor(context.getResources().getColor(R.color.colorBlue));
        }

        if (!block.isEditable()) {
            holder.value.setFocusable(false);
            holder.value.setClickable(false);
            holder.value.setTextColor(context.getResources().getColor(R.color.colorWhiteExample));
        }

        if (block.getValue() != null){
            holder.value.setText(block.getValue());
        }
        //else if (block.getPrevValue() != null){
        //    block.setValue(block.getPrevValue());
        //    holder.value.setText(block.getValue());
        //}

        if (block.getValue() == null && block.isRequired()){
            holder.name.setTextColor(context.getResources().getColor(R.color.colorOrange));
            holder.value.setHint(block.getPrevValue());
        }
        holder.value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (holder.value.getText().toString().equals(""))
                    block.setValue(null);
                else
                    block.setValue(holder.value.getText().toString());
                if (block.getValue() == null && block.isRequired()){
                    holder.name.setTextColor(context.getResources().getColor(R.color.colorOrange));
                }
                else if (block.isRequired()){
                    holder.name.setTextColor(context.getResources().getColor(R.color.colorBlue));
                }
                if (block.getValue() == null)
                    holder.value.setHint(block.getPrevValue());
                else
                    holder.value.setHint("");

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        holder.value.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                Log.i("main", "set to card " + block);
                parent.setToCard(parent.getCard(), position);
            }
        });

        if (block.getId() == NewCardFragment.ElIds.train ||
                block.getId() == NewCardFragment.ElIds.trainNative ||
                block.getId() == NewCardFragment.ElIds.mem ||
                block.getId() == NewCardFragment.ElIds.help ||
                block.getId() == NewCardFragment.ElIds.group ||
                block.getId() == NewCardFragment.ElIds.part){
            holder.help.setVisibility(View.VISIBLE);
            holder.help.setOnClickListener(v -> {
                if (block.getId() == NewCardFragment.ElIds.train) {
                    SimpleInfoPannel.openPanel((InfoHoldable) activity, "Поле " + block.getName(),
                            context.getResources().getString(R.string.trainInfo), R.drawable.icon_write);
                } else if (block.getId() == NewCardFragment.ElIds.trainNative){
                    SimpleInfoPannel.openPanel((InfoHoldable) activity, "Поле " + block.getName(),
                            context.getResources().getString(R.string.trainNativeInfo), R.drawable.icon_write);
                }
                else if (block.getId() == NewCardFragment.ElIds.help){
                    SimpleInfoPannel.openPanel((InfoHoldable) activity, "Поле " + block.getName(),
                            context.getResources().getString(R.string.helpInfo), R.drawable.icon_help);
                }
                else if (block.getId() == NewCardFragment.ElIds.mem) {
                    SimpleInfoPannel.openPanel((InfoHoldable) activity, "Поле " + block.getName(),
                            context.getResources().getString(R.string.memInfo), R.drawable.icon_mem);
                }
                else if (block.getId() == NewCardFragment.ElIds.group){
                    SimpleInfoPannel.openPanel((InfoHoldable) activity, "Поле " + block.getName(),
                            context.getResources().getString(R.string.groupInfo), R.drawable.icon_group);
                }
                else if (block.getId() == NewCardFragment.ElIds.part) {
                    SimpleInfoPannel.openPanel((InfoHoldable) activity, "Поле " + block.getName(),
                            context.getResources().getString(R.string.partInfo), R.drawable.icon_part);
                }
            });
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    class TextBlockViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final EditText value;
        private final View help;

        public TextBlockViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            value = itemView.findViewById(R.id.value);
            help = itemView.findViewById(R.id.helpView);


            if (mainPlain.sizeRatio < mainPlain.triggerRatio){
                LinearLayout infoCardLayout = itemView.findViewById(R.id.info_card_layout);
                LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(mainPlain.sizeWidth/7, 0, mainPlain.sizeWidth/7, dp(16));
                infoCardLayout.setLayoutParams(params);
                Log.i("main", "width " +mainPlain.sizeWidth);
                name.setTextSize(22);
                value.setTextSize(27);
            }


            itemView.setOnClickListener(v -> {
                if (blockListener != null)
                    blockListener.onClick(getAdapterPosition());
            });
        }
    }
}
