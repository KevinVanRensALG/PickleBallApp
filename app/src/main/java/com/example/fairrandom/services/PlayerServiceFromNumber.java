package com.example.fairrandom.services;

import com.example.fairrandom.beans.Player;

import java.util.ArrayList;

public class PlayerServiceFromNumber implements PlayerService{


    @Override
    public ArrayList<Player> makePlayerList(Object players) {
        // cast to int
        int playersNumber = (int) players;
        // create List
        ArrayList<Player> playerList = new ArrayList<>();
        // populate List
        for (int i=0;i<playersNumber;i++){
            playerList.add(new Player("Player "+(i+1)));
        }
        // return Array
        return playerList;
    }
}
