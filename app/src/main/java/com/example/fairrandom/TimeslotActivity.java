package com.example.fairrandom;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fairrandom.beans.Session;
import com.example.fairrandom.beans.Timeslot;
import com.example.fairrandom.retrofit.RetrofitService;
import com.example.fairrandom.retrofit.TimeslotAPI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeslotActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // create Session variable
    Session session;
    // create view variables
    Button submitButton;
    TextView startTimeText, endTimeText, playersText;
    Spinner timeslotSpinner;
    // local variables
    List<Timeslot> timeslots;
    ArrayAdapter<Timeslot> timeslotSpinnerAdapter;
    Timeslot selectedTimeslot;
    // retrofit service variable
    RetrofitService retrofitService;
    // API variable
    TimeslotAPI timeslotAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_timeslot);
        // initialize local variables and set up listeners
        initializeActivity();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeActivity() {
        // get session
        getSession();
        // assign the views
        getViews();
        // set API variables
        setAPI();
        // set local variables
        setLocalVariables();
        // set listeners
        setListeners();
    }


    private void getSession() {
        Intent intent = getIntent();
        try {
            session = intent.getParcelableExtra("session");
        } catch (Exception e) {
            Intent nextintent = new Intent(this, MainActivity.class);
            startActivity(nextintent);
        }
    }

    private void getViews() {
        submitButton = findViewById(R.id.submitButton);
        startTimeText = findViewById(R.id.startTimeText);
        endTimeText = findViewById(R.id.endTimeText);
        playersText = findViewById(R.id.playersText);
        timeslotSpinner = findViewById(R.id.timeslotSpinner);
    }

    private void setAPI() {
        retrofitService = new RetrofitService();
        timeslotAPI = retrofitService.getRetrofit().create(TimeslotAPI.class);
    }

    private void setLocalVariables() {
        timeslots = new ArrayList<>();
        // make API call to get timeslots
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeslotAPI.getTimeslotsNearDateTime(LocalDateTime.now())
                    .enqueue(new Callback<>() {

                        @Override
                        public void onResponse(Call<List<Timeslot>> call, Response<List<Timeslot>> response) {
                            if (response.isSuccessful()) {
                                if (response.body().size() != 0) {
                                    timeslots = response.body();
                                    // set spinner on item click
                                    timeslotSpinnerAdapter = new ArrayAdapter<Timeslot>(
                                            TimeslotActivity.this,
                                            android.R.layout.simple_spinner_item,
                                            timeslots
                                    );
                                    // set dropdown layout
                                    timeslotSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                                    // set adapter
                                    timeslotSpinner.setAdapter(timeslotSpinnerAdapter);
                                    //set spinner onItemSelected
                                    timeslotSpinner.setOnItemSelectedListener(TimeslotActivity.this);
                                } else {
                                    Toast.makeText(TimeslotActivity.this, getString(R.string.NoTimeslotsFoundNearNow), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // display error message
                                Toast.makeText(TimeslotActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<List<Timeslot>> call, Throwable t) {
                            Toast.makeText(TimeslotActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void setListeners() {
        // submit button click
        submitButton.setOnClickListener(view -> {
            // check if timeslot selected
            if (selectedTimeslot != null) {
                // set session timeslot
                session.setTimeslot(selectedTimeslot);
                // go to setup Activity
                Intent intent = new Intent(this, SetupActivity.class);
                intent.putExtra("session", session);
                startActivity(intent);
            } else {
                Toast.makeText(TimeslotActivity.this, getString(R.string.NoTimeslotsSelected), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedTimeslot = timeslots.get(i);
        updateTimeslotDisplay(selectedTimeslot);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void updateTimeslotDisplay(Timeslot timeslot) {
        // update start time text
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startTimeText.setText(timeslot.getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            // update end time text
            endTimeText.setText((timeslot.getEndTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
        }
        // update expected players number
        // make API call
        timeslotAPI.getPlayerCountFromTimeslotId(timeslot.getId())
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        // check if response succeeds
                        if (response.isSuccessful()) {
                            // get response value
                            int playerCount = response.body();
                            // display value
                            playersText.setText(String.valueOf(playerCount));
                        } else {
                            // display error message
                            playersText.setText("0");
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(TimeslotActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}