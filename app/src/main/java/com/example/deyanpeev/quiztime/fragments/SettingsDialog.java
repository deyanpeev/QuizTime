package com.example.deyanpeev.quiztime.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import com.example.deyanpeev.quiztime.R;
import com.example.deyanpeev.quiztime.helpers.PasswordValidator;

public class SettingsDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.activity_settings_dialog, null))
                // Add action buttons
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText etPassword = (EditText) ((AlertDialog) dialog).findViewById(R.id.etPassword);
                        String password = etPassword.getText().toString();
                        if(PasswordValidator.isPasswordCorrect(password)){
                            Intent goToSettingsActivity = new Intent(getActivity(), SettingsActivity.class);
                            startActivity(goToSettingsActivity );
                        } else{
                            Toast.makeText(getActivity().getApplicationContext(), "The password does not match the original one.", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
