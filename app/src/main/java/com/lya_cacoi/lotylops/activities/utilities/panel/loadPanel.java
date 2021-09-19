package com.lya_cacoi.lotylops.activities.utilities.panel;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import static android.view.View.GONE;
import static com.lya_cacoi.lotylops.activities.utilities.panel.PanelManager.isPanel;

/** панель, которая занимается чисто загрузкой, она ждет результатов, */
public class loadPanel {

    private static boolean isCompleted = true;

    public static void openPanel(InfoHoldable activity, String title, String buttonText, OnInfoClickListener listener){
        if (isPanel){
            closePanel(activity);
            return;
        }
        isPanel = true;
        View panel = activity.getView(R.id.infoPanel);
        Button button = (Button)activity.getView(R.id.infoCardButton);
        Button buttonCancel = (Button)activity.getView(R.id.cancelInfoButton);
        panel.setVisibility(View.VISIBLE);
        buttonCancel.setVisibility(View.VISIBLE);

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
        activity.getView(R.id.infoCardDescription).setVisibility(GONE);
        activity.getView(R.id.infoCardImage).setVisibility(GONE);
        button.setText(buttonText);
        button.setOnClickListener(v -> {
            button.setEnabled(false);
            isCompleted = false;
            listener.onClick(() -> {
                isCompleted = true;
                transportPreferences.setNothingStarted(((mainPlain)activity).getContext(), false);
                closePanel(activity);
            });
        });
        buttonCancel.setOnClickListener(v -> closePanel(activity));
    }
    public static void closePanel(InfoHoldable activity){
        if (!isPanel || !isCompleted){
            return;
        }

        View panel = activity.getView(R.id.infoPanel);
        Button button = (Button)activity.getView(R.id.infoCardButton);
        Button buttonCancel = (Button)activity.getView(R.id.cancelInfoButton);

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
                isPanel = false;
                buttonCancel.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        panel.startAnimation(a);
    }

    /** обычно closePanel может запуститься для лишней проверочки и может не закрыть его если оно что-то загружает, но это закрывает конкретно*/
    public static void forseClose(InfoHoldable activity){
        isCompleted = true;
        closePanel(activity);
    }

    /** вызывается, когда на панели прожимается кнопка*/
    public interface OnInfoClickListener{
        void onClick(OnLoadedListener listener);
    }

    /** */
    public interface OnLoadedListener{
        void onLoaded();
    }
}
