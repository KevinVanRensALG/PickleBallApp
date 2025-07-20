package com.example.fairrandom.services;

import com.example.fairrandom.beans.Court;

public interface CourtSercvice {

    Court[] getCourts(Object object);

    String[] getCourtnames(Court[] courts);
}
