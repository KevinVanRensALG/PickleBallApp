package com.example.fairrandom.retrofit;

import com.example.fairrandom.beans.Player;
import com.example.fairrandom.beans.Timeslot;

import java.time.LocalDateTime;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TimeslotAPI {

    @GET("/{playerId}/timeslot/{timeslotId}")
    Call<Player> getPlayerFromTimeslotById(@Path("playerId") int playerId, @Path("timeslotId") int timeslotId);

    @GET("/timeslot/near/{time}")
    Call<List<Timeslot>> getTimeslotsNearDateTime(@Path("time") LocalDateTime now);

    @GET("/timeslot/{timeslotId}/count")
    Call<Integer> getPlayerCountFromTimeslotId(@Path("timeslotId") int timeslotId);
}
