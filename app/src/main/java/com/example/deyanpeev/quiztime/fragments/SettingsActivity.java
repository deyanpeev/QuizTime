package com.example.deyanpeev.quiztime.fragments;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.deyanpeev.quiztime.R;
import com.example.deyanpeev.quiztime.data.StoreDbHelper;
import com.example.deyanpeev.quiztime.helpers.Seeder;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = getSharedPreferences(getResources().getString(R.string.settings), MODE_PRIVATE);
    }

    public void cleanDatabase(View view) {
        if(preferences.getBoolean(getResources().getString(R.string.database_cleared), false)){
            Toast.makeText(getApplicationContext(), "Database already cleared.", Toast.LENGTH_LONG).show();
            return;
        }

        StoreDbHelper dbHelper = new StoreDbHelper(this.getApplicationContext());
        dbHelper.clearAllTables();

        //Setting shared preferences
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.putBoolean(getResources().getString(R.string.seeded_categories), false).commit();
        preferencesEditor.putBoolean(getResources().getString(R.string.seeded_answers), false).commit();
        preferencesEditor.putBoolean(getResources().getString(R.string.seeded_interesing_facts), false).commit();
        preferencesEditor.putBoolean(getResources().getString(R.string.seeded_questions), false).commit();
        preferencesEditor.putBoolean(getResources().getString(R.string.database_cleared), true).commit();

        Toast.makeText(getApplicationContext(), "Database successfully cleared.", Toast.LENGTH_LONG).show();
    }

    public void seedCategories(View view) {
        if(preferences.getBoolean(getResources().getString(R.string.seeded_categories), false)){
            Toast.makeText(getApplicationContext(),"Categories already seeded", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Seeder seeder = new Seeder(this.getApplicationContext());
            seeder.seedCategories();
            Toast.makeText(getApplicationContext(), "The category successfully inserted.", Toast.LENGTH_LONG).show();

            preferences.edit().putBoolean(getResources().getString(R.string.seeded_categories), true).commit();
            preferences.edit().putBoolean(getResources().getString(R.string.database_cleared), false).commit();
        } catch(Exception e){
            Toast.makeText(getApplicationContext(), "Failed to insert categories.", Toast.LENGTH_LONG).show();
        }
    }

    public void seedInterestingFacts(View view) {
        if(preferences.getBoolean(getResources().getString(R.string.seeded_interesing_facts), false)){
            Toast.makeText(getApplicationContext(), "Interesting facts already seeded", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Seeder seeder = new Seeder(this.getApplicationContext());
            seeder.seedInterestingFacts();
            Toast.makeText(getApplicationContext(), "Interesting facts successfully inserted.", Toast.LENGTH_LONG).show();

            preferences.edit().putBoolean(getResources().getString(R.string.seeded_interesing_facts), true).commit();
            preferences.edit().putBoolean(getResources().getString(R.string.database_cleared), false).commit();
        } catch(Exception e){
            Toast.makeText(getApplicationContext(), "Failed to insert interesting facts.", Toast.LENGTH_LONG).show();
        }
    }

    public void seedAnswers(View view) {
        if(preferences.getBoolean(getResources().getString(R.string.seeded_answers), false)){
            Toast.makeText(getApplicationContext(), "Answers already seeded", Toast.LENGTH_LONG).show();
            return;
        }

        if(!preferences.getBoolean(getResources().getString(R.string.seeded_categories), false)){
            Toast.makeText(getApplicationContext(), "Categories must be seeded first", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Seeder seeder = new Seeder(this.getApplicationContext());
            seeder.seedAnswers();
            Toast.makeText(getApplicationContext(), "Answers successfully inserted.", Toast.LENGTH_LONG).show();

            preferences.edit().putBoolean(getResources().getString(R.string.seeded_answers), true).commit();
            preferences.edit().putBoolean(getResources().getString(R.string.database_cleared), false).commit();
        } catch(Exception e){
            Toast.makeText(getApplicationContext(), "Failed to insert answers.", Toast.LENGTH_LONG).show();
        }
    }

    public void seedQuestions(View view) {
        if(preferences.getBoolean(getResources().getString(R.string.seeded_questions), false)){
            Toast.makeText(getApplicationContext(), "Questions already seeded", Toast.LENGTH_LONG).show();
            return;
        }

        if(!preferences.getBoolean(getResources().getString(R.string.seeded_categories), false)){
            Toast.makeText(getApplicationContext(), "Categories must be seeded first", Toast.LENGTH_LONG).show();
            return;
        }

        if(!preferences.getBoolean(getResources().getString(R.string.seeded_answers), false)){
            Toast.makeText(getApplicationContext(), "Answers must be seeded first", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Seeder seeder = new Seeder(this.getApplicationContext());
            seeder.seedQuestions();
            Toast.makeText(getApplicationContext(), "Questions successfully inserted.", Toast.LENGTH_LONG).show();

            preferences.edit().putBoolean(getResources().getString(R.string.seeded_questions), true).commit();
            preferences.edit().putBoolean(getResources().getString(R.string.database_cleared), false).commit();
        } catch(Exception e){
            Toast.makeText(getApplicationContext(), "Failed to insert questions.", Toast.LENGTH_LONG).show();
        }
    }
}
