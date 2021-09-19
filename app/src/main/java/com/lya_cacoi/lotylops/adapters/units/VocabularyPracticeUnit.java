package com.lya_cacoi.lotylops.adapters.units;

public class VocabularyPracticeUnit {
    private int ID;
    private String command;

    public VocabularyPracticeUnit(int ID, String command) {
        this.ID = ID;
        this.command = command;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
