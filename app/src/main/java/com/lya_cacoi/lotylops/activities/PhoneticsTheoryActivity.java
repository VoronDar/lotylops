package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.ApproachManager.ApproachManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils;
import com.lya_cacoi.lotylops.activities.utilities.CardActivity;
import com.lya_cacoi.lotylops.adapters.PhoneticsExampleBlockAdapter;
import com.lya_cacoi.lotylops.adapters.units.ExampleBlock;
import com.lya_cacoi.lotylops.cards.PhoneticsCard;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.activities.mainPlain.multiple;
import static com.lya_cacoi.lotylops.activities.mainPlain.repeatTTS;


public class PhoneticsTheoryActivity extends CardActivity {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_learn_phonetics, container, false);


    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textBlocksList = new ArrayList<>();

        try {

            PhoneticsCard pCard = (PhoneticsCard)abstractCard;
            TextView title = view.findViewById(R.id.theory);
            title.setText(title.getText().toString() + pCard.getTranscription());

            if (transportPreferences.getPhoneticsApproach(context) == ApproachManager.PHONETICS_APPROACH_DEDUCTION) {
                TextView text = view.findViewById(R.id.theory_text);
                String theory = pCard.getTheory().replace("\\n", System.getProperty("line.separator") + " ");
                text.setText(" " + theory);
                if (mainPlain.sizeRatio < mainPlain.triggerRatio) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(mainPlain.sizeWidth / 7, 0, mainPlain.sizeWidth / 7, 0);
                    text.setLayoutParams(params);
                    text.setTextSize(mainPlain.sizeHeight / (40 * multiple));
                    ((TextView) view.findViewById(R.id.theory)).setTextSize(mainPlain.sizeHeight / (25 * multiple));
                    params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, mainPlain.sizeHeight / 12, 0, mainPlain.sizeHeight / 12);
                    params.gravity = Gravity.CENTER;
                    (view.findViewById(R.id.theory)).setLayoutParams(params);
                    ((TextView) view.findViewById(R.id.theory)).setGravity(View.TEXT_ALIGNMENT_CENTER);
                }
            } else{
                view.findViewById(R.id.theory_text).setVisibility(View.GONE);
            }


            recyclerView = view.findViewById(R.id.recyclerView);
            final ArrayList<ExampleBlock> examples = new ArrayList<>();

            if (pCard.getWordExamples() != null && pCard.getWordExamples().size() > 0) {
                for (String example : pCard.getWordExamples())
                    examples.add(new ExampleBlock(example));
                PhoneticsExampleBlockAdapter adapter = new PhoneticsExampleBlockAdapter(getContext(), examples);
                adapter.setBlockListener(new PhoneticsExampleBlockAdapter.BlockListener() {
                    @Override
                    public void onClick(int position) {
                        repeatTTS.speak(examples.get(position).getText(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
                adapter.notifyDataSetChanged();
                if (mainPlain.sizeRatio < mainPlain.triggerRatio)
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
                else
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
                //recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
                recyclerView.setAdapter(adapter);
                recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            }

        }
        catch(Error e){
            Log.i("main", (abstractCard).toString());
            ActivitiesUtils.createErrorWindow(mainPlain.activity, new ActivitiesUtils.DoWhileError() {
                        @Override
                        public void action() {
                            onRemember(null);
                        }
                    }, abstractCard.getId(),
                    getResources().getString(R.string.unknown_error));
        }
    }
}
