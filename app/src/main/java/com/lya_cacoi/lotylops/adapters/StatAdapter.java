package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.Statistics.OneDayInfo;

import java.util.List;

public class StatAdapter extends RecyclerView.Adapter<StatAdapter.ViewHolder>{

    private List<OneDayInfo> units;
    private Context context;
    private View view;
    private int maxCard = -1;

    public StatAdapter(Context context, List<OneDayInfo> blocks) {
        this.units = blocks;
        this.context = context;
    }

    public int getMaxCard(){
        if (maxCard != -1) return maxCard;
        else{
            for (OneDayInfo o: units){
                int now = o.getForget_count() + o.getRemember_count() + o.getStudied_count();
                if (now > maxCard) maxCard = now;
            } return  maxCard;
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stat_block, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.layout.setWeightSum(getMaxCard());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, units.get(position).getRemember_count());
        holder.remember.setLayoutParams(p);
        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, units.get(position).getForget_count());
        holder.forget.setLayoutParams(p);
        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, units.get(position).getStudied_count());
        holder.studied.setLayoutParams(p);
        p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, getMaxCard() - units.get(position).getStudied_count()  - units.get(position).getForget_count()  - units.get(position).getRemember_count() );
        holder.space.setLayoutParams(p);

        holder.pace.setText(Integer.toString(units.get(position).getPace()));
    }

    @Override
    public int getItemCount() {
        return units.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private View space;
        private View remember;
        private View forget;
        private View studied;
        private LinearLayout layout;
        private TextView pace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            space = itemView.findViewById(R.id.space);
            remember = itemView.findViewById(R.id.remembered);
            forget = itemView.findViewById(R.id.forgotten);
            studied = itemView.findViewById(R.id.studied);
            layout = itemView.findViewById(R.id.layout);
            pace = itemView.findViewById(R.id.pace);

        }
    }
}
