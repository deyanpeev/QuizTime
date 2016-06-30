package com.example.deyanpeev.quiztime.fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deyanpeev.quiztime.R;
import com.example.deyanpeev.quiztime.data.StoreDbHelper;
import com.example.deyanpeev.quiztime.models.InterestingFactModel;

import java.util.List;

public class CreateNewInterestingFact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_interesting_fact);
    }

    public void createNewInteresingFact(View view) {

        InterestingFactModel interesingFact = new InterestingFactModel((((EditText) findViewById(R.id.teShortTag)).getText().toString()),
                (((EditText) findViewById(R.id.teDescription)).getText().toString()));

        StoreDbHelper dbHelper = new StoreDbHelper(getApplicationContext());

        if(!dbHelper.doesInterestingFactExist(interesingFact)){
            dbHelper.insertNewInterestingFact(interesingFact);
            Toast.makeText(getApplicationContext(), "The interesting fact was successfully created.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "An interesting fact with that tag already exists.", Toast.LENGTH_LONG).show();
        }
    }

    public void testMe(View view) {
        StoreDbHelper dbHelper = new StoreDbHelper((getApplicationContext()));
        List<String> test = dbHelper.getAllInterestingFactTags();
    }
}
