package com.lya_cacoi.lotylops.SenteceCheck.Command;

import com.lya_cacoi.lotylops.SenteceCheck.Part;

public class MistakeCommand extends Command {
    public final String correctWriting;

    public MistakeCommand(String name, Part mistake, String correctWriting, String mistakeId) {
        super(name, mistake, mistakeId);

        this.correctWriting = correctWriting;
    }

    @Override
    public void show() {
        System.out.println(name);
    }
}