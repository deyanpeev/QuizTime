package com.example.deyanpeev.quiztime.fragments;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.deyanpeev.quiztime.R;
import com.example.deyanpeev.quiztime.data.StoreDbHelper;
import com.example.deyanpeev.quiztime.helpers.Constants;

public class InitialPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        XmlResourceParser xrp = getBaseContext().getResources().getXml(R.xml.categories);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_initial_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public void goToCreateNewQuestionOrCategory(View view){
        Intent goToCreateNewQuestionOrCategory = new Intent(this, CreateNew.class);
        startActivity(goToCreateNewQuestionOrCategory );
    }

    public void goToSettings(MenuItem item) {
        DialogFragment newFragment = new SettingsDialog();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        newFragment.show(ft, "Password confirmation");
    }
}
