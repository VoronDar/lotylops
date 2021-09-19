package com.lya_cacoi.lotylops.Databases.Auth;

public class User {
    private String name;
    private String id;
    private int author_rate;
    private int rate;
    private boolean game_active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAuthor_rate() {
        return author_rate;
    }

    public void setAuthor_rate(int author_rate) {
        this.author_rate = author_rate;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public boolean isGame_active() {
        return game_active;
    }

    public void setGame_active(boolean game_active) {
        this.game_active = game_active;
    }


    public static class Info{
        // типы - 1 - загрузка карты, подтверждено, 2 - загрузка карты отклонено,
        // 3 - игра выйграна, 4 - игра проиграна
        private int type;

        public Info(int type) {
            this.type = type;
        }
    }
}
