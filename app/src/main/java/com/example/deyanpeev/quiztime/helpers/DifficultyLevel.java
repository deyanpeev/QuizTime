package com.example.deyanpeev.quiztime.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dpeev on 7/3/16.
 */
public class DifficultyLevel {
    private static List<String> difficultyLevels = Arrays.asList("Easy", "Medium", "Hard");

    public static List<String> getDifficultyLevels(){
        return difficultyLevels;
    }

    public static int getNumberOfFreeQuestions(String difficultyLevel){
        switch (difficultyLevel.toUpperCase()){
            case "EASY":
                return 5;
            case "MEDIUM":
                return 2;
            case "HARD":
                return 0;
            default:
                return 0;
        }
    }
}
