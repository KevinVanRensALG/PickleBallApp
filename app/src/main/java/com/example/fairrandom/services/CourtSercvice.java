package com.example.fairrandom.services;

import com.example.fairrandom.beans.Court;

public interface CourtSercvice {

    public Court[] getCourts(Object object);

    public String[] getCourtnames(Court[] courts);
}
