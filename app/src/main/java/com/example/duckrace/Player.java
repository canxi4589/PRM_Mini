package com.example.duckrace;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Player implements Parcelable {
    private String name;
    private List<Integer> selectedDucks;

    public Player(String name) {
        this.name = name;
        this.selectedDucks = new ArrayList<>();
    }

    protected Player(Parcel in) {
        name = in.readString();
        selectedDucks = new ArrayList<>();
        in.readList(selectedDucks, Integer.class.getClassLoader());
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getSelectedDucks() {
        return selectedDucks;
    }

    public void addSelectedDuck(int duckId) {
        if (!selectedDucks.contains(duckId)) {
            selectedDucks.add(duckId);
        }
    }

    public void removeSelectedDuck(int duckId) {
        selectedDucks.remove(Integer.valueOf(duckId));
    }

    public void clearSelectedDucks() {
        selectedDucks.clear();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeList(selectedDucks);
    }
}
