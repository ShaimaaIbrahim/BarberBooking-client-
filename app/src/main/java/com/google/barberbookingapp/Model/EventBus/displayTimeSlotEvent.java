package com.google.barberbookingapp.Model.EventBus;

public class displayTimeSlotEvent {

    private boolean isDisplay;

    public displayTimeSlotEvent(boolean isDisplay) {
        this.isDisplay = isDisplay;
    }

    public boolean isDisplay() {
        return isDisplay;
    }

    public displayTimeSlotEvent setDisplay(boolean display) {
        isDisplay = display;
        return this;
    }

    public displayTimeSlotEvent() {
    }
}
