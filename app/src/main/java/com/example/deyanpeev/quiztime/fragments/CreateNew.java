package com.example.deyanpeev.quiztime.fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.deyanpeev.quiztime.R;
import com.example.deyanpeev.quiztime.data.StoreDbHelper;

public class CreateNew extends AppCompatActivity {

    private StoreDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);

        dbHelper = new StoreDbHelper(getApplicationContext());
    }

    public void goToCreateNewQuestion(View view) {
        if(!dbHelper.isThereCategories()){
            Toast.makeText(getApplicationContext(), "At least one category must exist first.", Toast.LENGTH_LONG).show();
            return;
        }

        Intent goToStartNewCategoryActivity = new Intent(this, CreateNewQuestion.class);
        startActivity(goToStartNewCategoryActivity );
    }

    public void goToCreateNewCategory(View view) {
        Intent goToStartNewCategoryActivity = new Intent(this, CreateNewCategory.class);
        startActivity(goToStartNewCategoryActivity );
    }

    public void goToCreateNewInterestingFact(View view) {
        if(!dbHelper.isThereCategories()){
            Toast.makeText(getApplicationContext(), "At least one category must exist first.", Toast.LENGTH_LONG).show();
            return;
        }

        Intent goToStartNewInterestingFactActivity = new Intent(this, CreateNewInterestingFact.class);
        startActivity(goToStartNewInterestingFactActivity );
    }
}
