package com.example.deyanpeev.quiztime.fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.deyanpeev.quiztime.R;
import com.example.deyanpeev.quiztime.helpers.DifficultyLevel;

public class GamePlay extends AppCompatActivity {

    //TODO get and store all random questions
    //TODO foreach question get 3 random answers place them in list and the shuffle

    private String difficultyLevel;
    int numberOfFreeQuestions;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        this.getBundles();
        this.numberOfFreeQuestions = DifficultyLevel.getNumberOfFreeQuestions(this.difficultyLevel);
    }

    private void getBundles(){
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            throw new IllegalStateException("There is no category and difficulty levels set.");
        }

        this.difficultyLevel = bundle.getString(getResources().getString(R.string.difficulty_level_bundle));
        this.category = bundle.getString(getResources().getString(R.string.category_bunde));
    }
}
