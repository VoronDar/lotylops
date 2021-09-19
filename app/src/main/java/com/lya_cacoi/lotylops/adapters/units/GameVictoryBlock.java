package com.lya_cacoi.lotylops.adapters.units;

public class GameVictoryBlock {
    public boolean isWin;

    public GameVictoryBlock(boolean isWin) {
        this.isWin = isWin;
    }

    public boolean isWin() {
        return isWin;
    }

    public void setWin(boolean win) {
        isWin = win;
    }
}
