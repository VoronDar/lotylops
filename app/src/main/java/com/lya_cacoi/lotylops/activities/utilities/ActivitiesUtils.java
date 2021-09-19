package com.lya_cacoi.lotylops.activities.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lya_cacoi.lotylops.R;

import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHRASEOLOGY_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.VOCABULARY_INDEX;

public class ActivitiesUtils {

    public static void removeTitleBar(AppCompatActivity activity){                                  // hide the fic'4ng title bar
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
    public static void setRememberButtonText(int repeatDay, View activity){                         // switch remember|forget buttons text in cards to don't know|know
        Button remember = activity.findViewById(R.id.buttonRemember);
        Button forget = activity.findViewById(R.id.buttonForget);
        if (repeatDay <= 0){
            remember.setText(activity.getResources().getString(R.string.yesKnow));
            forget.setText(activity.getResources().getString(R.string.noKnow));
        }
        else{
            remember.setText(activity.getResources().getString(R.string.yes));
            forget.setText(activity.getResources().getString(R.string.no));
        }
    }
    public static void setOrientation(AppCompatActivity activity){
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /////// GET COLOURS ////////////////////////////////////////////////////////////////////////////
    public static int getWordColor(View activity){                                     // get color for the word type
        return 0;
    }
    public static int getTranslateColor(View activity){                                // get color for the translate type (dark)
        return activity.getResources().getColor(R.color.colorWhiteTranslate);
    }
    public static int getMeaningColor(View activity){                                  // get color for the meaning
        return activity.getResources().getColor(R.color.colorWhiteMeaning);
    }
    public static int getExampleColor(View activity){                                  // get color for the example
        return activity.getResources().getColor(R.color.colorWhiteExample);
    }
    public static int getSynonymColor(View activity){                                  // get color for the synonym (fade)
        return activity.getResources().getColor(R.color.colorWhiteSynonym);
    }
    public static int getHelpColor(View activity){                                     // get color for the help (darkest)
        return activity.getResources().getColor(R.color.colorBlack);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    public static void setGroup(View activity, String cardGroup){                      // set group or if it is'nt existing - remove from screen
        TextView group = activity.findViewById(R.id.group);
        if (cardGroup != null) {
            group.setText(cardGroup);
        } else {
            group.setVisibility(View.GONE);
        }
    }
    public static void setPart(View activity, String cardPart){                        // set part or if it is'nt existing - put noun
        TextView part = activity.findViewById(R.id.part);
        if (cardPart != null) {
            part.setText(cardPart);
        } else {
            part.setText("noun");
        }
    }


    public static void prepareCardScroll(View activity){                               // prepare scroll - remove animation borders and move entries in the top of scroll
        //ScrollView scroll = activity.findViewById(R.id.cardScroll);
        //scroll.setOverScrollMode(View.OVER_SCROLL_NEVER);
        //scroll.smoothScrollTo(0, 0);
    }

    public static SoundManager getPreparedSoundManager(AppCompatActivity activity, String soundName){
        String soundPath = soundName + ".mp3";
        SoundManager soundManager = new SoundManager(activity);
        soundManager.initSoundPool(soundPath);
        return soundManager;
    }

    public static String getSectionUserString(Context context, int index){
        switch (index){
            case VOCABULARY_INDEX:
                return context.getResources().getString(R.string.section_voc);
            case PHRASEOLOGY_INDEX:
                return context.getResources().getString(R.string.section_phr);
            default:
                return null;
        }
    }

    public static void createErrorWindow(Activity context, DoWhileError func, String ID, String Error){
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        View my_custom_view = context.getLayoutInflater().inflate(R.layout.card_showing_error, null);
        adb.setView(my_custom_view);
        final DoWhileError function = func;
        TextView error = my_custom_view.findViewById(R.id.error_text);
        error.setText(context.getResources().getString(R.string.card) + ID + ", " + Error);
        adb.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                function.action();
            }
        });
        final AlertDialog ad = adb.create();
        my_custom_view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ad.cancel();
            }
        });
        ad.show();
    }

    public interface DoWhileError{
        void action();
    }

}
