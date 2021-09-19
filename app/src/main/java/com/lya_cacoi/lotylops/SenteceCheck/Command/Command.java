package com.lya_cacoi.lotylops.SenteceCheck.Command;

import com.lya_cacoi.lotylops.SenteceCheck.Part;

import static com.lya_cacoi.lotylops.SenteceCheck.Command.GM.TO_BE;
import static com.lya_cacoi.lotylops.SenteceCheck.Command.GM.VERB_INF;
import static com.lya_cacoi.lotylops.SenteceCheck.Command.GM.VERB_ING;
import static com.lya_cacoi.lotylops.SenteceCheck.Command.GM.VERB_PAST;

public class Command {
    public final String name;
    public final Part main;
    public final String mistakeId;

    public void show() {
        System.out.println(name);
    }

    public void cancel() {
        System.out.println("mmm");
    }

    public Command(String name, Part main, String mistakeId) {
        this.name = name;
        this.main = main;
        this.mistakeId = mistakeId;
    }

    public boolean isGrammarMistake(){
        return (mistakeId != null

                // потом убери
        && !mistakeId.equals(TO_BE)&& !mistakeId.equals(VERB_INF) && !mistakeId.equals(VERB_ING) && !mistakeId.equals(VERB_PAST)
        );
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", main=" + main +
                ", mistakeId='" + mistakeId + '\'' +
                '}';
    }
}
