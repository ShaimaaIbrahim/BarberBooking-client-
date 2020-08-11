package com.google.barberbookingapp.Model.EventBus;

public class confirmBookingEvent {

    public boolean isConfirm;

    public confirmBookingEvent() {
    }

    public boolean isConfirm() {
        return isConfirm;
    }

    public confirmBookingEvent(boolean isConfirm) {
        this.isConfirm = isConfirm;
    }

    public confirmBookingEvent setConfirm(boolean confirm) {

        return this;
    }
}
