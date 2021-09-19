package com.lya_cacoi.lotylops.activities.utilities.panel;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.mainPlain;

import static android.view.View.GONE;
import static com.lya_cacoi.lotylops.activities.utilities.panel.PanelManager.isPanel;

public class SimpleInfoPannel {

    public static void openPanel(InfoHoldable activity, String title, String description, int image){
        if (isPanel){
            closePanel(activity);
            return;
        }
        isPanel = true;
        View panel = activity.getView(R.id.infoPanel);
        Button button = (Button)activity.getView(R.id.infoCardButton);
        panel.setVisibility(View.VISIBLE);

        Animation a = new TranslateAnimation(0, 0,  mainPlain.sizeHeight, 0);
        a.setDuration(200);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                button.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        panel.startAnimation(a);

        ((TextView)activity.getView(R.id.infoCardTitle)).setText(title);
        ((TextView)activity.getView(R.id.infoCardDescription)).setText(description);
        (activity.getView(R.id.infoCardDescription)).setVisibility(View.VISIBLE);

        if (image != -1){
            activity.getView(R.id.infoCardImage).setVisibility(View.VISIBLE);
            ((ImageView)activity.getView(R.id.infoCardImage)).setImageResource(image);
        } else{
            activity.getView(R.id.infoCardImage).setVisibility(View.GONE);
        }


        button.setOnClickListener(v -> {
            closePanel(activity);
        });
    }
    public static void closePanel(InfoHoldable activity){
        if (!isPanel){
            return;
        }

        View panel = activity.getView(R.id.infoPanel);
        Button button = (Button)activity.getView(R.id.infoCardButton);

        Animation a = new TranslateAnimation(0, 0,  0, mainPlain.sizeHeight);
        a.setDuration(200);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                button.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                panel.setVisibility(GONE);
                isPanel = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        panel.startAnimation(a);
    }

}
