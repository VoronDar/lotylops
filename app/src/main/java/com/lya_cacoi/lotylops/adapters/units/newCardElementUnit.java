package com.lya_cacoi.lotylops.adapters.units;

import com.lya_cacoi.lotylops.activities.NewCardFragment;

public class newCardElementUnit {
    private String name;
    private String value;
    private String prevValue;
    private boolean required;
    private NewCardFragment.ElIds id;
    private boolean editable = true;

    public newCardElementUnit(String name, String prevValue, boolean required, NewCardFragment.ElIds id) {
        this.name = name;
        this.prevValue = prevValue;
        this.required = required;
        this.id = id;
    }

    public newCardElementUnit(String name, String prevValue, boolean required, NewCardFragment.ElIds id, boolean editable) {
        this(name, prevValue, required, id);
        this.editable = editable;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPrevValue() {
        return prevValue;
    }

    public NewCardFragment.ElIds getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isEditable() {
        return editable;
    }

    @Override
    public String toString() {
        return "newCardElementUnit{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", prevValue='" + prevValue + '\'' +
                ", required=" + required +
                ", id=" + id +
                ", editable=" + editable +
                '}';
    }
}
