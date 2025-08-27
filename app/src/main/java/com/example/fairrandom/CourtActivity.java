package com.example.fairrandom;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.example.fairrandom.services.FairRandomCourtGeneratorService;

import java.util.ArrayList;
import java.util.HashMap;

public class CourtActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // session variable
    Session session;

    // get views
    Spinner courtSpinner;
    Button generateButton, cancelButton;
    TextView playerOneTextVew, playerTwoTextView, playerThreeTextVew, playerFourTextVew, courtCenterTextView;

    // create Services
    FairRandomCourtGeneratorService playerGeneratorService;

    // create List
    ArrayList<Player> playersAttending;
    ArrayList<Court> courts;
    // create HashMap
    HashMap<Integer, ArrayList<Player>> playersAvailable;
    // create Arrays
    // Current court variable
    int currentCourt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_court);


        // set Session
        // get intent
        Intent intent = getIntent();
        try {
            session = intent.getParcelableExtra("session");
        } catch (Exception e) {
            Intent nextintent = new Intent(this, MainActivity.class);
            startActivity(nextintent);
        }

        // set view variables
        courtSpinner = findViewById(R.id.courtSelectSpinner);

        generateButton = findViewById(R.id.generateButton);
        cancelButton = findViewById(R.id.cancelButton);

        playerOneTextVew = findViewById(R.id.playerOneTextView);
        playerTwoTextView = findViewById(R.id.playerTwoTextView);
        playerThreeTextVew = findViewById(R.id.playerThreeTextView);
        playerFourTextVew = findViewById(R.id.playerFourTextView);

        courtCenterTextView = findViewById(R.id.courtCenterTextView);

        // set Services
        playerGeneratorService = new FairRandomCourtGeneratorService();

        // set current court
        currentCourt = 0;

        //set HashMap
        playersAvailable = new HashMap<>();

        // set Arrays
        playersAttending = session.getPlayers();
        courts = session.getCourts();

        // populate HashMap
        HashMap<Integer, ArrayList<Player>> tempMap = new HashMap<>();
        for (Player player : session.getPlayers()
        ) {
            if (!tempMap.containsKey(player.getGamesPlayed())) {
                tempMap.put(player.getGamesPlayed(), new ArrayList<>());
            }
            tempMap.get(player.getGamesPlayed()).add(player);
        }
        playersAvailable = tempMap;

        // toolbar setup
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //create Array for spinner adapter
        ArrayList<String> courtsNames = session.getCourtNames();

        // display populate spinner
        // create adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                CourtActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                courtsNames
        );
        // set dropdown layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // set adapter
        courtSpinner.setAdapter(adapter);
        //set spinner onItemSelected
        courtSpinner.setOnItemSelectedListener(this);

        // generate button
        generateButton.setOnClickListener((view -> {
            // finish current game and generate new one
            // check if a game is being played
            if (courts.get(currentCourt).getPlayers() != null) {
                // finish game
                // update game totals
                for (Player player : courts.get(currentCourt).getPlayers()
                ) {
                    player.finishGame();
                    // update players list
                    updatePlayerList(player);
                    // remove old player list if empty
                }
                // remove empty lists from map
                cleanPlayersList();
            }
            // start new game
            // generate players
            courts.get(currentCourt).setPlayers(playerGeneratorService.generateCourtPlayers(playersAvailable));
            //update available Players
            for (Player player : courts.get(currentCourt).getPlayers()
            ) {
                playersAvailable.remove(player);
            }
            //update court
            updateCourt();
            cancelButtonCheck();
        }));

        // cancel button
        cancelButton.setOnClickListener(view -> {
            // set court to empty
            for (Player player : courts.get(currentCourt).getPlayers()
            ) {
                // update players list
                updatePlayerList(player);
                // set player playing status
                player.setPlaying(false);
            }
            courts.get(currentCourt).setEmpty();
            //update court
            updateCourt();
            cancelButtonCheck();
        });

        cancelButtonCheck();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cancelButtonCheck() {
        cancelButton.setEnabled(!courts.get(currentCourt).isEmpty());
    }

    private void cleanPlayersList() {
        // check if the list at each key in the map is empty and if so remove it
        // make new hashmap
        HashMap<Integer, Integer> tempMap = new HashMap<>();
        playersAvailable.forEach((k, v) -> {
            if (playersAvailable.get(k).isEmpty()) {
                session.setLeastGamesPlayed(k + 1);
            }
            if (v.isEmpty()) {
                tempMap.put(k, k);
            }
        });
        tempMap.forEach((k, v) -> playersAvailable.remove(v));
    }

    private void updatePlayerList(Player player) {
        // check for existing key in current player list
        if (playersAvailable.containsKey(player.getGamesPlayed())) {
            // add player to list
            playersAvailable.get(player.getGamesPlayed()).add(player);
        } else {
            // create new list list and add it to hashmap
            playersAvailable.put(player.getGamesPlayed(), new ArrayList<>());
            // add player
            playersAvailable.get(player.getGamesPlayed()).add(player);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //load court data from selected court to court representation
        //set current court
        currentCourt = i;
        // update display
        updateCourt();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //nothing needed
    }

    private void updateCourt() {
        // get court
        Court court = courts.get(currentCourt);
        courtCenterTextView.setText(court.getName());
        cancelButtonCheck();
        // get player Names
        try {
            String[] courtPlayers = court.getPlayerNames();
            // set player names
            playerOneTextVew.setText(courtPlayers[0]);
            playerTwoTextView.setText(courtPlayers[1]);
            playerThreeTextVew.setText(courtPlayers[2]);
            playerFourTextVew.setText(courtPlayers[3]);
        } catch (NullPointerException e) {
            // set empty court
            playerOneTextVew.setText(R.string.empty);
            playerTwoTextView.setText(R.string.empty);
            playerThreeTextVew.setText(R.string.empty);
            playerFourTextVew.setText(R.string.empty);
        }

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
        if (item.getItemId() == R.id.toolbarActionSetup) {
            nextintent = new Intent(this, SetupActivity.class);
        } else if (item.getItemId() == R.id.toolbarActionCourts) {
            nextintent = new Intent(this, CourtActivity.class);
        } else if (item.getItemId() == R.id.toolbarActionHome) {
            nextintent = new Intent(this, MainActivity.class);
        } else {
            nextintent = new Intent(this, MainActivity.class);
        }
        // update session
        updateSession();
        // put the session into new intent
        nextintent.putExtra("session", session);
        //start new Activity
        startActivity(nextintent);
        return super.onOptionsItemSelected(item);
    }

    private void updateSession() {
        session.setCourts(courts);
        ArrayList<Player> playersList = new ArrayList<>();
        playersAvailable.forEach((k, v) -> {
            playersList.addAll(v);
        });
        session.setPlayers(playersList);
    }
}