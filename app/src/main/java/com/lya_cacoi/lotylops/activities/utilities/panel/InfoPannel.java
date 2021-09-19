package com.lya_cacoi.lotylops.activities.utilities.panel;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.mainPlain;

import static android.view.View.GONE;
import static com.lya_cacoi.lotylops.activities.utilities.panel.PanelManager.isPanel;

public class InfoPannel {
    private static OnInfoClickListener listener = null;

    public static void openPanel(InfoHoldable activity, String title, String buttonText, OnInfoClickListener listener, boolean keepListener){
        if (isPanel){
            closePanel(activity, null);
            return;
        }
        isPanel = true;
        if (keepListener)
            InfoPannel.listener = listener;
        else
            InfoPannel.listener = null;
        View panel = activity.getView(R.id.infoPanel);
        Button button = (Button)activity.getView(R.id.infoCardButton);
        panel.setVisibility(View.VISIBLE);

        Animation a = new TranslateAnimation(0, 0,  mainPlain.dp(500), 0);
        a.setDuration(400);
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
        ((TextView)activity.getView(R.id.infoCardDescription)).setVisibility(GONE);
        button.setText(buttonText);
        button.setOnClickListener(v -> {
            listener.onClick();
            closePanel(activity, null);
        });
    }
    public static void closePanel(InfoHoldable activity, OnPanelCloseListener l){
        if (!isPanel){
            if (l != null)
                l.onClose();
            return;
        }

        if (listener != null){
            listener.onClick();
        }

        View panel = activity.getView(R.id.infoPanel);
        Button button = (Button)activity.getView(R.id.infoCardButton);

        Animation a = new TranslateAnimation(0, 0,  0, mainPlain.dp(500));
        a.setDuration(200);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                button.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                panel.setVisibility(GONE);
                if (l != null)
                    l.onClose();
                isPanel = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        panel.startAnimation(a);
    }

    /** вызывается, когда на панели прожимается кнопка*/
    public interface OnInfoClickListener{
        void onClick();
    }
    /** вызывается, когда разрешено управлять какими-либо событиями, после того как закроется панель*/
    public interface OnPanelCloseListener{
        void onClose();
    }
}
