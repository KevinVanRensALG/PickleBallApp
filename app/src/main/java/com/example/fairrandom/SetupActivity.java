package com.example.fairrandom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fairrandom.beans.Session;

public class SetupActivity extends AppCompatActivity {

    // create Session variable
    Session session;
    // create view variables
    EditText playersEdit, courtEdit;
    Button submitButton;
    TextView errorMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setup);

        // set view variables
        submitButton = findViewById(R.id.submitButton);
        playersEdit = findViewById(R.id.playersNumberEditText);
        courtEdit = findViewById(R.id.courtNumberEditText);
        errorMessageTextView = findViewById(R.id.errorTextView);

        // get intent
        Intent intent = getIntent();

        // set Session
        session = intent.getParcelableExtra("session");

        submitButton.setOnClickListener(view -> {
            // Check for values in the EditTexts
            if(inputValueIsEmpty()||inputValueLow()){
                // Display error message
                createErrorMessage();
            } else {
                // get values from inputs
                int playerNumber = Integer.parseInt(playersEdit.getText().toString());
                int courtNumber = Integer.parseInt(courtEdit.getText().toString());
                // pass values to new activity
                Intent nextintent = new Intent(SetupActivity.this, CourtActivity.class);
                nextintent.putExtra("numberOfPlayers", playerNumber);
                nextintent.putExtra("numberOfCourts", courtNumber);
                nextintent.putExtra("session", session);
                startActivity(nextintent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean inputValueLow() {
        // check for minimum number of players and courts
        boolean lowCheck;
        lowCheck = getCourtEditValue() <= 0 || getPlayerEditValue() <= 4;
        return  lowCheck;
    }

    private void createErrorMessage() {
        // create error message variables
        String errorMessage = getString(R.string.error) + " %s %s";
        String errorMessagePlayer = "";
        String errorMessageCourt = "";

        // set error message variables
        if(playersEdit.getText().toString().isEmpty()){
            errorMessagePlayer = getString(R.string.errorPlayerNumber);
        } else if (getPlayerEditValue()<=4) {
            errorMessagePlayer  = getString(R.string.errorPlayerNumberLow);
        }
        if(courtEdit.getText().toString().isEmpty()){
            errorMessageCourt = getString(R.string.errorCourtNumber);
        } else if (getCourtEditValue()<=0) {
            errorMessagePlayer  = getString(R.string.errorCourtNumberLow);
        }
        // format error message
        errorMessage = String.format(errorMessage, errorMessagePlayer, errorMessageCourt);
        // display error message
        errorMessageTextView.setText(errorMessage);
        errorMessageTextView.setVisibility(View.VISIBLE);
    }

    private int getCourtEditValue() {
        return Integer.parseInt(courtEdit.getText().toString());
    }

    private int getPlayerEditValue() {
        return Integer.parseInt(playersEdit.getText().toString());
    }

    private boolean inputValueIsEmpty() {
        return (playersEdit.getText().toString().isEmpty()||courtEdit.getText().toString().isEmpty());
    }


}