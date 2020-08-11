package com.google.barberbookingapp.Model.entities;

public class TimeSlot {

    private int slot;

    public int getSlot() {
        return slot;
    }

    public TimeSlot setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public TimeSlot() {
    }

    public TimeSlot(int slot) {
        this.slot = slot;
    }
}
