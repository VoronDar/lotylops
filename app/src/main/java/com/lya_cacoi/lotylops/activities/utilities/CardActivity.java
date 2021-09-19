package com.lya_cacoi.lotylops.activities.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.ApproachManager.ApproachManager;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlMain;
import com.lya_cacoi.lotylops.Databases.transportSQL.SqlVocaPhrasUnion;
import com.lya_cacoi.lotylops.LevelManager.LevelManager;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.adapters.TextBlockAdapter;
import com.lya_cacoi.lotylops.adapters.units.TextBlock;
import com.lya_cacoi.lotylops.cards.Card;
import com.lya_cacoi.lotylops.declaration.consts;
import com.lya_cacoi.lotylops.ruler.Ruler;

import java.util.ArrayList;
import java.util.Objects;

import static android.view.View.GONE;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.CULTURE_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.GRAMMAR_INDEX;
import static com.lya_cacoi.lotylops.ApproachManager.ApproachManager.PHONETIC_INDEX;
import static com.lya_cacoi.lotylops.activities.mainPlain.dp;
import static com.lya_cacoi.lotylops.activities.mainPlain.repeatTTS;
import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.setRememberButtonText;
import static com.lya_cacoi.lotylops.ruler.CardRepeatManage.riseFromFall;

public abstract class CardActivity extends CommonFragment {

    protected int sqlIndex;
    protected RecyclerView recyclerView;
    protected ArrayList<TextBlock> textBlocksList;
    protected TextBlockAdapter adapter;
    protected Card abstractCard;
    protected ImageView soundButton;
    protected Context context;
    protected View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_learn_voc, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();
        this.view = view;

        sqlIndex = Ruler.getSQLIndex();
        abstractCard = Ruler.getLastCard();
        setSliderSize(SqlMain.getInstance(getContext(), sqlIndex).getLearnCardToday(),
                SqlMain.getInstance(getContext(), sqlIndex).getLearnCardLeft());

        setRememberButtonText(abstractCard.getRepetitionDat(), view);

        soundButton = view.findViewById(R.id.playSound);


        View buttonsCheck = view.findViewById(R.id.checkRemember);
        //LinearLayout buttonsNext = view.findViewById(R.id.layout_next);

        //if ((card.getRepeatlevel() >= consts.START_TEST || card.getPracticeLevel() >= consts.START_TEST) &&
        //        (card.getRepeatlevel() <= consts.SENTENVE)){                                                // пользователю можно определять - знает ли он слово самому до первого показа теста
        //    buttonsCheck.setVisibility(View.GONE);
        //    buttonsNext.setVisibility(View.VISIBLE);
        //    closeThing.setVisibility(View.GONE);
        //}
        // если повторение больше предложения - то и шло оно к чертям, показываем просто карточки
        //else{
        buttonsCheck.setVisibility(View.VISIBLE);
        //buttonsNext.setVisibility(View.GONE);
        //}



