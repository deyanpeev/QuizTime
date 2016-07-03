package com.example.deyanpeev.quiztime.fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.deyanpeev.quiztime.R;
import com.example.deyanpeev.quiztime.data.StoreDbHelper;
import com.example.deyanpeev.quiztime.helpers.DifficultyLevel;

import java.util.List;

public class ChooseGame extends AppCompatActivity {

    private Spinner spinnerCategories;
    private Spinner spinnerDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);

        this.spinnerCategories = (Spinner) findViewById(R.id.spinnerCategories);
        this.spinnerDifficulty = (Spinner) findViewById(R.id.spinnerDifficulty);

        this.initializeSpinners();
    }

    private void initializeSpinners(){
        this.initializeCategorySpinner();
        this.initializeDifficultySpinner();
    }

    private void initializeCategorySpinner(){
        StoreDbHelper db = new StoreDbHelper(getApplicationContext());

        List<String> playableCategories = db.getAllPlayableCategories();
        playableCategories.add(0, getResources().getString(R.string.all_elements));

        ArrayAdapter<String> dataAdater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playableCategories);

        this.spinnerCategories.setAdapter(dataAdater);
    }

    private void initializeDifficultySpinner(){
        StoreDbHelper db = new StoreDbHelper(getApplicationContext());

        List<String> playableCategories = DifficultyLevel.getDifficultyLevels();

        ArrayAdapter<String> dataAdater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, playableCategories);

        this.spinnerDifficulty.setAdapter(dataAdater);
    }

    public void goToGamePlay(View view) {
        String difficultyLevel = this.spinnerDifficulty.getSelectedItem().toString();
        String category = this.spinnerCategories.getSelectedItem().toString();

        Intent goToStartNewCategoryActivity = new Intent(this, GamePlay.class);
        Bundle bundle = new Bundle();
        bundle.putString(getResources().getString(R.string.difficulty_level_bundle), difficultyLevel);
        bundle.putString(getResources().getString(R.string.category_bunde), category);
        goToStartNewCategoryActivity.putExtras(bundle);

        startActivity(goToStartNewCategoryActivity );
    }
}
