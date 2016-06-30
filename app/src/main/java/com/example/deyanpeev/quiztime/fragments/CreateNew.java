package com.example.deyanpeev.quiztime.fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.deyanpeev.quiztime.R;

public class CreateNew extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);
    }

    public void goToCreateNewQuestion(View view) {
        Intent goToStartNewCategoryActivity = new Intent(this, CreateNewQuestion.class);
        startActivity(goToStartNewCategoryActivity );
    }

    public void goToCreateNewCategory(View view) {
        Intent goToStartNewCategoryActivity = new Intent(this, CreateNewCategory.class);
        startActivity(goToStartNewCategoryActivity );
    }

    public void goToCreateNewInterestingFact(View view) {
        Intent goToStartNewInterestingFactActivity = new Intent(this, CreateNewInterestingFact.class);
        startActivity(goToStartNewInterestingFactActivity );
    }
}
