package com.lya_cacoi.lotylops.Game;

import android.content.Context;

import com.lya_cacoi.lotylops.Achieve.AchieveManager;
import com.lya_cacoi.lotylops.Databases.Auth.User;
import com.lya_cacoi.lotylops.Databases.FirebaseAccess.FirebaseDatabase;
import com.lya_cacoi.lotylops.LevelManager.LevelManager;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.ArrayList;
import java.util.List;

public class GameManager implements FirebaseDatabase.GameWannable {

    public static ArrayList<GameLog> logs = new ArrayList<>();

    // игровые логи
    public static class GameLog{

        // если answer пустой, значит задание на слух

        public final String answer;
        public final String right;
        public final String your;
        public final boolean isRight;

        public GameLog(String answer, String right, String your, boolean isRight) {
            this.answer = answer;
            this.right = right;
            this.your = your;
            this.isRight = isRight;
        }
    }


    public void findStranger(){
        FirebaseDatabase db = new FirebaseDatabase();
        db.findPersonForGame(new User(),this);
    }

    @Override
    public void offer(List<User> users) {

    }

    public static void turnDone(Context context, GameData gameData, boolean isMultiplay) {

        //// ЗАПИСЬ ЗНАЧЕНИЙ В КЛАСС ПОСЛЕ ХОДА ////////////////////////////////////////////////////
        if (gameData.getOnes_score() != -1 && gameData.getSecond_scores() != -1) {
            if (gameData.getOnes_score() >= gameData.getSecond_scores()) {

                transportPreferences.setGameScores(context, transportPreferences.getGameScores(context) + (gameData.getOnes_healh()-gameData.getSecond_scores())*5);

                gameData.setOnes_healh(gameData.getOnes_healh() + gameData.getTurn_type());
                gameData.setSecond_healh(gameData.getSecond_healh() - gameData.getTurn_type()*3);
                if (gameData.getWins_holder() != 1) {
                    gameData.setWins_holder(1);
                    gameData.setWins_always_count(1);
                } else gameData.setWins_always_count(gameData.getWins_always_count() + 1);

            } else if (gameData.getOnes_score() < gameData.getSecond_scores()) {
                transportPreferences.setGameScores(context, transportPreferences.getGameScores(context) - (gameData.getSecond_scores()-gameData.getOnes_healh())*5);
                gameData.setOnes_healh(gameData.getOnes_healh() - gameData.getTurn_type()*3);
                gameData.setSecond_healh(gameData.getSecond_healh() + (gameData.getTurn_type()));

                if (gameData.getWins_holder() != 2) {
                    gameData.setWins_holder(2);
                    gameData.setWins_always_count(1);
                } else gameData.setWins_always_count(gameData.getWins_always_count() + 1);
            }
            gameData.setOnes_score(-1);
            gameData.setSecond_scores(-1);
        } else{
            //Log.i("main", "something went wrong");
            transportPreferences.setPlayerHealth(context, 25, 2);
            transportPreferences.setPlayerHealth(context, 25, 1);


            transportPreferences.setPlayerScore(context, -1, 1);
            transportPreferences.setPlayerScore(context, -1, 2);

            transportPreferences.setWinAlwaysCount(context, 0);
            transportPreferences.setWinHolder(context, 1);
            transportPreferences.setTurnplayer(context, 1);
            gameData.setId(null);
            return;
        }
        ////////////////////////////////////////////////////////////////////////////////////////////



        //// СОХРАНЕНИЕ ЗНАЧЕНИЙ НА СЕРВЕРЕ ИЛИ В TRANSPORT PREFERENCES ////////////////////////////
        if (isMultiplay) {
            FirebaseDatabase db = new FirebaseDatabase();

            if (gameData.getOnes_healh() == 0) {

                db.gamerWinOrLose(gameData.getPlayer_one(), gameData.getSecond_healh(), true);
                db.gamerWinOrLose(gameData.getPlayer_two(), gameData.getSecond_healh(), false);
                db.gameClose(gameData.getId());

                // ход был первого
                if (gameData.getTurn_type() == 2) db.informPlayer(gameData.getPlayer_two(), true);
                    // ход был второго
                else db.informPlayer(gameData.getPlayer_one(), false);

            } else if (gameData.getSecond_healh() == 0) {
                db.gamerWinOrLose(gameData.getPlayer_two(), gameData.getOnes_healh(), true);
                db.gamerWinOrLose(gameData.getPlayer_one(), gameData.getOnes_healh(), false);
                db.gameClose(gameData.getId());

                // ход был первого
                if (gameData.getTurn_type() == 2) db.informPlayer(gameData.getPlayer_two(), false);
                    // ход был второго
                else db.informPlayer(gameData.getPlayer_one(), true);
            } else {
                db.updateGame(gameData);
            }
        } else{

            if (gameData.getOnes_healh() == 0 || gameData.getSecond_healh() == 0 ||
                    gameData.getWins_always_count() >=5) {

                //// ИГРА ЗАВЕРШЕНА ////////////////////////////////////////////////////////////////
                if (gameData.getOnes_healh() == 0 || (gameData.getWins_always_count() >= 5 && gameData.getWins_holder() == 2)){
                    gameData.setWinner(2);
                } else
                    gameData.setWinner(1);


                if (gameData.getWinner() == 1 && Integer.parseInt(transportPreferences.getPlayerName(context)) == 1){
                    AchieveManager.setAchieve(context, AchieveManager.A_EASY_WON);
                }

                setExp(gameData.getWinner(), Integer.parseInt(transportPreferences.getPlayerName(context)), context);


                transportPreferences.setPlayerHealth(context, 25, 2);
                transportPreferences.setPlayerHealth(context, 25, 1);


                transportPreferences.setPlayerScore(context, -1, 1);
                transportPreferences.setPlayerScore(context, -1, 2);

                transportPreferences.setWinAlwaysCount(context, 0);
                transportPreferences.setWinHolder(context, 1);
                transportPreferences.setTurnplayer(context, 1);
                transportPreferences.setPlayerName(context, null);
                gameData.setId(null);


                ////////////////////////////////////////////////////////////////////////////////////
            } else{

                //// ИГРА ПРОДОЛЖАЕТСЯ /////////////////////////////////////////////////////////////
                transportPreferences.setPlayerHealth(context, gameData.getSecond_healh(), 2);
                transportPreferences.setPlayerHealth(context, gameData.getOnes_healh(), 1);


                transportPreferences.setPlayerScore(context, gameData.getOnes_score(), 1);
                transportPreferences.setPlayerScore(context, gameData.getSecond_scores(), 2);

                transportPreferences.setWinAlwaysCount(context, gameData.getWins_always_count());
                transportPreferences.setWinHolder(context, gameData.getWins_holder());
                transportPreferences.setTurnplayer(context, gameData.getTurn_player());
                ////////////////////////////////////////////////////////////////////////////////////
            }

        }
        ////////////////////////////////////////////////////////////////////////////////////////////

    }


    public static void endGame(Context context){
        transportPreferences.setPlayerHealth(context, 25, 2);
        transportPreferences.setPlayerHealth(context, 25, 1);


        transportPreferences.setPlayerScore(context, -1, 1);
        transportPreferences.setPlayerScore(context, -1, 2);

        transportPreferences.setWinAlwaysCount(context, 0);
        transportPreferences.setWinHolder(context, 1);
        transportPreferences.setTurnplayer(context, 1);
        transportPreferences.setPlayerName(context, null);

    }


    private static void setExp(int winner, int hisId, Context context){

        if (winner == 2) {
            LevelManager.addExp(context, LevelManager.Action.gameLose);
            return;
        }


        switch (hisId){
            case 1:
                    LevelManager.addExp(context, LevelManager.Action.gameEasyWin);
                    break;
            case 2:
                LevelManager.addExp(context, LevelManager.Action.gameMediumWin);
                break;
            case 3:
                LevelManager.addExp(context, LevelManager.Action.gameHardWin);
                break;
        }

    }
}
