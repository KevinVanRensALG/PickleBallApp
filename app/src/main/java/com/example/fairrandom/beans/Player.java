package com.example.fairrandom.beans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Player implements Parcelable {
    private String name;
    private int gamesPlayed;

    public Player(String name) {
        this.setName(name);
        this.setGamesPlayed(0);
    }

    public Player(String name, int leastGamesPlayed) {
        this.setName(name);
        this.setGamesPlayed(leastGamesPlayed);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void finishGame() {
        this.gamesPlayed++;
    }

    protected Player(Parcel in) {
        name = in.readString();
        gamesPlayed = in.readInt();
    }

    public static final Creator<Player> CREATOR = new Creator<>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(gamesPlayed);
    }
}
