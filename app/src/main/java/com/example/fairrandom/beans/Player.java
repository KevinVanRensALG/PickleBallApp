package com.example.fairrandom.beans;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Player implements Parcelable {
    private String firstName, lastName;
    private int gamesPlayed, id;
    private  boolean playing;

    public Player(String firstName) {
        this.setFirstName(firstName);
        this.setGamesPlayed(0);
        this.setPlaying(false);
    }

    public Player(String firstName, int leastGamesPlayed) {
        this.setFirstName(firstName);
        this.setGamesPlayed(leastGamesPlayed);
        this.setPlaying(false);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return firstName+" "+lastName;
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
        firstName = in.readString();
        lastName = in.readString();
        id = in.readInt();
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
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeInt(id);
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
