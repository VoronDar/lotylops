package com.lya_cacoi.lotylops.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lya_cacoi.lotylops.Databases.Auth.User;
import com.lya_cacoi.lotylops.Game.GameData;
import com.lya_cacoi.lotylops.Game.GameManager;
import com.lya_cacoi.lotylops.Helper.Helper;
import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.activities.Games.GameFifthCheckSynonym;
import com.lya_cacoi.lotylops.activities.Games.GameFirstCheckTranslate;
import com.lya_cacoi.lotylops.activities.Games.GameFoursSentenceWrite;
import com.lya_cacoi.lotylops.activities.Games.GameSecondWordWrite;
import com.lya_cacoi.lotylops.activities.Games.GameThirdCheckGroup;
import com.lya_cacoi.lotylops.activities.Games.GameThirdSoundWrite;
import com.lya_cacoi.lotylops.activities.utilities.CommonFragment;
import com.lya_cacoi.lotylops.adapters.winSeriesAdapter;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.Random;

import static android.view.View.GONE;
import static com.lya_cacoi.lotylops.transportPreferences.transportPreferences.getGameScores;


public class GamePlayFragment extends CommonFragment {

    private GameData data;

    private boolean isTurnCompleted = false;
    private int yourPoints = 0;
    private int hisPoints = 0;


    public GamePlayFragment() {
    }


    public GamePlayFragment(int yourPoints, int hisPoints) {

    }

