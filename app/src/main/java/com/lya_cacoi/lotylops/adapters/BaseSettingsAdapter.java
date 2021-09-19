package com.lya_cacoi.lotylops.adapters;

public interface BaseSettingsAdapter {
    boolean isClickable();
    void setClickable(boolean clickable);
    void setBlockListener(SettingsAdapter.BlockListener block_listener);
    void notifyDataSetChanged();

}
