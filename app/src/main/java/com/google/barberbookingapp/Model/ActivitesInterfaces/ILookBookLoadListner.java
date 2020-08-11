package com.google.barberbookingapp.Model.ActivitesInterfaces;

import com.google.barberbookingapp.Model.entities.Banner;

import java.util.List;

public interface ILookBookLoadListner {
    void onLookBookLoadSucceded(List<Banner> bannerList);
    void onLookBookLoadFailed(String message);
}
