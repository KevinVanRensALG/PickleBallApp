package com.example.fairrandom.beans;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Court implements Parcelable {
    private String name;
    private Player[] players;

    public Court(String name){
        this.setName(name);
    }

    public Court(String name, String playerName){
        this.setName(name);
        this.setPlayers(new Player[3]);
        for (Player player:this.players
        ) {
            player.setName(playerName);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public String[] getPlayerNames(){
        String[] playerNames = new String[this.players.length];
        for(int i=0; i <this.players.length ; i++){
            playerNames[i] = this.players[i].getName();
        }
        return playerNames;
    }

    public void setEmpty(){
        this.setPlayers(null);
    }

    protected Court(Parcel in) {
        name = in.readString();
        players = in.createTypedArray(Player.CREATOR);
    }

    public static final Creator<Court> CREATOR = new Creator<Court>() {
        @Override
        public Court createFromParcel(Parcel in) {
            return new Court(in);
        }

        @Override
        public Court[] newArray(int size) {
            return new Court[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeTypedArray(players, i);
    }
}
