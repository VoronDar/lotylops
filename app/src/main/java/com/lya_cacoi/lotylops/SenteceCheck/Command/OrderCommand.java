package com.lya_cacoi.lotylops.SenteceCheck.Command;

import com.lya_cacoi.lotylops.SenteceCheck.Part;

public class OrderCommand extends Command{
    public final Part mistakeLast;

    public OrderCommand(String name, Part mistakeMain, Part mistakeLast, String mistakeId) {
        super(name, mistakeMain, mistakeId);
        this.mistakeLast = mistakeLast;
    }
    @Override
    public void show() {
            System.out.println(name);
        }
}
