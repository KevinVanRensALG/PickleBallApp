package com.example.fairrandom.retrofit;

import com.example.fairrandom.beans.Player;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TimeslotAPI {

    @GET("/{playerId}/timeslot/{timeslotId}")
    Call<Player> getPlayerFromTimeslotById(@Path("playerId") int playerId, @Path("timeslotId") int timeslotId);
}
