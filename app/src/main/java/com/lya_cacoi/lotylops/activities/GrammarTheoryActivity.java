package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.style.ReplacementSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils;
import com.lya_cacoi.lotylops.activities.utilities.CardActivity;
import com.lya_cacoi.lotylops.cards.TheoryHolder;

import java.util.ArrayList;

import static android.widget.Adapter.NO_SELECTION;
import static com.lya_cacoi.lotylops.activities.mainPlain.multiple;


public class GrammarTheoryActivity extends CardActivity {

    int selectionBegin = 3;
    int selectionEnd = 20;
    float tracking;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_learn_grammar, container, false);


    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textBlocksList = new ArrayList<>();

        try {
            TheoryHolder card = (TheoryHolder) abstractCard;

            TextView text = view.findViewById(R.id.theory_text);
            //tracking = text.getLetterSpacing();

            //int NO_SELECTION = -1;

            //ReplacementSpan selectionTrackingSpan = new SelectionTrackingSpan();
            /*
            SpannableString s = new SpannableString(card.getTheory());
            s.setSpan(selectionTrackingSpan, selectionBegin, selectionEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setText(s);

             */

                String theory = card.getTheory().replace("\\n", System.getProperty("line.separator") + " ");
                text.setText(" " + theory);

            if (mainPlain.sizeRatio < mainPlain.triggerRatio){
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(mainPlain.sizeWidth/7, 0, mainPlain.sizeWidth/7, 0);
                text.setLayoutParams(params);
                text.setTextSize(mainPlain.sizeHeight/(40*multiple));
                ((TextView)view.findViewById(R.id.theory)).setTextSize(mainPlain.sizeHeight/(25*multiple));
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, mainPlain.sizeHeight/12, 0, mainPlain.sizeHeight/12);
                params.gravity = Gravity.CENTER;
                (view.findViewById(R.id.theory)).setLayoutParams(params);
                ((TextView)view.findViewById(R.id.theory)).setGravity(View.TEXT_ALIGNMENT_CENTER);
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

    private class SelectionTrackingSpan extends ReplacementSpan {
        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            // размер будет достаточный для того чтобы нарисовать буквы + расстояния между ними
            return (int)(paint.measureText(text, start, end) + tracking * (end - start));
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            float dx = x;
            for (int i = start; i < end; i++) {
                // если символ не попадает в выделенную часть, будем рисовать его просто черным
                if (i < selectionBegin || i >= (selectionEnd != NO_SELECTION ? selectionEnd + 1 : selectionBegin + 1)) paint.setColor(Color.BLACK);
                canvas.drawText(text, i, i + 1, dx, y, paint);
                dx += paint.measureText(text, i, i + 1) + tracking;
            }
        }
    }
}
