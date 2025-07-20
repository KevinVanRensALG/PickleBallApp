package com.example.fairrandom.beans;

public class Court {
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
}
