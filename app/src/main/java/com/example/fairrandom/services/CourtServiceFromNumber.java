package com.example.fairrandom.services;

import com.example.fairrandom.beans.Court;

public class CourtServiceFromNumber implements  CourtSercvice{


    @Override
    public Court[] getCourts(Object object) {
        // cast object
        int courtNumber = (int) object;
        // create array
        Court[] courts = new Court[courtNumber];
        for (int i = 0; i < courts.length; i++){
            courts[i] = new Court("Court "+(i+1));
            courts[i].setEmpty();
        }
        return courts;
    }

    @Override
    public String[] getCourtnames(Court[] courts) {
        String[] names = new String[courts.length];
        for( int i=0; i<courts.length;i++){
            names[i] = courts[i].getName();
        }
        return names;
    }
}
