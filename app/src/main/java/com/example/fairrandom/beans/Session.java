package com.example.fairrandom.beans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Session  implements Parcelable {

    private ArrayList<Player> players;

    private  ArrayList<Court> courts;

   public Session(){
        players = new ArrayList<>();
        courts = new ArrayList<>();
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

    public int getCourtCount() {
        return this.courts.size();
    }

    public int getPlayerCount(){
        return this.players.size();
    }

    protected Session(Parcel in) {
        players = in.createTypedArrayList(Player.CREATOR);
        courts = in.createTypedArrayList(Court.CREATOR);
    }

    public static final Creator<Session> CREATOR = new Creator<Session>() {
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
        parcel.writeTypedList(courts);
    }
}
