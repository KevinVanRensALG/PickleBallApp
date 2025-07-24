package com.example.fairrandom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fairrandom.beans.Court;
import com.example.fairrandom.beans.Player;
import com.example.fairrandom.beans.Session;

import java.util.ArrayList;

public class SetupActivity extends AppCompatActivity {

    // create Session variable
    Session session;
    // create view variables
    EditText newPlayerEdit, courtEdit;
    Button submitButton, newPlayerButton;
    TextView errorMessageTextView;
    ListView playerListView;

    // local variables
    ArrayList<String> playerNames;

    ArrayAdapter<String> playerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setup);

        // set view variables
        submitButton = findViewById(R.id.submitButton);
        newPlayerButton = findViewById(R.id.newPlayerButton);
        newPlayerEdit = findViewById(R.id.newPlayerEditText);
        courtEdit = findViewById(R.id.courtNumberEditText);
        errorMessageTextView = findViewById(R.id.errorTextView);
        playerListView = findViewById(R.id.playersList);

        // get intent
        Intent intent = getIntent();

        // set Session
        try {
            session = intent.getParcelableExtra("session");
        } catch (Exception e) {
            Intent nextintent = new Intent(SetupActivity.this, MainActivity.class);
            startActivity(nextintent);
        }

        // set up listview
        assert session != null;
        playerNames = session.getPlayerNames();
        playerListAdapter = new ArrayAdapter<>(this, R.layout.activity_player_list_view, playerNames);
        playerListView.setAdapter(playerListAdapter);
        playerListAdapter.notifyDataSetChanged();

        // set courts number
        if (session.getCourts().isEmpty()){
            courtEdit.setText("");
        } else {
            courtEdit.setText(session.getCourtCount());
        }

        // add player button
        newPlayerButton.setOnClickListener(view -> {
            // check if name input is empty
            if (!newPlayerEdit.getText().toString().isEmpty()){
                // if not add player name to player list
                playerNames.add(newPlayerEdit.getText().toString());
                // update session
                session.getPlayers().add(new Player(newPlayerEdit.getText().toString(),session.getLeastGamesPlayed()));
                // update ListView
                playerListAdapter.notifyDataSetChanged();
                // clear text
                newPlayerEdit.setText("");
            } else {
                // if empty
                // display error
                createErrorMessage(newPlayerErrorMessage(),"");
            }
        });



        submitButton.setOnClickListener(view -> {
            // Check for at errors in inputs
            if(!isErrors()){
                // add courts to session
                // create new list of courts
                ArrayList<Court> courtsList = new ArrayList<>();
                for(int i = 0; i<Integer.parseInt(courtEdit.getText().toString()); i++){
                    courtsList.add(new Court(getString(R.string.court)+" "+(i+1)));
                }
                session.setCourts(courtsList);
                // create new intent
                Intent nextintent = new Intent(SetupActivity.this, CourtActivity.class);
                // put the session into new intent
                nextintent.putExtra("session", session);
                //start new Activity
                startActivity(nextintent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean isErrors() {
        // get error messages about  players
        String playerErrorMessage = getPlayerErrors();
        // get error messages about courts
        String courtErrorMessage = getCourtErrors();
        //check if there were any errors
        // set boolean
        boolean errors = (!playerErrorMessage.isEmpty()||!courtErrorMessage.isEmpty());
        // if there were
        if (errors){
            //update error Message TextView
            createErrorMessage(playerErrorMessage,courtErrorMessage);
        }
        //return if there were errors
        return errors;
    }

    private String getCourtErrors() {
        // string to return
        String errorMessage;
        // check if there are less then 1 court being played on
        if(courtEdit.getText().toString().isEmpty()){
            errorMessage = getString(R.string.errorCourtNumberLow);
        } else if (Integer.parseInt(courtEdit.getText().toString())>1) {
            errorMessage = getString(R.string.errorCourtNumberLow);
        } else  {
            errorMessage = "";
        }
        return errorMessage;
    }

    private String getPlayerErrors() {
        // string to return
        String errorMessage;
        // check if there are less then 5 players playing
        if(session.getPlayers().size()<5){
            errorMessage = getString(R.string.errorPlayerNumberLow);
        } else {
            errorMessage = "";
        }
        return errorMessage;
    }

    private String newPlayerErrorMessage() {
        // if empty
        if (newPlayerEdit.getText().toString().isEmpty()){
            return getString(R.string.errorPlayerNameEmpty);
        }
        return "";
    }

    private void createErrorMessage(String playerMessage, String courtMessage) {
        // create error message variables
        String errorMessage = getString(R.string.error) + " %s %s";
        // format error message
        errorMessage = String.format(errorMessage, playerMessage, courtMessage);
        // display error message
        errorMessageTextView.setText(errorMessage);
        errorMessageTextView.setVisibility(View.VISIBLE);
    }


}