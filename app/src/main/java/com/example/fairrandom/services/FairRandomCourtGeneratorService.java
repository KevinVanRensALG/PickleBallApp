package com.example.fairrandom.services;

import com.example.fairrandom.beans.Player;

import java.util.ArrayList;
import java.util.HashMap;


public class FairRandomCourtGeneratorService implements CourtPlayerGeneratorService {

    private Player[] playerArray;
    private int playerSelected;
    private int leastGamesPlayedNumber;

    @Override
    public Player[] generateCourtPlayers(HashMap<Integer, ArrayList<Player>> playerMap) {
        // set Array
        playerArray = new Player[4];
        // int variable
        playerSelected = 0;
        leastGamesPlayedNumber = 0;
        //  while less then 4 players selected
        while (playerSelected < 4) {
            if (!playerMap.containsKey(leastGamesPlayedNumber)) {
                leastGamesPlayedNumber++;
            } else {
                //check if we have at least 4 players to choose from
                if (playerMap.get(leastGamesPlayedNumber).size() >= (4 - playerSelected)) {
                    //if yes then randomly select 4 players and add then to Array
                    randomSelect(playerMap);
                } else {
                    // if not add all players to Array and find all players with next least games played
                    while (!(playerMap.get(leastGamesPlayedNumber).isEmpty())) {
                        playerArray[playerSelected] = playerMap.get(leastGamesPlayedNumber).get(0);
                        playerMap.get(leastGamesPlayedNumber).remove(0);
                        playerSelected++;
                    }
                    leastGamesPlayedNumber++;
                }
            }
        }
        return playerArray;
    }

    private void randomSelect(HashMap<Integer, ArrayList<Player>> playerMap) {
        // get random number
        // min ans max variables
        int min = 0;
        int max = playerMap.get(leastGamesPlayedNumber).size() - 1;
        int randomNumber = (int) (Math.random() * (max - min + 1));
        // select random player
        playerArray[playerSelected] = playerMap.get(leastGamesPlayedNumber).get(randomNumber);
        // update player's playing status
        playerArray[playerSelected].setPlaying(true);
        // remove selected player
        playerMap.get(leastGamesPlayedNumber).remove(randomNumber);
        // increase number of selected players
        playerSelected++;
    }
}
