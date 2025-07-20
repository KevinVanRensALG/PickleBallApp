package com.example.fairrandom.services;

import com.example.fairrandom.beans.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public interface CourtPlayerGeneratorService {

    public Player[] generateCourtPlayers(HashMap<Integer, ArrayList<Player>>  playerMap);
}
