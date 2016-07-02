package com.example.deyanpeev.quiztime.helpers;

public class PasswordValidator {
    private static final String SEED_DATA_PASSWORD = "deyan";

    public static boolean IsPasswordCorrect(String password){
        return password.equals(SEED_DATA_PASSWORD);
    }
}
