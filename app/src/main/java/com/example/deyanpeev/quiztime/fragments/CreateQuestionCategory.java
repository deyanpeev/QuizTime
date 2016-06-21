package com.example.deyanpeev.quiztime.fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.deyanpeev.quiztime.R;

public class CreateQuestionCategory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question_category);
    }

    public void goToCreateNewQuestion(View view) {

    }

    public void goToCreateNewCategory(View view) {
        Intent goToStartNewCategoryActivity = new Intent(this, CreateNewCategory.class);
        startActivity(goToStartNewCategoryActivity );
    }
}
