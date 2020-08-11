package com.google.barberbookingapp.Model.ActivitesInterfaces;

import com.google.barberbookingapp.Model.entities.Salon;

import java.util.List;

public interface IBrabchLoadListner {

    void onBranchLoadSuccess(List<Salon> salonList);
    void onBranchLoadFailed(String message);
}
