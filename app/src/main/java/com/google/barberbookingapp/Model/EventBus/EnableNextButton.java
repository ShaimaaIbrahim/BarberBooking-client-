package com.google.barberbookingapp.Model.EventBus;

import com.google.barberbookingapp.Model.entities.Barber;
import com.google.barberbookingapp.Model.entities.Salon;

public class EnableNextButton {

    private int  next , timeSlot ;
    private Barber barber;
    private Salon salon;


    public EnableNextButton() {
    }

    public Salon getSalon() {
        return salon;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public EnableNextButton setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
        return this;
    }

    public EnableNextButton(int next, int timeSlot) {
        this.next = next;
        this.timeSlot = timeSlot;
    }

    public EnableNextButton setSalon(Salon salon) {
        this.salon = salon;
        return this;
    }

    public EnableNextButton(int next, Salon salon) {
        this.next = next;
        this.salon = salon;
    }

    public EnableNextButton(Barber barber, int next) {
        this.barber = barber;
        this.next = next;
    }

    public int getNext() {
        return next;
    }

    public EnableNextButton setNext(int next) {
        this.next = next;
        return this;
    }

    public Barber getBarber() {
        return barber;
    }

    public EnableNextButton setBarber(Barber barber) {
        this.barber = barber;
        return this;
    }
}
