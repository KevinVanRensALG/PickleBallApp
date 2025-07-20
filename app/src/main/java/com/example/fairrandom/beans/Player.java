package com.example.fairrandom.beans;

public class Player {
    private String name;
    private int gamesPlayed;

    public Player(String name) {
        this.setName(name);
        this.setGamesPlayed(0);
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
}
