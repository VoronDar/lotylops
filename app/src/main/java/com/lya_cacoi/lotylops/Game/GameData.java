package com.lya_cacoi.lotylops.Game;

public class GameData {

    private final int max_healf = 25;

    private String id;

    private String player_one;
    private String player_two;

    // здоровье
    private int ones_healh;
    private int second_healh;

    // тип хода
    private int turn_type;

    // владелец хода
    private int turn_player;

    // очки за этот ход
    private int ones_score;
    private int second_scores;

    // счетчик побед подряд и держатель этого счетчика
    private int wins_always_count;
    private int wins_holder;

    private int winner;

    public int getMax_healf() {
        return max_healf;
    }

    public int getWins_always_count() {
        return wins_always_count;
    }

    public void setWins_always_count(int wins_always_count) {
        this.wins_always_count = wins_always_count;
    }

    public int getWins_holder() {
        return wins_holder;
    }

    public void setWins_holder(int wins_holder) {
        this.wins_holder = wins_holder;
    }

    public GameData() {
    }

    public GameData(String id, String player_one, String player_two, int ones_healh, int second_healh, int turn_type, int turn_player, int ones_score, int second_scores, int wins_always_count, int wins_holder) {
        this.id = id;
        this.player_one = player_one;
        this.player_two = player_two;
        this.ones_healh = ones_healh;
        this.second_healh = second_healh;
        this.turn_type = turn_type;
        this.turn_player = turn_player;
        this.ones_score = ones_score;
        this.second_scores = second_scores;
        this.wins_always_count = wins_always_count;
        this.wins_holder = wins_holder;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayer_one() {
        return player_one;
    }

    public void setPlayer_one(String player_one) {
        this.player_one = player_one;
    }

    public String getPlayer_two() {
        return player_two;
    }

    public void setPlayer_two(String player_two) {
        this.player_two = player_two;
    }

    public int getOnes_healh() {
        return ones_healh;
    }

    public void setOnes_healh(int ones_healh) {
        if (ones_healh > max_healf) ones_healh = max_healf;
        if (ones_healh < 0) ones_healh = 0;
        this.ones_healh = ones_healh;
    }

    public int getSecond_healh() {
        return second_healh;
    }

    public void setSecond_healh(int second_healh) {
        if (second_healh > max_healf) second_healh = max_healf;
        if (second_healh < 0) second_healh = 0;
        this.second_healh = second_healh;
    }

    public int getTurn_type() {
        return turn_type;
    }

    public void setTurn_type(int turn_type) {
        this.turn_type = turn_type;
    }

    public int getTurn_player() {
        return turn_player;
    }

    public void setTurn_player(int turn_player) {
        this.turn_player = turn_player;
    }

    public int getOnes_score() {
        return ones_score;
    }

    public void setOnes_score(int ones_score) {
        this.ones_score = ones_score;
    }

    public int getSecond_scores() {
        return second_scores;
    }

    public void setSecond_scores(int second_scores) {
        this.second_scores = second_scores;
    }


    @Override
    public String toString() {
        return "GameData{" +
                "max_healf=" + max_healf +
                ", id='" + id + '\'' +
                ", player_one='" + player_one + '\'' +
                ", player_two='" + player_two + '\'' +
                ", ones_healh=" + ones_healh +
                ", second_healh=" + second_healh +
                ", turn_type=" + turn_type +
                ", turn_player=" + turn_player +
                ", ones_score=" + ones_score +
                ", second_scores=" + second_scores +
                ", wins_always_count=" + wins_always_count +
                ", wins_holder=" + wins_holder +
                '}';
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }
}
