package com.google.barberbookingapp.Model.EventBus;

import com.google.barberbookingapp.Model.entities.Barber;

import java.util.List;

public class BarberDoneEvent {

    private List<Barber> barberList;

    public BarberDoneEvent(List<Barber> barberList) {
        this.barberList = barberList;
    }

    public BarberDoneEvent() {
    }

    public List<Barber> getBarberList() {
        return barberList;
    }

    public BarberDoneEvent setBarberList(List<Barber> barberList) {
        this.barberList = barberList;
        return this;
    }
}
