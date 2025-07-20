package com.example.fairrandom.services;

import com.example.fairrandom.beans.Player;

import java.util.ArrayList;
import java.util.HashMap;

public interface CourtPlayerGeneratorService {

    Player[] generateCourtPlayers(HashMap<Integer, ArrayList<Player>>  playerMap);
}
