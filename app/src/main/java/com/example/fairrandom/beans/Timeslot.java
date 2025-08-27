package com.example.fairrandom.beans;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Timeslot implements Parcelable {
    private int id;
    private LocalDateTime startTime, endTime;

    public Timeslot() {
    }

    public Timeslot(int id, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    protected Timeslot(Parcel in) {
        id = in.readInt();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startTime = LocalDateTime.parse(in.readString());
            endTime = LocalDateTime.parse(in.readString());
        }
    }

    public static final Creator<Timeslot> CREATOR = new Creator<Timeslot>() {
        @Override
        public Timeslot createFromParcel(Parcel in) {
            return new Timeslot(in);
        }

        @Override
        public Timeslot[] newArray(int size) {
            return new Timeslot[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @NonNull
    @Override
    public String toString() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return startTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
                    +" - "
                    + endTime.format(DateTimeFormatter.ofPattern("hh:mm a")) ;
        }
        return "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(startTime.toString());
        parcel.writeString(endTime.toString());
    }
}
