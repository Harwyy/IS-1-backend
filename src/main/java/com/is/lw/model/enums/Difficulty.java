package com.is.lw.model.enums;

public enum Difficulty {
    EASY("EASY"),
    NORMAL("NORMAL"),
    HARD("HARD"),
    TERRIBLE("TERRIBLE");

    public final String str;

    Difficulty(String str) {
        this.str = str;
    }
}
