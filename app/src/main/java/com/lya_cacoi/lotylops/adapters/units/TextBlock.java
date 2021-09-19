package com.lya_cacoi.lotylops.adapters.units;

public class TextBlock {
    private int textSize;
    private String text;
    private int colorID;
    private int backkgroundColorId;
    private boolean needDivider;
    private String imageId;

    public TextBlock(int textSize, String text, int colorID, boolean needDivider) {
        this.textSize = textSize;
        this.text = text;
        this.colorID = colorID;
        this.needDivider = needDivider;
        this.imageId = null;
    }
    public TextBlock(int textSize, String text, int colorID) {
        this.textSize = textSize;
        this.text = text;
        this.colorID = colorID;
        this.needDivider = false;
        this.imageId = null;
    }

    public TextBlock(int textSize, String text, int colorID, String imageID) {
        this.textSize = textSize;
        this.text = text;
        this.colorID = colorID;
        this.needDivider = false;
        this.imageId = imageID;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColorID() {
        return colorID;
    }

    public void setColorID(int colorID) {
        this.colorID = colorID;
    }

    public int getBackkgroundColorId() {
        return backkgroundColorId;
    }

    public boolean isNeedDivider() {
        return needDivider;
    }

    public String getImageId() {
        return imageId;
    }
}
