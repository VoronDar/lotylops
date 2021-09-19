package com.lya_cacoi.lotylops.adapters.units;

import androidx.fragment.app.Fragment;

public class SettingUnit {

    public interface OpenAlertInterface{
        Fragment openAlert();                                                                       // переходит на новый фрагмент
    }
    public interface DoSomehtingInteface{                                                           // выполняет какое-либо действие в случае того, если было нажато "подтвердить"
        void doSomething();
    }

    OpenAlertInterface openAlertInterface;
    DoSomehtingInteface doSomehtingInteface;

    private final int imageID;
    private final String Command;
    private String description;
    private boolean checked = false;
    public Fragment openAlert(){
        return openAlertInterface.openAlert();
    }
    public void doSomething(){
        doSomehtingInteface.doSomething();
    }

    public SettingUnit( int imageID, String command, OpenAlertInterface openAlertInterface) {
        this.openAlertInterface = openAlertInterface;
        this.imageID = imageID;
        Command = command;
    }

    public SettingUnit( int imageID, String command,boolean checked, String description,  DoSomehtingInteface doSomehtingInteface) {
        this.doSomehtingInteface = doSomehtingInteface;
        this.imageID = imageID;
        Command = command;
        this.description = description;
        this.checked = checked;
    }


    public int getImageID() {
        return imageID;
    }

    public String getCommand() {
        return Command;
    }

    public boolean isChecked() {
        return checked;
    }

    public String getDescription() {
        return description;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