    private GamePlayFragment(GameData data, int hisPoints, int yourPoints) {
        this.data = data;
        isTurnCompleted = true;
        this.yourPoints = yourPoints;
        this.hisPoints = hisPoints;
    }
    public GamePlayFragment(GameData data) {
        this.data = data;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game, container, false);

    }

    @SuppressLint("WrongViewCast")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //FirebaseDatabase db = new FirebaseDatabase();
        //final GameData data =  db.getGameData();

        Helper.showHelp(requireContext(), Helper.Help.gamePlay);

        if (data == null)
            //data = new GameData("g1", "god", "gg2", 25, 25, 1, 1, -1, -1, 0, 0);
            data = new GameData("g1", "god", transportPreferences.getPlayerName(requireContext()),
                    transportPreferences.getPlayerHealth(requireContext(), 1),
                    transportPreferences.getPlayerHealth(requireContext(), 2),
                    transportPreferences.getTurnType(requireContext()),
                    transportPreferences.getTurnplayer(requireContext()),
                    transportPreferences.getPlayerScore(requireContext(), 1),
                    transportPreferences.getPlayerScore(requireContext(), 2),
                    transportPreferences.getWinAlwaysCount(requireContext()),
                    transportPreferences.getWinHolder(requireContext()));

        winSeriesAdapter adapter = new winSeriesAdapter((data.getWins_holder() == 1)?data.getWins_always_count():0, requireContext());
        winSeriesAdapter his_adapter = new winSeriesAdapter((data.getWins_holder() == 2)?data.getWins_always_count():0, requireContext());

        RecyclerView his_recycler = view.findViewById(R.id.his_win);
        RecyclerView your_recycler = view.findViewById(R.id.your_win);
        his_recycler.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        your_recycler.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        his_recycler.setAdapter(his_adapter);
        your_recycler.setAdapter(adapter);

        final Random random = new Random(Runtime.getRuntime().freeMemory());


        final User user = new User();
        user.setId("god");
        user.setRate(100);

        view.findViewById(R.id.firstPlay).setOnClickListener(v -> {
            data.setTurn_type(1);
            //if (random.nextBoolean())
            ((mainPlain)requireActivity()).slideFragment(new GameFirstCheckTranslate(data, user));
            //else
            //    ((mainPlain)requireActivity()).slideFragment(new GameFirstSentenceSound(data, user));
        });

        view.findViewById(R.id.secondPlay).setOnClickListener(v -> {
            data.setTurn_type(2);
            if (random.nextBoolean())
                ((mainPlain)requireActivity()).slideFragment(new GameThirdSoundWrite(data, user));
            else
                ((mainPlain)requireActivity()).slideFragment(new GameFifthCheckSynonym(data, user));
        });
        view.findViewById(R.id.thirdPlay).setOnClickListener(v -> {
            data.setTurn_type(3);
            if (random.nextBoolean())
                ((mainPlain)requireActivity()).slideFragment(new GameSecondWordWrite(data, user));
            else
                ((mainPlain)requireActivity()).slideFragment(new GameThirdCheckGroup(data, user));

        });


        if (!transportPreferences.isTrainAllowed(requireContext()))
            view.findViewById(R.id.fourthPlay).setVisibility(GONE);
        else {
            view.findViewById(R.id.fourthPlay).setOnClickListener(v -> {
                data.setTurn_type(4);
                ((mainPlain) requireActivity()).slideFragment(new GameFoursSentenceWrite(data, user));
            });
        }


        /*
        view.findViewById(R.id.fifthPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setTurn_type(5);
            }
        });
         */

        ImageView gamerImage = view.findViewById(R.id.gamerImage);
        final int difficulty;

        switch (data.getPlayer_two()){
            case "1":
                gamerImage.setImageDrawable(requireContext().getResources().getDrawable(R.drawable.avatar_two));
                difficulty = 0;
                break;
            case "2":
                gamerImage.setImageDrawable(requireContext().getResources().getDrawable(R.drawable.gamer2));
                difficulty = 2;
                break;
            case "3":
                gamerImage.setImageDrawable(requireContext().getResources().getDrawable(R.drawable.avatar_one));
                difficulty = 3;
                break;
                default:
                    difficulty = 0;
        }

        view.findViewById(R.id.fifthPlay).setVisibility(GONE);

        setHealf();

        // предположим - что мы игрок 1

        final LinearLayout buttons_play = view.findViewById(R.id.game_buttons);
        View not_your_turn = view.findViewById(R.id.completed);
        final View turnDone = view.findViewById(R.id.turnDone);
         if (data.getId() == null){

             (view.findViewById(R.id.turnDone)).setVisibility(GONE);
             (view.findViewById(R.id.game_buttons)).setVisibility(GONE);

             view.findViewById(R.id.your_win).setVisibility(View.GONE);
             view.findViewById(R.id.his_win).setVisibility(View.GONE);

            view.findViewById(R.id.youWin).setVisibility(View.VISIBLE);
            if (data.getWinner() == 1) {
                ((TextView) view.findViewById(R.id.result)).setText("Вы выйграли!");
                transportPreferences.setGameScores(requireContext(), 30*(difficulty+1)*(data.getOnes_healh()-data.getSecond_healh()) + getGameScores(requireContext()));

            }
            else
                ((TextView)view.findViewById(R.id.result)).setText("Вы проиграли");{
                transportPreferences.setGameScores(requireContext(), getGameScores(requireContext()));
            }


            ((TextView)view.findViewById(R.id.scores)).setText("Вы набрали\n" + transportPreferences.getGameScores(requireContext()) + " очков");


            if (getGameScores(requireContext()) >= transportPreferences.getMaxGameScores(requireContext())) {
                transportPreferences.setMaxGameScores(requireContext(), getGameScores(requireContext()));
                view.findViewById(R.id.newRecord).setVisibility(View.VISIBLE);
            }
            transportPreferences.setGameScores(requireContext(), 0);

            view.findViewById(R.id.ok).setOnClickListener(v -> ((mainPlain) requireActivity()).slideFragment(new GameStartFragment()));
        }
        else if (!isTurnCompleted  && data.getTurn_player() == 1 && data.getOnes_score() == -1 && data.getSecond_scores() == -1){
            buttons_play.setVisibility(View.VISIBLE);
            turnDone.setVisibility(View.GONE);
            not_your_turn.setVisibility(GONE);
        } else if (data.getTurn_player() == 2) {
            buttons_play.setVisibility(GONE);
            turnDone.setVisibility(View.GONE);
            not_your_turn.setVisibility(View.VISIBLE);

            // костыль
            not_your_turn.setOnClickListener(v -> {

                switch (data.getTurn_type()) {
                    case 1:
                        data.setSecond_scores((1 + random.nextInt(5)) + difficulty);
                        break;
                    case 2:
                        data.setSecond_scores(2 * (1 + random.nextInt(2)) + difficulty * 2);
                        break;
                    case 3:
                        data.setSecond_scores(3 * (1 + random.nextInt(2)) + difficulty * 3);
                        break;
                    case 4:
                        data.setSecond_scores(4 * (1 + random.nextInt(4)) + difficulty * 4);
                        break;
                    case 5:
                        data.setSecond_scores(5 * (2 + random.nextInt(5)) + difficulty * 5);
                        break;
                }

                Log.i("main", "his scores - " + data.getSecond_scores());
                int hisScores = data.getSecond_scores();
                int yourScores = data.getOnes_score();
                data.setTurn_player(1);
                GameManager.turnDone(requireContext(), data, false);
                ((mainPlain) requireActivity()).slideFragment(new GamePlayFragment(data, hisScores, yourScores));
            });
        }
            else if (isTurnCompleted  && data.getTurn_player() == 1 && data.getOnes_score() == -1 && data.getSecond_scores() == -1) {
                isTurnCompleted = false;
                buttons_play.setVisibility(View.GONE);
                not_your_turn.setVisibility(GONE);
                turnDone.setVisibility(View.VISIBLE);
                ((ImageView)view.findViewById(R.id.his_image)).setImageDrawable(gamerImage.getDrawable());
                ((TextView)(view.findViewById(R.id.your_points))).setText(yourPoints + "");
                ((TextView)(view.findViewById(R.id.his_points))).setText(hisPoints + "");
                requireView().findViewById(R.id.buttonNext).setOnClickListener(v -> {
                    turnDone.setVisibility(GONE);
                    buttons_play.setVisibility(View.VISIBLE);
                    if (data.getId() == null)  ((mainPlain) requireActivity()).slideFragment(new GamePlayFragment(data));
                });
            } else if (data.getTurn_player() == 1){
            // ПОКА ТАК
            GameManager.turnDone(requireContext(), data, false);
            ((mainPlain)requireActivity()).slideFragment(new GamePlayFragment(data));
        }

    }


    private void setHealf(){
        ((LinearLayout)requireView().findViewById(R.id.your_healh_holder)).setWeightSum(25);
        ((LinearLayout)requireView().findViewById(R.id.his_healh_holder)).setWeightSum(25);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) requireView().findViewById(R.id.your__healf).getLayoutParams();
        params.weight = data.getOnes_healh();
        LinearLayout.LayoutParams paramsHis = (LinearLayout.LayoutParams) requireView().findViewById(R.id.his__healf).getLayoutParams();
        paramsHis.weight = data.getSecond_healh();

        Log.i("main", data.getOnes_healh() + " " + data.getSecond_healh());


    }


    private void showHelp(){

        final AlertDialog.Builder adb = new AlertDialog.Builder(requireContext());
        adb.setTitle("игра (нормальное сделаю потом)");
        adb.setMessage("Вы можете поиграть в игру. Доведите жизни противника до нуля, выполняя задания. Разные задания имеют разную силу");
        final AlertDialog alertDialog = adb.create();


        adb.setPositiveButton("ок", (dialog, which) -> {

        });
        alertDialog.show();

    }

    @Override
    public String getTitle() {
        return null;
    }
}
