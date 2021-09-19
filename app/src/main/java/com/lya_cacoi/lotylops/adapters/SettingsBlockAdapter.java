package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.adapters.units.SettingUnit;

import java.util.ArrayList;

import static android.view.Gravity.CENTER;

public class SettingsBlockAdapter extends RecyclerView.Adapter<SettingsBlockAdapter.ViewHolder> implements BaseSettingsAdapter{

    private final ArrayList<SettingUnit> units;
    private SettingsAdapter.BlockListener blockListener;
    private final Context context;
    private boolean ckickable;

    public int selected = -1;
    private int lastSelected = 0;



    public SettingsBlockAdapter(Context context, ArrayList<SettingUnit> blocks) {
        this.units = blocks;
        this.context = context;
    }

    public boolean isClickable() {
        return ckickable;
    }

    public void setClickable(boolean ckickable) {
        this.ckickable = ckickable;
    }


    public void setBlockListener(SettingsAdapter.BlockListener block_listener) {
        this.blockListener = block_listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.settings_select_block, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SettingUnit unit = units.get(position);
        holder.icon.setImageResource(unit.getImageID());
        holder.text.setText(unit.getCommand());
        if (unit.isChecked()){
            holder.indicator.setVisibility(View.VISIBLE);
        }
        else{
            holder.indicator.setVisibility(View.GONE);
        }
        /*
        if (selected == position)
            holder.cardView.setAlpha(0.8f);
        else
            holder.cardView.setAlpha(1);

         */
        //animateClick(holder.cardView, (holder.cardView.getAlpha()< 0.9));
    }


    private void animateClick(final View view, final boolean isPress){
        Animation a = new AlphaAnimation(((isPress)?0.7f:1), ((isPress)?1:0.7f));
        a.setDuration(100);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setAlpha(((isPress)?1f:0.7f));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(a);
    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView text;
        public View indicator;
        public CardView cardView;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.image);
            text = itemView.findViewById(R.id.command);
            indicator = itemView.findViewById(R.id.indicator);
            cardView = itemView.findViewById(R.id.cardView);


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
