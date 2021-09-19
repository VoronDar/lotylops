package com.lya_cacoi.lotylops.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.Game.GameManager;
import com.lya_cacoi.lotylops.R;

import java.util.ArrayList;

public class GameLogAdapter extends RecyclerView.Adapter<GameLogAdapter.ButtonLetterViewHolder>{

    private ArrayList<GameManager.GameLog> buttonLetterUnits;
    private Context context;

    public GameLogAdapter(Context context, ArrayList<GameManager.GameLog> blocks) {
        this.buttonLetterUnits = blocks;
        this.context = context;
    }

    @NonNull
    @Override
    public ButtonLetterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.game_log, viewGroup, false);
        return new ButtonLetterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ButtonLetterViewHolder holder, int position) {
        GameManager.GameLog unit = buttonLetterUnits.get(position);

        holder.question.setText(((unit.answer == null)?"задание на аудирование":unit.answer));
        holder.right.setText(((unit.right.equals("true"))?"правда":(unit.right.equals("false")?"ложь":unit.right)));
        holder.your.setText(((unit.your.equals("true"))?"правда":(unit.your.equals("false")?"ложь":unit.your)));

        if (!unit.isRight){
            //holder.questionPlace.setCardBackgroundColor(context.getResources().getColor(R.color.colorRedApp));
            holder.yourPlace.setCardBackgroundColor(context.getResources().getColor(R.color.colorRedApp));
            holder.right.setTextColor(context.getResources().getColor(R.color.colorRedApp));
        } else{
            //holder.questionPlace.setCardBackgroundColor(context.getResources().getColor(R.color.colorBlueApp));
            holder.yourPlace.setCardBackgroundColor(context.getResources().getColor(R.color.colorBlueApp));
            holder.right.setTextColor(context.getResources().getColor(R.color.colorBlueApp));

        }
        /*
        holder.questionPlace.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        holder.questionPlace.getViewTreeObserver()
                                .removeOnGlobalLayoutListener(this);
                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) holder.yourPlace.getLayoutParams();
                        params.setMargins(0,  holder.questionPlace.getHeight() - dp(30), 0, 0);
                    }
                }
        );4

         */
    }

    @Override
    public int getItemCount() {
        return buttonLetterUnits.size();
    }

    class ButtonLetterViewHolder extends RecyclerView.ViewHolder {

        private TextView question;
        private TextView right;
        private TextView your;
        private CardView yourPlace;
        //private CardView questionPlace;

        public ButtonLetterViewHolder(@NonNull View itemView) {
            super(itemView);
            //questionPlace = itemView.findViewById(R.id.answers_place);
            question = itemView.findViewById(R.id.answer);
            yourPlace = itemView.findViewById(R.id.yourPlace);

            right = itemView.findViewById(R.id.right);
            your = itemView.findViewById(R.id.your);


        }


    }

}
