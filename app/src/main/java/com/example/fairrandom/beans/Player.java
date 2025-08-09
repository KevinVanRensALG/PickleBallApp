package com.example.fairrandom.beans;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Player implements Parcelable {
    private String name;
    private int gamesPlayed;
    private  boolean playing;

    public Player(String name) {
        this.setName(name);
        this.setGamesPlayed(0);
        this.setPlaying(false);
    }

    public Player(String name, int leastGamesPlayed) {
        this.setName(name);
        this.setGamesPlayed(leastGamesPlayed);
        this.setPlaying(false);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public void finishGame() {
        this.gamesPlayed++;
        this.setPlaying(false);
    }

    protected Player(Parcel in) {
        name = in.readString();
        gamesPlayed = in.readInt();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            playing = in.readBoolean();
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            parcel.writeBoolean(playing);
        }
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }
}
