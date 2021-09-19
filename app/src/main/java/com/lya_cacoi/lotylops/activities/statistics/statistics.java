package com.lya_cacoi.lotylops.activities.statistics;


import android.content.Context;

import com.lya_cacoi.lotylops.activities.statistics.command.Command;

import java.util.ArrayList;

public class statistics {
    private ArrayList<Command> commands;                                                            // queue with commands
    private static statistics instance;                                                             // the object itself
    private Context context;


    //////// LIFE CYCLE ////////////////////////////////////////////////////////////////////////////
    private statistics(){
        commands = new ArrayList<>();
    }
    public static statistics getInstance(Context context){
        if (instance == null)
            instance = new statistics();
        instance.context = context;
        return instance;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////


    //////// COMMAND USE ///////////////////////////////////////////////////////////////////////////
    public void putCommand(Command command){
        commands.add(command);
    }
    public void executeCommand(){
        commands.get(0).action(context);
        commands.remove(0);
    }
    public void cancelComand(){
        commands.get(0).drop(context);
        commands.remove(0);
    }
    public String getActionName(){                                                                  // get string command for user
        return commands.get(0).getName();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////



    public boolean isEmpty(){
        return (commands.size() == 0);
    }
}
