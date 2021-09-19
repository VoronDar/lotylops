package com.lya_cacoi.lotylops.Databases.FirebaseAccess.pojo;

public final class Course {
    private int version = 0;
    private boolean accessibility = true;    // является ли курс полностью открытым для пользования
    private boolean hasAccess = true;       // есть ли доступ к курсу у пользователя
    private String id;
    private boolean isEnabled = false;       // добавлен ли курс в обучение
    private int difficulty = 11;
    private String name;
    private String mistakeId;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isAccessibility() {
        return accessibility;
    }

    public void setAccessibility(boolean accessibility) {
        this.accessibility = accessibility;
    }

    public boolean isHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // конструктор для sql - содержит последнюю загруженную версию
    public Course(String id, int version, boolean hasAccess, boolean isEnabled, int difficulty, boolean accessibility, String name) {
        this.version = version;
        this.hasAccess = hasAccess;
        this.id = id;
        this.isEnabled = isEnabled;
        this.difficulty = difficulty;
        this.accessibility = accessibility;
        this.name = name;
    }

    public Course() {
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMistakeId() {
        return mistakeId;
    }

    public void setMistakeId(String mistakeId) {
        this.mistakeId = mistakeId;
    }
}
