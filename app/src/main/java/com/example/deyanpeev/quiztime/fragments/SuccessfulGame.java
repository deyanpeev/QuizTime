package com.example.deyanpeev.quiztime.fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.deyanpeev.quiztime.R;
import com.example.deyanpeev.quiztime.data.StoreDbHelper;

public class SuccessfulGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_game);
    }

    public void goToChooseGameActivity(View view) {
        StoreDbHelper db = new StoreDbHelper(getApplicationContext());

        if(!db.couldRunGame()){
            Toast.makeText(getApplicationContext(), "There are not enough questions.", Toast.LENGTH_LONG).show();
            return;
        }

        Intent goToStartNewGameActivity = new Intent(this, ChooseGame.class);
        startActivity(goToStartNewGameActivity );
    }
}
