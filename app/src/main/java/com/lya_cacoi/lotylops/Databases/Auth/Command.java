package com.lya_cacoi.lotylops.Databases.Auth;

import android.content.Context;

import com.lya_cacoi.lotylops.activities.mainPlain;
import com.lya_cacoi.lotylops.transportPreferences.transportPreferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {

    public String comand;
    public String user;

    public Command() {
    }

    public Command(String comand, String user) {
        this.comand = comand;
        this.user = user;
    }

    public static void interpierCommand(String command, Context context){
        String[] commands = command.split(";");
        for (String com: commands){
            int count = 0;
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(com);
            if (matcher.find()){
                System.out.println(com.substring(matcher.start(), matcher.end()));
                count = Integer.parseInt(com.substring(matcher.start(), matcher.end()));
            } else
                count = -1;
            if (com.contains("give")) {
                mainPlain.activity.addMoney(count);
                transportPreferences.setMoney(context, transportPreferences.getMoney(context) + count);
            } else if (com.contains("up rate")){

            } else if (com.contains("down rate")){
            }else if (com.contains("decline")){
                mainPlain.activity.declineCardLoad();
            }
        }
    }

}
