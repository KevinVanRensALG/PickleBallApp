package com.example.fairrandom;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    ArrayList<String> playerNames, newPlayerNames;

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

        // toolbar setup
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


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
        // set on item click listener to remove players
        playerListView.setOnItemClickListener((adapterView, view, i, l) -> {
            // check if player is playing
            boolean playerOnCourt = false;
            for (Court court: session.getCourts()
            ) {
                if (court.getPlayers() != null){
                    for (Player player: court.getPlayers()
                    ) {
                        if (player.getName().equals(playerNames.get(i))){
                            Toast toast = Toast.makeText(SetupActivity.this, getString(R.string.errorPlayerOnCourt), Toast.LENGTH_LONG);
                            toast.show();
                            playerOnCourt = true;
                            break;
                        }
                    }
                }
            }
            if (!playerOnCourt){
                // remove from session list
                for (Player player: session.getPlayers()
                ) {
                    if (player.getName().equals(playerNames.get(i))){
                        session.getPlayers().remove(player);
                        break;
                    }
                }
                newPlayerNames.remove(playerNames.get(i));
                // remove from adapter list
                playerNames.remove(i);
                playerNames.trimToSize();
                // update ListView
                playerListAdapter.notifyDataSetChanged();
            }
        });

        // set courts number
        if (session.getCourts().isEmpty()){
            courtEdit.setText("");
        } else {
            courtEdit.setText(String.valueOf(session.getCourtCount()));
        }

        newPlayerNames = new ArrayList<>();
        // add player button
        newPlayerButton.setOnClickListener(view -> {
            // check if name input is empty
            if (!newPlayerEdit.getText().toString().isEmpty()){
                // if not add player name to player list
                playerNames.add(newPlayerEdit.getText().toString());
                // add player name to new player name list
                newPlayerNames.add(newPlayerEdit.getText().toString());
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
                ArrayList<Court> courtsList = session.getCourts();
                if (Integer.parseInt(courtEdit.getText().toString())>session.getCourtCount()){
                    for(int i = session.getCourtCount(); i<Integer.parseInt(courtEdit.getText().toString()); i++){
                        courtsList.add(new Court(getString(R.string.court)+" "+(i+1)));
                    }
                } else if (Integer.parseInt(courtEdit.getText().toString())<session.getCourtCount()) {
                    ArrayList<Court> tempList = new ArrayList<>();
                    for (int i = 0; i < session.getCourtCount(); i++) {
                        //if court remaining
                        if (i<Integer.parseInt(courtEdit.getText().toString())){
                            tempList.add(session.getCourts().get(i));
                        } else {
                            for (Player player:session.getCourts().get(i).getPlayers()
                                 ) {
                                session.getPlayers().add(player);
                            }
                        }
                    }
                    session.setCourts(tempList);
                }
                session.setCourts(courtsList);
                // add new players to session
                for (String playerName:newPlayerNames
                     ) {
                    session.getPlayers().add(new Player(playerName, session.getLeastGamesPlayed()));
                    session.getAvailablePlayers().add(new Player(playerName));
                }
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
        } else if (Integer.parseInt(courtEdit.getText().toString())<1) {
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
        if(session.getPlayerCount()+newPlayerNames.size()<4){
            errorMessage = getString(R.string.errorPlayerNumberLow);
        } else {
            errorMessage = "";
        }
        try {
            if (session.getPlayerCount()+newPlayerNames.size()<(4*Integer.parseInt(courtEdit.getText().toString()))){
                errorMessage = getString(R.string.errorPlayerNumberLowForCourt);
            }
        } catch (Exception e) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // create intent
        Intent nextintent;
        if (item.getItemId()==R.id.toolbarActionSetup){
            nextintent = new Intent(this, SetupActivity.class);
        } else if (item.getItemId()==R.id.toolbarActionCourts) {
            nextintent = new Intent(this, CourtActivity.class);
        } else if (item.getItemId()==R.id.toolbarActionHome) {
            nextintent = new Intent(this, MainActivity.class);
        } else {
            nextintent = new Intent(this, MainActivity.class);
        }
        // put the session into new intent
        nextintent.putExtra("session", session);
        //start new Activity
        startActivity(nextintent);
        return super.onOptionsItemSelected(item);
    }
}