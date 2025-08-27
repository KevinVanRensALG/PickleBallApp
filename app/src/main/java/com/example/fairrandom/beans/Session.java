package com.example.fairrandom.beans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;

public class Session implements Parcelable {

    private ArrayList<Player> players;
    private ArrayList<Player> availablePlayers;
    private ArrayList<Court> courts;
    private int leastGamesPlayed;

    private Timeslot timeslot;

    public Session() {
        players = new ArrayList<>();
        availablePlayers = new ArrayList<>();
        courts = new ArrayList<>();
        leastGamesPlayed = 0;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<Court> getCourts() {
        return courts;
    }

    public void setCourts(ArrayList<Court> courts) {
        this.courts = courts;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public int getCourtCount() {
        return this.courts.size();
    }

    public int getPlayerCount() {
        int playerCount = this.players.size();
        for (Court court : courts
        ) {
            playerCount += court.getPlayerNames().length;
        }
        return playerCount;
    }

    protected Session(Parcel in) {
        players = in.createTypedArrayList(Player.CREATOR);
        availablePlayers = in.createTypedArrayList(Player.CREATOR);
        courts = in.createTypedArrayList(Court.CREATOR);
        leastGamesPlayed = in.readInt();
        timeslot = in.readParcelable(Timeslot.class.getClassLoader());
    }

    public static final Creator<Session> CREATOR = new Creator<>() {
        @Override
        public Session createFromParcel(Parcel in) {
            return new Session(in);
        }

        @Override
        public Session[] newArray(int size) {
            return new Session[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeTypedList(players);
        parcel.writeTypedList(availablePlayers);
        parcel.writeTypedList(courts);
        parcel.writeInt(leastGamesPlayed);
        parcel.writeParcelable(timeslot, i);
    }

    public ArrayList<String> getPlayerNames() {
        // create return list
        ArrayList<String> playernames = new ArrayList<>();
        //populate list
        for (Player player : players
        ) {
            playernames.add(player.getFirstName());
        }
        for (Court court : courts
        ) {
            playernames.addAll(Arrays.asList(court.getPlayerNames()));
        }
        // return list
        return playernames;
    }

    public ArrayList<String> getCourtNames() {
        // create return list
        ArrayList<String> courtnames = new ArrayList<>();
        //populate list
        for (Court court : courts
        ) {
            courtnames.add(court.getName());
        }
        // return list
        return courtnames;
    }

    public int getLeastGamesPlayed() {
        return leastGamesPlayed;
    }

    public void setLeastGamesPlayed(int leastGamesPlayed) {
        this.leastGamesPlayed = leastGamesPlayed;
    }

    public ArrayList<Player> getAvailablePlayers() {
        return availablePlayers;
    }

    public void setAvailablePlayers(ArrayList<Player> availablePlayers) {
        this.availablePlayers = availablePlayers;
    }

    public ArrayList<Player> getALLPlayers() {
        ArrayList<Player> playersList = new ArrayList<>(this.getPlayers());
        for (Court court : courts
        ) {
            if (court.getPlayers() != null) {
                playersList.addAll(Arrays.asList(court.getPlayers()));
            }
        }
        return playersList;
    }
}
