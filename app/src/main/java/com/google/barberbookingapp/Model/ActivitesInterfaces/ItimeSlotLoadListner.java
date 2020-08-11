package com.google.barberbookingapp.Model.ActivitesInterfaces;

import com.google.barberbookingapp.Model.entities.TimeSlot;

import java.util.List;

public interface ItimeSlotLoadListner {

    void LoadTimeSlotSuccess(List<TimeSlot> timeSlotList);
    void LoadTimeSlotFailed(String message);
    void LoadTimeSlotEmpty();
}
