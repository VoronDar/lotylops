package com.lya_cacoi.lotylops.activities.utilities.panel;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.mainPlain;

import static android.view.View.GONE;
import static com.lya_cacoi.lotylops.activities.utilities.panel.PanelManager.isPanel;

public class ErrorPanel {

    public static void openPanel(InfoHoldable activity, String answer, String right, String info, DoAfter listener){
        if (isPanel){
            closePanel(activity, null);
            return;
        }
        isPanel = true;
        View panel = activity.getView(R.id.errorPanel);
        Button button = (Button)activity.getView(R.id.errorCardButton);
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

        ((TextView)activity.getView(R.id.yourAnswer)).setText(answer);
        ((TextView)activity.getView(R.id.rightAnswer)).setText(right);
        if (info == null || info.length() == 0)
            activity.getView(R.id.infoAnswer).setVisibility(GONE);
        else
            activity.getView(R.id.infoAnswer).setVisibility(View.VISIBLE);
        ((TextView)activity.getView(R.id.infoAnswer)).setText(info);


        button.setOnClickListener(v -> closePanel(activity, listener));
    }
    public static void closePanel(InfoHoldable activity, DoAfter listener){
        if (!isPanel){
            return;
        }

        View panel = activity.getView(R.id.errorPanel);
        Button button = (Button)activity.getView(R.id.errorCardButton);

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
                if (listener != null)
                    listener.act();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        panel.startAnimation(a);
    }

    public interface DoAfter{
        void act();
    }

}