        Button remember = view.findViewById(R.id.buttonRemember);
        Button forget = view.findViewById(R.id.buttonForget);
        remember.setOnClickListener(this::onRemember);
        forget.setOnClickListener(this::onForget);
        if (mainPlain.triggerRatio > mainPlain.sizeRatio){
            forget.setTextSize(mainPlain.sizeHeight/
                    (40f*mainPlain.multiple));
            forget.setHeight(mainPlain.sizeHeight/8);
            remember.setTextSize(mainPlain.sizeHeight/ (40f*mainPlain.multiple));
            remember.setHeight(mainPlain.sizeHeight/8);
            if (sqlIndex == ApproachManager.VOCABULARY_INDEX || sqlIndex == ApproachManager.PHRASEOLOGY_INDEX)
                ((TextView)view.findViewById(R.id.open)).setTextSize(mainPlain.sizeHeight/(30f*mainPlain.multiple));
        }
    }


    protected void prepareAdapter(){
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new TextBlockAdapter(context, textBlocksList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter.notifyDataSetChanged();
        adapter.setBlockListener(position -> repeatTTS.speak(textBlocksList.get(position).getText(), TextToSpeech.QUEUE_FLUSH,null));
    }


    public void deleteCard(final View view) {
        view.setClickable(false);
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(400);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(animation);
    }

    public void onForget(View view) {
        Ruler ruler = Ruler.getInstance(context, sqlIndex);
        ruler.reloadErrorStringWhileStudy(abstractCard);

        Fragment fragment = ruler.changeActivityWhileStudy(context);
        ((mainPlain) requireActivity()).slideFragment(fragment);
    }

    public void onRemember(View view) {
        if (Ruler.dayState == consts.dayState.repeat) {
            if (sqlIndex == GRAMMAR_INDEX || sqlIndex == PHONETIC_INDEX || sqlIndex == CULTURE_INDEX) {
                LevelManager.addExp(context, LevelManager.Action.repeatRule);
            } else
                LevelManager.addExp(context, LevelManager.Action.repeatItem);
        } else{
            if (sqlIndex == GRAMMAR_INDEX || sqlIndex == PHONETIC_INDEX || sqlIndex == CULTURE_INDEX) {
                LevelManager.addExp(context, LevelManager.Action.lookRule);
            } else
                LevelManager.addExp(context, LevelManager.Action.lookItem);

        }

            Ruler ruler = Ruler.getInstance(context, sqlIndex);
        riseFromFall(abstractCard);
        Log.i("mainCardDay", Integer.toString(abstractCard.getRepetitionDat()));
        ruler.reloadRightStringWhileStudy(abstractCard);

        Fragment fragment = ruler.changeActivityWhileStudy(context);
        ((mainPlain) requireActivity()).slideFragment(fragment);

    }


    public void setEnableAllCardButtons(Boolean enabled){                                           // make all buttons unusable/usable
        view.findViewById(R.id.playSound).setClickable(enabled);
        view.findViewById(R.id.memButton).setClickable(enabled);
        view.findViewById(R.id.buttonRemember).setClickable(enabled);
        view.findViewById(R.id.buttonForget).setClickable(enabled);
        //view.findViewById(R.id.layout_next).setClickable(enabled);
    }

    public void addMem(){
        setEnableAllCardButtons(false);
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        final View my_custom_view = getLayoutInflater().inflate(R.layout.mem_add_layout, null);
        adb.setOnCancelListener(dialog -> setEnableAllCardButtons(true));

        /*
        if (mainPlain.sizeRatio < mainPlain.triggerRatio){
            ((TextView)my_custom_view.findViewById(R.id.association)).
                    setTextSize(mainPlain.sizeHeight/(30f*mainPlain.multiple));
            ((TextView)my_custom_view.findViewById(R.id.errorName)).
                    setTextSize(mainPlain.sizeHeight/(30f*mainPlain.multiple));
            ((TextView)my_custom_view.findViewById(R.id.yourAnswer)).
                    setTextSize(mainPlain.sizeHeight/(30f*mainPlain.multiple));
            ((Button)my_custom_view.findViewById(R.id.cancel)).
                    setTextSize(mainPlain.sizeHeight/(60f*mainPlain.multiple));
            ((Button)my_custom_view.findViewById(R.id.add)).
                    setTextSize(mainPlain.sizeHeight/(60f*mainPlain.multiple));
        }

         */

        if (abstractCard.getMem() == null){
            my_custom_view.findViewById(R.id.association).setVisibility(GONE);
            my_custom_view.findViewById(R.id.divider).setVisibility(GONE);
        }
        else{
            TextView text = (my_custom_view.findViewById(R.id.association));
            text.setText(abstractCard.getMem());
        }


        adb.setView(my_custom_view);
        final AlertDialog ad = adb.create();
        my_custom_view.findViewById(R.id.cancel).setOnClickListener(v -> {
            setEnableAllCardButtons(true);
            ad.cancel();
        });

        my_custom_view.findViewById(R.id.add).setOnClickListener(v -> {
            EditText text = (my_custom_view.findViewById(R.id.yourAnswer));
            abstractCard.setMem(text.getText().toString());
            //TransportSQLInterface transportSql =  MainTransportSQL.getTransport(sqlIndex,context);
            //transportSql.reloadMem(abstractCard.getId(), abstractCard.getMem());
            Objects.requireNonNull(SqlVocaPhrasUnion.getInstance(context, sqlIndex)).updateCard(abstractCard);
            prepareAdapter();
            ad.cancel();
            addMemInCard();
            adapter.notifyDataSetChanged();
        });

        ad.show();
    }

    protected void addMemInCard(){
        setItem(R.id.association, abstractCard.getMem(), R.id.help_divider);

    }

    @Override
    public String getTitle() {
        return null;
    }


    protected void setItem(int itemId, String info, int dividerId){
        ((TextView)requireView().findViewById(itemId)).setText(info);
        if (info != null && info.length() > 0)
            requireView().findViewById(itemId).setVisibility(View.VISIBLE);
        if (dividerId != 0 && info != null && info.length() > 0)
            requireView().findViewById(dividerId).setVisibility(View.VISIBLE);
    }

    private void setSliderSize(int allCards, int leftCards){

        Log.i("main", "cards, allCards: " + allCards + ", leftCards: " + leftCards);

        View bar = requireView().findViewById(R.id.progress_bar);

        if (allCards == 0){
            bar.setVisibility(GONE);
            return;
        }

        int oneItem = (mainPlain.sizeWidth)/allCards;
        ViewGroup.LayoutParams p = bar.getLayoutParams();
        p.width = oneItem*(allCards-leftCards);
        if (p.width == 0)
            bar.setVisibility(GONE);
        else
            bar.setVisibility(View.VISIBLE);
    }
}
