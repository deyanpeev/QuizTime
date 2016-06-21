package com.example.deyanpeev.quiztime.helpers.enums;

public enum DifficultyLevel {

    EASY (8), MEDIUM(5), HARD(1);

    private final int DifficultyValue;

    DifficultyLevel(int level){
        this.DifficultyValue = level;
    }

    public int getDifficultyValue() {
        return this.DifficultyValue;
    }
}
