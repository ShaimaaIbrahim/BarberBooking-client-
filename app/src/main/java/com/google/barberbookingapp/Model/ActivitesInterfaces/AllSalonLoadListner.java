package com.google.barberbookingapp.Model.ActivitesInterfaces;

import java.util.List;

public interface AllSalonLoadListner {

    void onAllSalonLoadSuccess(List<String > areaNameSalons);
    void onAllSalonLoadFailed(String message);


}
